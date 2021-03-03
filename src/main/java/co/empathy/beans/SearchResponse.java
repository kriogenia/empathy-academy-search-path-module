package co.empathy.beans;

import java.io.Serializable;
import java.util.List;

public class SearchResponse<T extends Serializable> implements Serializable {

	private long total;
	private List<T> items;

	public SearchResponse() {}

	public void setTotal(long total) {
		this.total = total;
	}

	public void setItems(List<T> items) {
		this.items = items;
	}

	public long getTotal() {
		return total;
	}

	public List<T> getItems() {
		return items;
	}
}
