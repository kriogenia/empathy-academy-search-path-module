package co.empathy.search;

import co.empathy.common.ImdbRating;
import co.empathy.exceptions.NoResultException;
import co.empathy.search.request.MyRequest;
import co.empathy.search.response.SearchResult;
import co.empathy.engines.SearchEngine;
import co.empathy.common.ImdbItem;
import co.empathy.search.response.SearchResponse;
import io.micronaut.context.annotation.Prototype;
import io.reactivex.annotations.NonNull;
import org.apache.http.NoHttpResponseException;
import org.apache.lucene.index.IndexNotFoundException;

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
		var result = engine.idSearch(id, INDEX);
		var item = result.getItems().stream().map(this::extendedItemBuilder).findFirst();
		return item.orElse(new ImdbItem());
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
				.setAggregations(result.getAggregations())
				.setSuggestions(result.getSuggestions());
	}

	/**
	 * Builds a basic ImdbItem from the properties map
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
		// Build the item
		return new ImdbItem()
				.setId((String) properties.get(ImdbItem.ID))
				.setTitleType((String) properties.get(ImdbItem.TYPE))
				.setPrimaryTitle((String) properties.get(ImdbItem.TITLE))
				.setStartYear((String) properties.get(ImdbItem.START))
				.setEndYear((String) properties.get(ImdbItem.END))
				.setAverageRating((Double) properties.get(ImdbRating.AVERAGE))
				.setVotes((Integer) properties.get(ImdbRating.VOTES))
				.setGenres(genresList.toArray(new String[0]));
	}

	/**
	 * Builds a complete ImdbItem from the properties map
	 * @param properties	Retrieved properties of the search
	 * @return				Imdb Item with the specified properties
	 */
	private ImdbItem extendedItemBuilder(Map<String, Object> properties) {
		return itemBuilder(properties)
				.setOriginalTitle((String) properties.get(ImdbItem.ORIGINAL_TITLE))
				.setRuntime((String) properties.get(ImdbItem.RUNTIME_MINUTES))
				.setIsAdult((Boolean) properties.get(ImdbItem.IS_ADULT));
	}

}
