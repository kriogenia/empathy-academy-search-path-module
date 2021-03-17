package co.empathy.search.request;

import co.empathy.common.ImdbItem;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.QueryValue;
import io.reactivex.annotations.NonNull;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
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
	private final String[] genres;

	@Nullable
	@QueryValue
	private final String[] types;

	public MovieRequest(HttpRequest<?> httpRequest,
	                    @NonNull String query,
	                    @Nullable String genres,
	                    @Nullable String type) {
		this.httpRequest = httpRequest;
		this.query = query;
		this.genres = (genres != null) ? genres.split(",") : null;
		this.types = (type != null) ? type.split(",") : null;
	}

	@Override
	@NotNull
	public Map<String, String> musts(){
		Map<String, String> map = new HashMap<>();
		map.put(ImdbItem.ORIGINAL_TITLE, query);
		return map;
	}

	@Override
	@NotNull
	public Map<String, String[]> filters() {
		Map<String, String[]> map = new HashMap<>();
		if (genres != null) {
			map.put(ImdbItem.GENRES, genres);
		}
		if (types != null) {
			map.put(ImdbItem.TYPE, types);
		}
		return map;
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
