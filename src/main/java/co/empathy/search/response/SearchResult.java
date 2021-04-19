package co.empathy.search.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

/**
 * Common POJO class to manage search results
 */
public class SearchResult {

	public final static String TOTAL = "total";
	public final static String ITEMS = "items";
	public final static String AGGREGATIONS = "aggregations";
	public final static String SUGGESTIONS = "suggest";

	private final long total;
	private final List<Map<String, Object>> items;

	@JsonProperty(AGGREGATIONS)
	private Map<String,Map<String, Long>> aggregations;

	@JsonProperty(SUGGESTIONS)
	private List<String> suggestions;

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
	 * @return	total hits of the search
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * @return	list of items retrieved from the search as maps
	 */
	public List<Map<String, Object>> getItems() {
		return items;
	}

	/**
	 * @return  map with all the aggregations of the search
	 */
	public Map<String, Map<String, Long>> getAggregations() {
		return aggregations;
	}

	/**
	 * @return  list of options suggested
	 */
	public List<String> getSuggestions() {
		return suggestions;
	}



	/**
	 * @param aggregations  map of aggregations returned
	 * @return              modified item
	 */
	public SearchResult setAggregations(Map<String, Map<String, Long>> aggregations) {
		this.aggregations = aggregations;
		return this;
	}

	/**
	 * @param suggestions   list of suggested options
	 * @return              modified item
	 */
	public SearchResult setSuggestions(List<String> suggestions) {
		this.suggestions = suggestions;
		return this;
	}
}
