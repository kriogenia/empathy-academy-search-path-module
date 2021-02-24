package com.example.pojos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResult {

	private final String query;
	private final String cluster_name;

	@JsonCreator
	public SearchResult(
			@JsonProperty("query") String query,
			@JsonProperty("cluster_name") String cluster_name) {
		this.query = query;
		this.cluster_name = cluster_name;
	}

	public String getQuery() {
		return query;
	}

	public String getCluster_name() {
		return cluster_name;
	}

}
