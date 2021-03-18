package co.empathy.search.request.aggregations;

import co.empathy.engines.AggregationVisitor;
import co.empathy.engines.FilterVisitor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Aggregation specified on the search request
 */
public interface RequestAggregation {

	/**
	 * @return  name of the aggregation
	 */
	@NotEmpty
	String getName();

	/**
	 * @return  name of the field to aggregate
	 */
	@NotEmpty
	String getField();

	/**
	 * Visitor class method to manage filter conversion between engines
	 * @param visitor   visitor to manage the parsing
	 * @return          parsed filter made by the visitor
	 */
	@NotNull
	Object accept(AggregationVisitor visitor);

}
