package co.empathy.search;

import co.empathy.common.ImdbRating;
import co.empathy.search.request.MyRequest;
import co.empathy.search.response.SearchResult;
import co.empathy.engines.SearchEngine;
import co.empathy.common.ImdbItem;
import co.empathy.search.response.SearchResponse;
import io.micronaut.context.annotation.Prototype;
import io.reactivex.annotations.NonNull;

import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * IMDB dedicated Searcher class
 */
@Prototype
public class ImdbSearcher implements Searcher {

	public static final String INDEX = "imdb";

	@NonNull
	private SearchEngine engine;

	/**
	 * Constructor of the IMDB Searcher.
	 * As injected loads the ElasticSearchEngine by default.
	 * @param engine	Engine the Searcher will use.
	 */
	public ImdbSearcher(@NonNull @Named("elastic") SearchEngine engine) {
		this.engine = engine;
	}

	/**
	 * Changes the Searcher engine
	 * @param engine	New SearchEngine to use
	 */
	public void setEngine(@NonNull SearchEngine engine) {
		this.engine = engine;
	}

	@Override
	public Serializable searchByQuery(MyRequest request) throws IOException {
		var result = engine.scoredSearch(request, INDEX);
		return buildResponse(result);
	}

	@Override
	public Serializable searchById(String id) throws IOException {
		var item = new ImdbItem();
		item.setId(id);
		item.setVotes(100);
		item.setEndYear("1900");
		return item;
	}

	/**
	 * Converts a SearchResult into a SearchResponse
	 * @param result	result of the search given by the engine
	 * @return			SearchResponse with the ImdbItems
	 */
	private Serializable buildResponse(SearchResult result) {
		// Convert map to items
		var items = result.getItems().stream().map(
				this::itemBuilder
		).collect(Collectors.toList());
		// Generate a response object
		return new SearchResponse<ImdbItem>()
				.setTotal(result.getTotal())
				.setItems(items)
				.setAggregations(result.getAggregations());
	}

	/**
	 * Builds an ImdbItem from the properties map
	 * @param properties	Retrieved properties of the search
	 * @return				Imdb Item with the specified properties
	 */
	private ImdbItem itemBuilder(Map<String, Object> properties) {
		// Cast the genres list
		List<String> genresList = new ArrayList<>();
		Object retrieved = properties.get(ImdbItem.GENRES);
		if (retrieved instanceof List<?>) {
			for (Object o: (List<?>) retrieved) {
				if (o instanceof String) {
					genresList.add((String) o);
				}
			}
		}
		// Build and return the item
		ImdbItem item = new ImdbItem()
				.setId(properties.get(ImdbItem.ID).toString())
				.setTitleType(properties.get(ImdbItem.TYPE).toString())
				.setPrimaryTitle(properties.get(ImdbItem.TITLE).toString())
				.setOriginalTitle(properties.get(ImdbItem.ORIGINAL_TITLE).toString())
				.setIsAdult(properties.get(ImdbItem.IS_ADULT).toString())
				.setStartYear(properties.get(ImdbItem.START).toString())
				.setRuntime(properties.get(ImdbItem.RUNTIME_MINUTES).toString())
				.setAverageRating((Double) properties.get(ImdbRating.AVERAGE))
				.setVotes((Integer) properties.get(ImdbRating.VOTES))
				.setGenres(genresList.toArray(new String[0]));
		if (properties.get(ImdbItem.END) != null) {
			item.setEndYear(properties.get(ImdbItem.END).toString());
		}
		return item;
	}

}
