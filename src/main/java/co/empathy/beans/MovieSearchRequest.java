package co.empathy.beans;

import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.QueryValue;
import io.reactivex.annotations.NonNull;

import javax.annotation.Nullable;

@Introspected
public class MovieSearchRequest {

	private final HttpRequest<?> httpRequest;

	@NonNull
	@QueryValue
	private String query;

	@Nullable
	@QueryValue
	private String[] genres;

	@Nullable
	@QueryValue
	private String[] type;

	public MovieSearchRequest(HttpRequest<?> httpRequest,
	                          @NonNull String query,
	                          @Nullable String genres,
	                          @Nullable String type) {
		this.httpRequest = httpRequest;
		this.query = query;
		this.genres = (genres != null) ? genres.split(",") : null;
		this.type = (type != null) ? type.split(",") : null;
	}

}
