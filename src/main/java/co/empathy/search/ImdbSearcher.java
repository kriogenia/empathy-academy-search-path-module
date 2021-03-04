package co.empathy.search;

import co.empathy.beans.SearchResult;
import co.empathy.engines.SearchEngine;
import co.empathy.beans.ImdbItem;
import co.empathy.beans.SearchResponse;
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
	public Serializable searchByQuery(String query) throws IOException {
		// Generate the array with the fields to search - Can be moved to a dedicated class if needed
		var fields = new String[]{ImdbItem.ORIGINAL_TITLE, ImdbItem.TYPE};
		// Retrieve and return  the query result
		var result = engine.searchMultiMatch(query, fields, "imdb");
		return buildResponse(result);
	}

	@Override
	public Serializable searchByTitle(String query) throws IOException {
		// Retrieve and return  the query result
		var result = engine.searchSingleMatch(query, ImdbItem.TITLE, "imdb");
		return buildResponse(result);
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
				.setItems(items);
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
		return new ImdbItem()
				.setId(properties.get(ImdbItem.ID).toString())
				.setTitleType(properties.get(ImdbItem.TYPE).toString())
				.setPrimaryTitle(properties.get(ImdbItem.TITLE).toString())
				.setOriginalTitle(properties.get(ImdbItem.ORIGINAL_TITLE).toString())
				.setIsAdult(properties.get(ImdbItem.IS_ADULT).toString())
				.setStartYear(properties.get(ImdbItem.START).toString())
				.setEndYear(properties.get(ImdbItem.END).toString())
				.setRuntime(properties.get(ImdbItem.RUNTIME_MINUTES).toString())
				.setGenres(genresList.toArray(new String[0]));
	}

}
