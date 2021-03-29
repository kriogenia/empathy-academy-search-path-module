package co.empathy.search.request.queries;

import co.empathy.engines.QueryVisitor;

import javax.validation.constraints.NotNull;

/**
 * Query to be used on the search request
 */
public interface RequestQuery {

	/**
	 * Visitor class method to manage query conversion between engines
	 * @param visitor   visitor to manage the parsing
	 * @return          parsed query made by the visitor
	 */
	@NotNull
	Object accept(@NotNull QueryVisitor visitor);

}
