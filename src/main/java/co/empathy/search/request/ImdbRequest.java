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
import co.empathy.search.request.suggestions.RequestSuggestion;
import co.empathy.search.request.suggestions.TermsSuggestion;
import io.micronaut.core.annotation.Introspected;
import io.micronaut.http.HttpRequest;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import java.util.*;

@Introspected
public class ImdbRequest implements MyRequest {

	public static final String GENRES_AGG = "genres";
	public static final String TYPES_AGG = "types";
	public static final String YEAR_AGG = "year";

	public static final int NUMBER_OF_GENRES = 27;

	@Nullable
	private final String query;

	@NotNull(message = "The list of filters must exists. If there's no filters, it should be empty")
	private final List<RequestFilter> filters;

	@NotNull(message = "The list of aggregations must exists. If there's no aggregations, it should be empty")
	private final List<RequestAggregation> aggs;

	@NotNull(message = "The list of suggestions must exists. If there's no suggestions, it should be empty")
	private final List<RequestSuggestion> suggestions;

	public ImdbRequest(HttpRequest<?> httpRequest,
	                   @Nullable String query,
	                   @Nullable String genres,
	                   @Nullable String type,
	                   @Nullable String year) {
		this.query = query;
		// Create the filters and aggregations of each query
		this.filters = new ArrayList<>();
		this.aggs = new ArrayList<>();
		filterGenres(genres);
		filterTypes(type);
		filterYear(year);
		// Add the filters to the aggregations
		this.aggs.forEach(a -> a.setFilters(new ArrayList<>(filters)));
		// Create the suggestions
		this.suggestions = new ArrayList<>();
		if (query != null && !query.isEmpty()) {
			this.suggestions.add(new TermsSuggestion(ImdbItem.ORIGINAL_TITLE, query));
		}
	}

	/**
	 * Creates the filter and aggregation of genres based on the query
	 * @param genres    query to filter genres, null if not applied
	 */
	private void filterGenres(@Nullable String genres) {
		var agg = new TermsAggregation(GENRES_AGG, ImdbItem.GENRES)
				.setSize(NUMBER_OF_GENRES);
		if (genres != null) {
			this.filters.add(new TermsFilter(ImdbItem.GENRES, genres));
		}
		this.aggs.add(agg);
	}

	/**
	 * Creates the filter and aggregation of types based on the query
	 * @param types    query to filter types, null if not applied
	 */
	private void filterTypes(@Nullable String types) {
		var agg = new TermsAggregation(TYPES_AGG, ImdbItem.TYPE)
				.setSize(ImdbItem.Types.values().length);
		if (types != null) {
			this.filters.add(new TermsFilter(ImdbItem.TYPE, types));
		}
		this.aggs.add(agg);
	}

	/**
	 * Creates the filter and aggregation of the year based on the query
	 * @param year    query to filter year, null if not applied
	 */
	private void filterYear(@Nullable String year) {
		var agg = new DividedRangeAggregation(YEAR_AGG, ImdbItem.START,
				1890, Calendar.getInstance().get(Calendar.YEAR), 10);
		agg.setAscendant(false);                // Descendant order
		if (year != null) {
			this.filters.add(new DateRangesFilter(ImdbItem.START, year));
		}
		this.aggs.add(agg);
	}

	@Override
	@NotNull
	public List<RequestQuery> musts() {
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
	@NotNull
	public List<RequestFilter> filters() {
		return filters;
	}

	@Override
	@NotNull
	public List<RequestAggregation> aggregations() {
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

	@Override
	@NotNull
	public  List<RequestSuggestion> suggestions() {
		return suggestions;
	}
}
