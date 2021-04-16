package co.empathy.search.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SearchResponse<T extends Serializable> implements Serializable {

	public static final String TOTAL = "total";
	public static final String ITEMS = "items";
	public static final String AGGREGATIONS = "aggregations";
	public static final String SUGGESTIONS = "suggestions";

	@JsonProperty(TOTAL)
	private long total;

	@JsonProperty(ITEMS)
	private List<T> items;

	@JsonProperty(AGGREGATIONS)
	private Map<String, Map<String, Long>> aggregations;

	@JsonProperty(SUGGESTIONS)
	private List<String> suggestions;

	/**
	 * Empty constructor of the JavaBean
	 */
	public SearchResponse() {
		this.aggregations = new LinkedHashMap<>();
		this.suggestions = new ArrayList<>();
	}

	/**
	 * @param total		total hits of the search
	 * @return			modified item
	 */
	public SearchResponse<T> setTotal(long total) {
		this.total = total;
		return this;
	}

	/**
	 * @param items		items retrieved in the search
	 * @return			modified item
	 */
	public SearchResponse<T> setItems(List<T> items) {
		this.items = items;
		return this;
	}

	/**
	 * @param aggregations  aggregations map of the search
	 * @return              modified item
	 */
	public SearchResponse<T> setAggregations(Map<String, Map<String, Long>> aggregations) {
		this.aggregations = aggregations;
		return this;
	}

	/**
	 * @param suggestions   options suggested in the search
	 * @return              modified item
	 */
	public SearchResponse<T> setSuggestions(List<String> suggestions) {
		this.suggestions = suggestions;
		return this;
	}


	/**
	 * @return	total hits of the search
	 */
	public long getTotal() {
		return total;
	}

	/**
	 * @return	items retrieved in the search
	 */
	public List<T> getItems() {
		return items;
	}

	/**
	 * @return  options from the suggestions
	 */
	public List<String> getSuggestions() {
		return suggestions;
	}

	/**
	 * @return  aggregations retrieved in the search
	 */
	public Map<String, Map<String, Long>> getAggregations() {
		return aggregations;
	}
}
