package co.empathy.search.request.functions;

import co.empathy.engines.AggregationVisitor;
import co.empathy.engines.FunctionVisitor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Function applied the modify the scoring on the search request
 */
public interface RequestFunction {

	/**
	 * @return  name of the field to apply the function
	 */
	@NotEmpty
	String getField();

	/**
	 * Visitor class method to manage function conversion between engines
	 * @param visitor   visitor to manage the parsing
	 * @return          parsed filter made by the visitor
	 */
	@NotNull
	Object accept(@NotNull FunctionVisitor visitor);

}
