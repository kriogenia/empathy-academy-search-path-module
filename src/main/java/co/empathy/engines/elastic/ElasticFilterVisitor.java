package co.empathy.engines.elastic;

import co.empathy.engines.FilterVisitor;
import co.empathy.search.request.filters.TermsFilter;
import org.elasticsearch.index.query.QueryBuilders;

import javax.inject.Singleton;

@Singleton
public class ElasticFilterVisitor implements FilterVisitor {

	@Override
	public Object visit(TermsFilter filter) {
		return QueryBuilders.termsQuery(filter.getField(), filter.getTerms());
	}

}
