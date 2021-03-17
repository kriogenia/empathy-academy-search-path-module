package co.empathy.search.request.filters;

import co.empathy.engines.FilterVisitor;

/**
 * Filters received on the search requests
 */
public interface RequestFilter {

	/**
	 * @return  name of the field to filter
	 */
	String getField();

	/**
	 * Visitor class method to manage filter conversion between engines
	 * @param visitor   visitor to manage the parsing
	 * @return          parsed filter made by the visitor
	 */
	Object accept(FilterVisitor visitor);


}
