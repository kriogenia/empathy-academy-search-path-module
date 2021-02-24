package com.example.search;

import com.example.pojos.SearchResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;

import javax.inject.Singleton;
import java.io.IOException;

@Singleton
public class SearcherImpl implements Searcher {

	private final RestHighLevelClient esClient;

	public SearcherImpl() {
		// ElasticSearch client
		esClient = new RestHighLevelClient(
				RestClient.builder(
						new HttpHost("localhost", 9200, "http"),
						new HttpHost("localhost", 9201, "http")
				)
		);
	}

	@Override
	public String search(String query) throws IOException {
		// ElasticSearch server info
		MainResponse response = esClient.info(RequestOptions.DEFAULT);
		String clusterVersion = response.getVersion().getNumber();
		// Query result
		return new ObjectMapper().writeValueAsString(
					new SearchResult(query, clusterVersion)
			);
	}
}
