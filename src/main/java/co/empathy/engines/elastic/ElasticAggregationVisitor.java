package co.empathy.engines.elastic;

import co.empathy.engines.AggregationVisitor;
import co.empathy.search.request.aggregations.DividedRangeAggregation;
import co.empathy.search.request.aggregations.RangeAggregation;
import co.empathy.search.request.aggregations.TermsAggregation;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;

/**
 * Visitor to transform common aggregations to Elastic Search specific ones
 */
@Singleton
public class ElasticAggregationVisitor implements AggregationVisitor {

	@Inject
	ElasticFilterVisitor filterVisitor;

	@Override
	@NotNull
	public Object transform(DividedRangeAggregation range) {
		var builder = AggregationBuilders.dateRange(range.getName());
		builder.field(range.getField());
		// One range for each decade
		int from;
		int to = range.getTo();
		int gap = range.getGap();
		for (from = range.getFrom(); from < to - gap; from += gap) {
			builder.addRange(from, from + gap);
		}
		builder.addUnboundedFrom(from);
		return builder;
	}

	@Override
	@NotNull
	public Object transform(RangeAggregation range) {
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
	public Object transform(TermsAggregation terms) {
		var aggregation = AggregationBuilders
				.terms(terms.getName())
				.field(terms.getField())
				.size(terms.getSize());
		if (terms.getFilters().isEmpty()) {
			return aggregation;
		}
		else {
			var filters = new ArrayList<QueryBuilder>();
			terms.getFilters().forEach((filter) -> filters.add((QueryBuilder) filter.accept(filterVisitor)));
			return AggregationBuilders
					.filters(terms.getName() + "_filtered", filters.toArray(new QueryBuilder[0]))
					.subAggregation(aggregation);
		}
	}

}
