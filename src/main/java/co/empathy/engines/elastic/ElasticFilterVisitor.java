package co.empathy.engines.elastic;

import co.empathy.engines.FilterVisitor;
import co.empathy.search.request.filters.DateRangesFilter;
import co.empathy.search.request.filters.TermsFilter;
import io.reactivex.annotations.NonNull;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;

import javax.inject.Singleton;

/**
 * Visitor that transforms general filters to Elastic Search filters
 */
@Singleton
public class ElasticFilterVisitor implements FilterVisitor {

	@Override
	public Object transform(DateRangesFilter filter) {
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
	public Object transform(TermsFilter filter) {
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