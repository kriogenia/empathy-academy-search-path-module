package co.empathy.search;

import co.empathy.engines.SearchEngine;
import co.empathy.beans.ImdbItem;
import co.empathy.beans.SearchResponse;
import io.micronaut.context.annotation.Prototype;
import io.reactivex.annotations.NonNull;

import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
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
	public Serializable searchByTitle(String query) throws IOException {
		// Retrieve the query result
		var result = engine.searchByTitle(query, "imdb");
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
		return new ImdbItem()
				.setId(properties.get(ImdbItem.ID).toString())
				.setTitleType(properties.get(ImdbItem.TYPE).toString())
				.setPrimaryTitle(properties.get(ImdbItem.TITLE).toString())
				.setOriginalTitle(properties.get(ImdbItem.ORIGINAL_TITLE).toString())
				.setIsAdult(properties.get(ImdbItem.IS_ADULT).toString())
				.setStartYear(properties.get(ImdbItem.START).toString())
				.setEndYear(properties.get(ImdbItem.END).toString())
				.setRuntime(properties.get(ImdbItem.RUNTIME_MINUTES).toString())
				.setGenres(properties.get(ImdbItem.GENRES));
	}

}
