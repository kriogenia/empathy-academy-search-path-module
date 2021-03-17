package co.empathy.search.request;

import io.micronaut.context.annotation.Replaces;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Replaces(MyRequest.class)
@Singleton
public class MockMyRequest implements MyRequest {

	@NotNull
	private Map<String, String> musts;

	@NotNull
	private Map<String, String[]> filters;

	@NotNull
	private Map<String, String> aggregationBuckets;

	public MockMyRequest() {
		this.musts = new HashMap<>();
		this.filters = new HashMap<>();
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
	public @NotNull Map<String, String[]> filters() {
		return filters;
	}

	/**
	 * @param filters   mock of the filters() call
	 */
	public void mockFilters(Map<String, String[]> filters) {
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
