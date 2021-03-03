package co.empathy.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class SearchResponse<T extends Serializable> implements Serializable {

	@JsonProperty("total")
	private long total;
	@JsonProperty("items")
	private List<T> items;

	public SearchResponse() {}

	public SearchResponse<T> setTotal(long total) {
		this.total = total;
		return this;
	}

	public SearchResponse<T> setItems(List<T> items) {
		this.items = items;
		return this;
	}

	public long getTotal() {
		return total;
	}

	public List<T> getItems() {
		return items;
	}
}
