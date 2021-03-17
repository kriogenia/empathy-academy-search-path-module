package co.empathy.search.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

/**
 * Common POJO class to manage search results
 */
public class SearchResult {

	public final static String TOTAL = "total";
	public final static String ITEMS = "items";
	public final static String AGGREGATIONS = "aggregations";

	private final long total;
	private final List<Map<String, Object>> items;
	private final Map<String,Map<String, Long>> aggregations;

	/**
	 * Constructor and JSON creator of the search result object
	 * @param total			Number of hits of the Search
	 * @param items			List of retrieved hits
	 */
	@JsonCreator
	public SearchResult(
			@JsonProperty(TOTAL) long total,
			@JsonProperty(ITEMS) List<Map<String, Object>> items,
			@JsonProperty(AGGREGATIONS)  Map<String,Map<String, Long>> aggregations) {
		this.total = total;
		this.items = items;
		this.aggregations = aggregations;
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
	 * @return  Map with all the aggregations of the search
	 */
	public Map<String, Map<String, Long>> getAggregations() {
		return aggregations;
	}

}
