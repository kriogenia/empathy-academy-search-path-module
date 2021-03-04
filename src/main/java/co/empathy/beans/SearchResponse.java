package co.empathy.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class SearchResponse<T extends Serializable> implements Serializable {

	@JsonProperty("total")
	private long total;
	@JsonProperty("items")
	private List<T> items;

	/**
	 * Empty constructor of the JavaBean
	 */
	public SearchResponse() {}

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
}