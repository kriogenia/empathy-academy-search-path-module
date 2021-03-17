package co.empathy.engines.elastic;

import co.empathy.engines.FilterVisitor;
import co.empathy.search.request.filters.RangeFilter;
import co.empathy.search.request.filters.TermsFilter;
import org.elasticsearch.index.query.QueryBuilders;

import javax.inject.Singleton;

@Singleton
public class ElasticFilterVisitor implements FilterVisitor {

	@Override
	public Object transform(RangeFilter filter) {
		var query = QueryBuilders.rangeQuery(filter.getField());
		query.from(filter.getFrom());
		query.to(filter.getTo());
		query.format(filter.getFormat());
		return query;
	}

	@Override
	public Object transform(TermsFilter filter) {
		return QueryBuilders.termsQuery(filter.getField(), filter.getTerms());
	}

}
