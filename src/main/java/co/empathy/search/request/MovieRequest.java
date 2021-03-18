package co.empathy.search.request;

import co.empathy.common.ImdbItem;
import co.empathy.search.request.aggregations.RequestAggregation;
import co.empathy.search.request.aggregations.TermsAggregation;
import co.empathy.search.request.filters.RangeFilter;
import co.empathy.search.request.filters.RequestFilter;
import co.empathy.search.request.filters.TermsFilter;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.QueryValue;
import io.reactivex.annotations.NonNull;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Introspected
public class MovieRequest implements MyRequest {

	public static final String GENRES_AGG = "genres";
	public static final String TYPES_AGG = "types";

	private final HttpRequest<?> httpRequest;

	@NotNull
	@QueryValue
	private final String query;

	@Nullable
	@QueryValue
	private final List<RequestFilter> filters;

	@Nullable
	private final List<RequestAggregation> aggs;

	public MovieRequest(HttpRequest<?> httpRequest,
	                    @NonNull String query,
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
			this.filters.add(new RangeFilter(ImdbItem.START, year));
		}
		aggs = setRequestAggregations();
	}

	@NonNull
	private List<RequestAggregation> setRequestAggregations() {
		final @Nullable List<RequestAggregation> aggs;
		// Aggregations
		aggs = new ArrayList<>();
		aggs.add(new TermsAggregation(GENRES_AGG, ImdbItem.GENRES));
		aggs.add(new TermsAggregation(TYPES_AGG, ImdbItem.TYPE));
		//aggs.add(new )
		return aggs;
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
		return aggs;
	}

}
