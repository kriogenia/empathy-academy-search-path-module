package co.empathy.search.request.filters;

import co.empathy.engines.FilterVisitor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Filter received on the search requests
 */
public interface RequestFilter {

	/**
	 * @return  name of the field to filter
	 */
	@NotEmpty
	String getField();

	/**
	 * Visitor class method to manage filter conversion between engines
	 * @param visitor   visitor to manage the parsing
	 * @return          parsed filter made by the visitor
	 */
	@NotNull
	Object accept(@NotNull FilterVisitor visitor);

}
