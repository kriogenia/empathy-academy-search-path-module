package co.empathy.engines;

import co.empathy.index.Indexable;
import co.empathy.beans.SearchResult;
import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;

/**
 * Search Engine adapter of Elastic Search
 */
@Singleton
public class ElasticSearchEngine implements SearchEngine {

	private final RestHighLevelClient esClient;

	/**
	 * Default constructor loading Elastic Search client
	 */
	public ElasticSearchEngine() {
		// ElasticSearch client
		esClient = new RestHighLevelClient(
				RestClient.builder(
						new HttpHost("localhost", 9200, "http"),
						new HttpHost("localhost", 9201, "http")
				)
		);
	}

	@Override
	public void close() throws Exception {
		esClient.close();
	}

	@Override
	public void index(String index, Indexable entry) throws IOException {
		IndexRequest request = new IndexRequest(index).id(entry.getId()).source(entry.toJsonMap());
		IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
		System.out.format("Indexed %s (%s)\n", entry.getId(), response.getId());
	}

	@Override
	public void bulkIndex(String index, List<Indexable> entries) throws IOException {
		BulkRequest bulk = new BulkRequest();
		// Creates the index requests
		var requests = entries.stream().map(
				x -> new IndexRequest(index).id(x.getId()).source(x.toJsonMap()));
		// Adds them to the bulk request
		requests.forEach(bulk::add);
		// Index the bulk request
		esClient.bulk(bulk, RequestOptions.DEFAULT);
	}

	@Override
	public SearchResult searchSingleMatch(String query, String field, String... indices) throws IOException {
		SearchSourceBuilder builder = new SearchSourceBuilder();
		builder.query(QueryBuilders.matchQuery(field, query));
		return launchSearch(builder, indices);
	}

	@Override
	public SearchResult searchMultiMatch(String query, String[] fields, String... indices) throws IOException {
		// Build the multi match query
		SearchSourceBuilder builder = new SearchSourceBuilder();
		builder.query(QueryBuilders.multiMatchQuery(query, fields));
		return launchSearch(builder, indices);
	}

	@Override
	public String getVersion() throws IOException {
		// ElasticSearch server info
		MainResponse response = esClient.info(RequestOptions.DEFAULT);
		return response.getVersion().getNumber();
	}

	/**
	 * Create and send the search request
	 * @param builder		built query
	 * @param indices		indices to search
	 * @return				search result
	 * @throws IOException	if an error with the search engine has occurred
	 */
	private SearchResult launchSearch(SearchSourceBuilder builder, String... indices) throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices(indices);
		searchRequest.source(builder);
		// Invokes the search
		SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
		return SearchResult.builder(response);
	}
}
