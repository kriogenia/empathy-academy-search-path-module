package co.empathy.search;

import co.empathy.engines.SearchEngine;
import co.empathy.beans.ImdbItem;
import co.empathy.beans.SearchResponse;
import io.micronaut.context.annotation.Prototype;
import io.reactivex.annotations.NonNull;

import javax.inject.Named;
import java.io.IOException;
import java.io.Serializable;
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
		// Generate a response object*/
		return new SearchResponse<ImdbItem>()
				.setTotal(result.getTotal())
				.setItems(items);
	}

	private ImdbItem itemBuilder(Map<String, Object> properties) {
		return new ImdbItem()
				.setId(properties.get("id").toString())
				.setTitleType(properties.get("titleType").toString())
				.setPrimaryTitle(properties.get("primaryTitle").toString())
				.setOriginalTitle(properties.get("originalTitle").toString())
				.setIsAdult(properties.get("isAdult").toString())
				.setStartYear(properties.get("startYear").toString())
				.setEndYear(properties.get("endYear").toString())
				.setRuntime(properties.get("runtimeMinutes").toString())
				.setGenres(properties.get("genres").toString());
	}

}
