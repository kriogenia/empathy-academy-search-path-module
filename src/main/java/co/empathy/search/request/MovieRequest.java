package co.empathy.search.request;

import co.empathy.common.ImdbItem;
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

	public MovieRequest(HttpRequest<?> httpRequest,
	                    @NonNull String query,
	                    @Nullable String genres,
	                    @Nullable String type,
	                    @Nullable String year) {
		this.httpRequest = httpRequest;
		this.query = query;
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
	public Map<String, String> aggregationBuckets() {
		Map<String, String> map = new HashMap<>();
		map.put(GENRES_AGG, ImdbItem.GENRES);
		map.put(TYPES_AGG, ImdbItem.TYPE);
		return map;
	}
}
