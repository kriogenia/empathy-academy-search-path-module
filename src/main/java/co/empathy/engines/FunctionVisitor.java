package co.empathy.engines;

import co.empathy.search.request.functions.FieldValueFunction;
import co.empathy.search.request.functions.GaussDecayFunction;
import co.empathy.search.request.functions.TermWeightingFunction;

import javax.validation.constraints.NotNull;

public interface FunctionVisitor {

	/**
	 * @param function     term weighting function to transform
	 * @return          engine specific term weighting function
	 */
	@NotNull
	Object transform(@NotNull TermWeightingFunction function);

	/**
	 * @param function      field value factor function to transform
	 * @return              engine specific field value factor function
	 */
	@NotNull
	Object transform(@NotNull FieldValueFunction function);

	/**
	 * @param function      gauss decay function to transform
	 * @return              engine specific gauss decay function
	 */
	@NotNull
	Object transform(@NotNull GaussDecayFunction function);

}