package co.empathy.engines.elastic;

import co.empathy.engines.FilterVisitor;
import co.empathy.search.request.filters.DateRangesFilter;
import co.empathy.search.request.filters.TermsFilter;
import io.reactivex.annotations.NonNull;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

/**
 * Visitor that transforms general filters to Elastic Search filters
 */
@Singleton
public class ElasticFilterVisitor implements FilterVisitor {

	@Override
	@NotNull
	public QueryBuilder transform(@NotNull DateRangesFilter filter) {
		var ranges = filter.getRanges();
		if (ranges.size() == 1) {
			return getRangeQueryBuilder(filter, ranges.get(0));
		} else {
			var query = QueryBuilders.boolQuery();
			ranges.forEach(r -> query.should(getRangeQueryBuilder(filter, r)));
			return query;
		}
	}

	@Override
	@NotNull
	public QueryBuilder transform(@NotNull TermsFilter filter) {
		return QueryBuilders.termsQuery(filter.getField(), filter.getTerms());
	}

	/**
	 * Builds a RangeQueryBuilder from a filter and range
	 * @param filter    filter with the info to build the query
	 * @param range     range for the query
	 * @return          specified range query
	 */
	@NonNull
	private RangeQueryBuilder getRangeQueryBuilder(DateRangesFilter filter, DateRangesFilter.Range range) {
		var query = QueryBuilders.rangeQuery(filter.getField());
		query.from(range.from);
		query.to(range.to);
		query.format(filter.getFormat());
		return query;
	}

}
