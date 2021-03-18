package co.empathy.search.request;

import co.empathy.search.request.filters.RequestFilter;
import io.micronaut.context.annotation.Replaces;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Replaces(MyRequest.class)
@Singleton
public class MockMyRequest implements MyRequest {

	@NotNull
	private Map<String, String> musts;

	@NotNull
	private List<RequestFilter> filters;

	@NotNull
	private Map<String, String> aggregationBuckets;

	public MockMyRequest() {
		this.musts = new HashMap<>();
		this.filters = new ArrayList<>();
		this.aggregationBuckets = new HashMap<>();
	}

	@Override
	public @NotNull Map<String, String> musts() {
		return musts;
	}

	/**
	 * @param musts mock of the musts() call
	 */
	public void mockMusts(Map<String, String> musts) {
		this.musts = musts;
	}

	@Override
	public @NotNull List<RequestFilter> filters() {
		return filters;
	}

	/**
	 * @param filters   mock of the filters() call
	 */
	public void mockFilters(List<RequestFilter> filters) {
		this.filters = filters;
	}

	@Override
	public @NotNull Map<String, String> aggregationBuckets() {
		return aggregationBuckets;
	}

	/**
	 * @param aggregationBuckets    mock of the aggregationBuckets() call
	 */
	public void mockAggregationBuckets(Map<String, String> aggregationBuckets) {
		this.aggregationBuckets = aggregationBuckets;
	}

}
