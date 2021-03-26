package co.empathy.engines.elastic;

import co.empathy.engines.FunctionVisitor;
import co.empathy.search.request.functions.TermWeightingFunction;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.weightFactorFunction;

@Singleton
public class ElasticFunctionVisitor implements FunctionVisitor {

	@Override
	@NotNull
	public Object transform(TermWeightingFunction range) {
		return new FunctionScoreQueryBuilder.FilterFunctionBuilder(
				matchQuery(range.getField(), range.getText()),
				weightFactorFunction(range.getWeight())
		);
	}

}
