package co.empathy.search.request;

import co.empathy.common.ImdbItem;
import co.empathy.search.request.aggregations.DividedRangeAggregation;
import co.empathy.search.request.aggregations.RequestAggregation;
import co.empathy.search.request.aggregations.TermsAggregation;
import co.empathy.search.request.filters.DateRangesFilter;
import co.empathy.search.request.filters.RequestFilter;
import co.empathy.search.request.filters.TermsFilter;
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

	private static final int START_YEAR = 1890;
	private static final int LAST_YEAR = Calendar.getInstance().get(Calendar.YEAR);
	private static final int YEAR_GAP = 10;

	private final HttpRequest<?> httpRequest;

	@NotNull(message = "Missing query to search. To not specify one, submit and empty query")
	@QueryValue
	private final String query;

	@Nullable
	@QueryValue
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
				START_YEAR, LAST_YEAR, YEAR_GAP));
		return aggs;
	}

}
