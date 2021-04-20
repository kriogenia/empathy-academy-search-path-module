package co.empathy.search.request;

import co.empathy.search.request.aggregations.RequestAggregation;
import co.empathy.search.request.filters.RequestFilter;
import co.empathy.search.request.functions.RequestFunction;
import co.empathy.search.request.queries.RequestQuery;
import co.empathy.search.request.suggestions.RequestSuggestion;
import io.micronaut.context.annotation.Replaces;

import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Replaces(MyRequest.class)
@Singleton
public class MockMyRequest implements MyRequest {

	@NotNull
	private List<RequestQuery> musts;

	@NotNull
	private List<RequestFilter> filters;

	@NotNull
	private List<RequestAggregation> aggregations;

	@NotNull
	private List<RequestFunction> functions;

	@NotNull
	private List<RequestSuggestion> suggestions;

	public MockMyRequest() {
		this.musts = new ArrayList<>();
		this.filters = new ArrayList<>();
		this.aggregations = new ArrayList<>();
		this.functions = new ArrayList<>();
		this.suggestions = new ArrayList<>();
	}

	@Override
	public @NotNull List<RequestQuery> musts() {
		return musts;
	}

	/**
	 * @param musts mock of the musts() call
	 */
	public void mockMusts(List<RequestQuery> musts) {
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
	public @NotNull List<RequestAggregation> aggregations() {
		return aggregations;
	}

	/**
	 * @param aggregations    mock of the aggregation() call
	 */
	public void mockAggregations(List<RequestAggregation> aggregations) {
		this.aggregations = aggregations;
	}

	@Override
	public @NotNull List<RequestFunction> functions() {
		return functions;
	}

	/**
	 * @param functions mock of the functions() call
	 */
	public void mockScoringFunctions(List<RequestFunction> functions) {
		this.functions = functions;
	}

	@Override
	public @NotNull List<RequestSuggestion> suggestions() {
		return this.suggestions;
	}

	/**
	 * @param suggestions   mock of the suggestions() call
	 */
	public void mockSuggestions(List<RequestSuggestion> suggestions) {
		this.suggestions = suggestions;
	}
}
