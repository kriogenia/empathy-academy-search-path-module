package co.empathy.engines.elastic;

import co.empathy.engines.FunctionVisitor;
import co.empathy.search.request.functions.FieldValueFunction;
import co.empathy.search.request.functions.TermWeightingFunction;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.fieldValueFactorFunction;
import static org.elasticsearch.index.query.functionscore.ScoreFunctionBuilders.weightFactorFunction;

@Singleton
public class ElasticFunctionVisitor implements FunctionVisitor {

	@Override
	@NotNull
	public Object transform(TermWeightingFunction function) {
		return new FunctionScoreQueryBuilder.FilterFunctionBuilder(
				matchQuery(function.getField(), function.getText()),
				weightFactorFunction(function.getWeight())
		);
	}

	@Override
	@NotNull
	public Object transform(FieldValueFunction function) {
		var builder = fieldValueFactorFunction("num_votes");
		if (function.getFactor() != null) {
			builder.factor(function.getFactor());
		}
		if (function.getModifier() != null) {
			builder.modifier(function.getModifier().elasticSearch());
		}
		if (function.getMissing() != null) {
			builder.missing(function.getMissing());
		}
		return new FunctionScoreQueryBuilder.FilterFunctionBuilder(builder);
	}
}
