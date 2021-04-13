package co.empathy.engines.elastic;

import co.empathy.engines.AggregationVisitor;
import co.empathy.search.request.aggregations.DividedRangeAggregation;
import co.empathy.search.request.aggregations.RangeAggregation;
import co.empathy.search.request.aggregations.RequestAggregation;
import co.empathy.search.request.aggregations.TermsAggregation;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.BucketOrder;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.aggregations.pipeline.BucketSortPipelineAggregationBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Visitor to transform common aggregations to Elastic Search specific ones
 */
@Singleton
public class ElasticAggregationVisitor implements AggregationVisitor {

	@Inject
	ElasticFilterVisitor filterVisitor;

	@Override
	@NotNull
	public Object transform(@NotNull DividedRangeAggregation range) {
		var aggregation = AggregationBuilders.dateRange(range.getName());
		aggregation.field(range.getField());
		// One range for each decade
		int from;
		int to = range.getTo();
		int gap = range.getGap();
		for (from = range.getFrom(); from < to - gap; from += gap) {
			aggregation.addRange(from, from + gap);
		}
		// Last open range
		aggregation.addUnboundedFrom(from);
		// Order of the aggregation
		var sorterList = new ArrayList<FieldSortBuilder>();
		sorterList.add(new FieldSortBuilder("_key")
				.order(range.orderAscendant() ? SortOrder.ASC : SortOrder.DESC));
		aggregation.subAggregation(new BucketSortPipelineAggregationBuilder(range.getName() + "_sorted", sorterList));
		// Addition or not of the filters
		return (range.getFilters().isEmpty())
				? aggregation
				:  makeAggregationFiltered(range, aggregation);
	}

	@Override
	@NotNull
	public Object transform(@NotNull RangeAggregation range) {
		var builder = AggregationBuilders.dateRange(range.getName());
		builder.field(range.getField());
		if (range.getFrom() != null) {
			if (range.getTo() != null) {
				builder.addRange(range.getFrom(), range.getTo());
			} else {
				builder.addUnboundedFrom(range.getFrom());
			}
		} else {
			if (range.getTo() != null) {
				builder.addUnboundedTo(range.getTo());
			} else {
				throw new IllegalArgumentException("Range aggregations must have at least one edge");
			}
		}
		return builder;
	}

	@Override
	@NotNull
	public Object transform(@NotNull TermsAggregation terms) {
		var aggregation = AggregationBuilders
				.terms(terms.getName())
				.field(terms.getField())
				.size(terms.getSize())
				.order(BucketOrder.key(terms.orderAscendant()));
		return (terms.getFilters().isEmpty())
				? aggregation
				:  makeAggregationFiltered(terms, aggregation);
	}

	/**
	 * Builds a filtered aggregation with the filters and the sub aggregation
	 * @param requested         requested aggregation with the filters and name
	 * @param aggregation       sub aggregation to filter
	 * @return                  filter aggregation with the wanted sub aggregation
	 */
	private FilterAggregationBuilder makeAggregationFiltered(RequestAggregation requested, AggregationBuilder aggregation) {
		var query = QueryBuilders.boolQuery();
		requested.getFilters().forEach((filter) -> query.must((QueryBuilder) filter.accept(filterVisitor)));
		return AggregationBuilders
				.filter(requested.getName() + "_filtered", query)
				.subAggregation(aggregation);
	}



}
