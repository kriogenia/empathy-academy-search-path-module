package co.empathy.engines.elastic;

import co.empathy.engines.AggregationVisitor;
import co.empathy.search.request.aggregations.TermsAggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;

import javax.inject.Singleton;

@Singleton
public class ElasticAggregationVisitor implements AggregationVisitor {

	@Override
	public Object transform(TermsAggregation terms) {
		return AggregationBuilders.terms(terms.getName()).field(terms.getField());
	}

}
