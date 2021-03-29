package co.empathy.search.request;

import co.empathy.search.request.aggregations.RequestAggregation;
import co.empathy.search.request.filters.RequestFilter;
import co.empathy.search.request.functions.RequestFunction;
import co.empathy.search.request.queries.RequestQuery;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface MyRequest {

	/**
	 * @return  map with the required queries
	 */
	@NotNull
	List<RequestQuery> musts();

	/**
	 * @return  list of the filters applied to the request
	 */
	@NotNull
	List<RequestFilter> filters();

	/**
	 * @return  list with the aggregations of the request
	 */
	@NotNull
	List<RequestAggregation> aggregations();

	/**
	 * @return  list with the scoring functions applied to the request
	 */
	@NotNull
	List<RequestFunction> functions();

}
