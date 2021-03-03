package co.empathy.engines;

import co.empathy.beans.ImdbItem;
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
		System.out.format("Indexed %s\n", entry.getId());
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
	public SearchResult searchByTitle(String title, String... indices) throws IOException {
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices(indices);
		// Build the match title query
		SearchSourceBuilder builder = new SearchSourceBuilder();
		builder.query(QueryBuilders.matchQuery(ImdbItem.ORIGINAL_TITLE, title));
		searchRequest.source(builder);
		// Invokes the search
		SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
		return SearchResult.builder(response);
	}

	@Override
	public String getVersion() throws IOException {
		// ElasticSearch server info
		MainResponse response = esClient.info(RequestOptions.DEFAULT);
		return response.getVersion().getNumber();
	}
}
