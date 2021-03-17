package co.empathy.search.request;

import co.empathy.search.request.filters.RequestFilter;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public interface MyRequest {

	/**
	 * @return  map with the required queries
	 */
	@NotNull
	Map<String, String> musts();

	/**
	 * @return  list of the requested filters
	 */
	@NotNull List<RequestFilter> filters();

	/**
	 * @return  map with the aggregation requests
	 */
	@NotNull
	Map<String, String> aggregationBuckets();

}
