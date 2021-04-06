package co.empathy.search.request;

import co.empathy.common.ImdbItem;
import co.empathy.common.ImdbRating;
import co.empathy.search.request.aggregations.DividedRangeAggregation;
import co.empathy.search.request.aggregations.RequestAggregation;
import co.empathy.search.request.aggregations.TermsAggregation;
import co.empathy.search.request.filters.DateRangesFilter;
import co.empathy.search.request.filters.RequestFilter;
import co.empathy.search.request.filters.TermsFilter;
import co.empathy.search.request.functions.FieldValueFunction;
import co.empathy.search.request.functions.GaussDecayFunction;
import co.empathy.search.request.functions.RequestFunction;
import co.empathy.search.request.functions.TermWeightingFunction;
import co.empathy.search.request.queries.DisjunctionMaxQuery;
import co.empathy.search.request.queries.PartialPlusPerfectQuery;
import co.empathy.search.request.queries.RequestQuery;
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

	public static final int NUMBER_OF_GENRES = 26;

	private final HttpRequest<?> httpRequest;

	@Nullable
	private final String query;

	@NotNull(message = "The list of filters must exists. If there's no filters, it should be empty")
	private final List<RequestFilter> filters;

	public MovieRequest(HttpRequest<?> httpRequest,
	                    @Nullable String query,
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
	public List<RequestQuery> musts(){
		List<RequestQuery> queries = new ArrayList<>();
		// If no query was specified return an empty list
		if (query == null || query.isEmpty()) {
			return queries;
		}
		// In the other case generate the titles query
		var original = new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, query);
		var primary = new PartialPlusPerfectQuery(ImdbItem.TITLE, query);
		var titlesQuery = new DisjunctionMaxQuery();
		titlesQuery.add(original);
		titlesQuery.add(primary);
		queries.add(titlesQuery);
		return queries;
	}

	@Override
	public @NotNull List<RequestFilter> filters() {
		return filters;
	}

	@Override
	@NotNull
	public List<RequestAggregation> aggregations() {
		final @NotNull List<RequestAggregation> aggs = new ArrayList<>();
		aggs.add(new TermsAggregation(GENRES_AGG, ImdbItem.GENRES)
				.setSize(NUMBER_OF_GENRES));
		aggs.add(new TermsAggregation(TYPES_AGG, ImdbItem.TYPE)
				.setSize(ImdbItem.Types.values().length));
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
		functions.add(new FieldValueFunction(ImdbRating.VOTES, 0.5f, FieldValueFunction.Modifier.LOG1P, 0f));
		functions.add(new FieldValueFunction(ImdbRating.AVERAGE, 0.2f, FieldValueFunction.Modifier.SQUARE, 0f));
		functions.add(new GaussDecayFunction(ImdbItem.START, "now", "10950d", 0.8)
				.setOffset("1825d"));
		return functions;
	}

}
