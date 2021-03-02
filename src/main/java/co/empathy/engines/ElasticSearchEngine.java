package co.empathy.engines;

import co.empathy.index.Indexable;
import org.apache.http.HttpHost;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;

import javax.inject.Singleton;
import java.io.IOException;

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
		IndexRequest request = new IndexRequest("imdb").id(entry.getId()).source(entry.getJson());
		IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
		System.out.format("Indexed %s\n", entry.getId());
	}

	@Override
	public String getVersion() throws IOException {
		// ElasticSearch server info
		MainResponse response = esClient.info(RequestOptions.DEFAULT);
		return response.getVersion().getNumber();
	}
}
