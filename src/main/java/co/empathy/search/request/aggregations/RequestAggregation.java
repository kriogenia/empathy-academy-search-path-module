package co.empathy.search.request.aggregations;

import co.empathy.engines.AggregationVisitor;
import co.empathy.engines.FilterVisitor;
import co.empathy.search.request.filters.RequestFilter;

import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

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
	 * @return  the order for the aggregation, true if it should be ascendant, false for descendant
	 */
	boolean orderAscendant();

	/**
	 * @param ascendant   true for ascendant order and false for descendant order
	 */
	void setAscendant(boolean ascendant);

	/**
	 * Sets the list of filters the aggregation must apply to its results
	 * @return  modified aggregation
	 */
	@NotNull
	RequestAggregation setFilters(List<RequestFilter> filters);

	/**
	 * @return  the list of filters to apply to the aggregation
	 */
	@NotNull
	List<RequestFilter> getFilters();

	/**
	 * Visitor class method to manage filter conversion between engines
	 * @param visitor   visitor to manage the parsing
	 * @return          parsed filter made by the visitor
	 */
	@NotNull
	Object accept(@NotNull AggregationVisitor visitor);

}
