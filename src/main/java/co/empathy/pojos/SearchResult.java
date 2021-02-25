package co.empathy.pojos;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Common POJO class to manage search results
 */
public class SearchResult {

	private final String query;
	private final String cluster_name;

	/**
	 * Constructor and JSON creator of the search result object
	 * @param query	Query of the search
	 * @param cluster_name	Version or name of the cluster
	 */
	@JsonCreator
	public SearchResult(
			@JsonProperty("query") String query,
			@JsonProperty("cluster_name") String cluster_name) {
		this.query = query;
		this.cluster_name = cluster_name;
	}

	/**
	 * @return	The query of the search
	 */
	public String getQuery() {
		return query;
	}

	/**
	 * @return	The version of the cluster used for the search
	 */
	public String getCluster_name() {
		return cluster_name;
	}

}
