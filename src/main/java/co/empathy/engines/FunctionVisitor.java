package co.empathy.engines;

import co.empathy.search.request.functions.TermWeightingFunction;

import javax.validation.constraints.NotNull;

public interface FunctionVisitor {

	/**
	 * @param range     term weighting function to transform
	 * @return          engine specific term weighting function
	 */
	@NotNull
	Object transform(TermWeightingFunction range);

}