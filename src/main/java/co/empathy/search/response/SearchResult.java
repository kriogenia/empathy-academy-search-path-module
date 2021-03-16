package co.empathy.search.beans;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Common POJO class to manage search results
 */
public class SearchResult {

	public final static String TOTAL = "total";
	public final static String ITEMS = "items";

	private final long total;
	private final List<Map<String, Object>> items;

	/**
	 * Constructor and JSON creator of the search result object
	 * @param total			Number of hits of the Search
	 * @param items			List of retrieved hits
	 */
	@JsonCreator
	public SearchResult(
			@JsonProperty(TOTAL) long total,
			@JsonProperty(ITEMS) List<Map<String, Object>> items) {
		this.total = total;
		this.items = items;
	}

	/**
	 * Builds a SearchResult pojo from an ElasticSearch response
	 * @param esResponse	SearchResponse from ElasticSearch
	 * @return				SearchResult with the search info
	 */
	public static SearchResult builder(SearchResponse esResponse) {
		SearchHits hits = esResponse.getHits();
		long totalHits = hits.getTotalHits().value;
		var items = Arrays.stream(hits.getHits()).map(SearchResult::flattenHit);
		return new SearchResult(totalHits, items.collect(Collectors.toList()));
	}

	/**
	 * @return	Total hits of the search
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * @return	List of items retrieved from the search as maps
	 */
	public List<Map<String, Object>> getItems() {
		return items;
	}

	/**
	 * Converts a ES SearchHit into flat map
	 * @param hit	ElasticSearch search hit
	 * @return		Flat map with the retrieved attributes
	 */
	private static Map<String, Object> flattenHit(SearchHit hit) {
		var map = hit.getSourceAsMap();
		map.put("id", hit.getId());
		return map;
	}
}
