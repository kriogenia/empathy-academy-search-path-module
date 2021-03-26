package co.empathy.search.request;

import co.empathy.common.ImdbItem;
import co.empathy.search.request.aggregations.DividedRangeAggregation;
import co.empathy.search.request.aggregations.RequestAggregation;
import co.empathy.search.request.aggregations.TermsAggregation;
import co.empathy.search.request.filters.DateRangesFilter;
import co.empathy.search.request.filters.RequestFilter;
import co.empathy.search.request.filters.TermsFilter;
import co.empathy.search.request.functions.RequestFunction;
import co.empathy.search.request.functions.TermWeightingFunction;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.QueryValue;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.*;

@Introspected
public class MovieRequest implements MyRequest {

	public static final String GENRES_AGG = "genres";
	public static final String TYPES_AGG = "types";
	public static final String YEAR_AGG = "year";

	private final HttpRequest<?> httpRequest;

	@NotNull(message = "Missing query to search. To not specify one, submit and empty query")
	private final String query;

	@NotNull(message = "The list of filters must exists. If there's no filters, it should be empty")
	private final List<RequestFilter> filters;

	public MovieRequest(HttpRequest<?> httpRequest,
	                    @NotNull String query,
	                    @Nullable String genres,
	                    @Nullable String type,
	                    @Nullable String year) {
		this.httpRequest = httpRequest;
		this.query = query;
		// Filters
		this.filters = new ArrayList<>();
		if (genres != null) {
			this.filters.add(new TermsFilter(ImdbItem.GENRES, genres));
		}
		if (type != null) {
			this.filters.add(new TermsFilter(ImdbItem.TYPE, type));
		}
		if (year != null) {
			this.filters.add(new DateRangesFilter(ImdbItem.START, year));
		}

	}

	@Override
	@NotNull
	public Map<String, String> musts(){
		Map<String, String> map = new HashMap<>();
		map.put(ImdbItem.ORIGINAL_TITLE, query);
		return map;
	}

	@Override
	public @NotNull List<RequestFilter> filters() {
		return filters;
	}

	@Override
	@NotNull
	public List<RequestAggregation> aggregations() {
		final @NotNull List<RequestAggregation> aggs = new ArrayList<>();
		aggs.add(new TermsAggregation(GENRES_AGG, ImdbItem.GENRES));
		aggs.add(new TermsAggregation(TYPES_AGG, ImdbItem.TYPE));
		aggs.add(new DividedRangeAggregation(YEAR_AGG, ImdbItem.START,
				1890, Calendar.getInstance().get(Calendar.YEAR), 10));
		return aggs;
	}

	@Override
	@NotNull
	public List<RequestFunction> functions() {
		final @NotNull List<RequestFunction> functions = new ArrayList<>();
		functions.add(new TermWeightingFunction(ImdbItem.TYPE, ImdbItem.Types.MOVIE.getText(), 1.5f));
		functions.add(new TermWeightingFunction(ImdbItem.TYPE, ImdbItem.Types.TVEPISODE.getText(), 0.1f));
		return functions;
	}

}
