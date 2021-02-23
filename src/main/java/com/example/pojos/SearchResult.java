package com.example.pojos;

public class SearchResult {

	private String query;
	private String cluster_name;

	public SearchResult() {}

	public SearchResult(String query, String cluster_name) {
		this.query = query;
		this.cluster_name = cluster_name;
	}

	public String getQuery() {
		return query;
	}

	public void setQuery(String query) {
		this.query = query;
	}

	public String getCluster_name() {
		return cluster_name;
	}

	public void setCluster_name(String cluster_name) {
		this.cluster_name = cluster_name;
	}

}
