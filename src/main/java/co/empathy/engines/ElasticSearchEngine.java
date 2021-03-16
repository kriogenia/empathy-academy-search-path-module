package co.empathy.engines;

import co.empathy.search.request.MyRequest;
import co.empathy.search.response.SearchResult;
import co.empathy.index.Indexable;
import co.empathy.index.configuration.IndexConfiguration;
import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;

/**
 * Search Engine adapter of Elastic Search
 */
@Singleton
public class ElasticSearchEngine implements SearchEngine {

	private static final Logger LOG = LoggerFactory.getLogger(ElasticSearchEngine.class);

	private final RestHighLevelClient esClient;
	private final EEngine info = EEngine.ELASTIC_SEARCH;

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
		// Close client when the engine is destroyed
		LOG.info("Closing ElasticSearch client");
		esClient.close();
	}

	@Override
	public void index(String index, Indexable entry) throws IOException {
		// Index a single entry into the specified index
		IndexRequest request = new IndexRequest(index).id(entry.getId()).source(entry.toJsonMap());
		IndexResponse response = esClient.index(request, RequestOptions.DEFAULT);
		LOG.info("Indexed {} ({})", entry.getId(), response.getId());
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
	public SearchResult searchMultiMatch(MyRequest request, String... indices) throws IOException {
		// Build and add the boolean query with all the specified pairs
		SearchSourceBuilder builder = new SearchSourceBuilder();
		var queryBuilder = QueryBuilders.boolQuery();
		// Musts
		request.musts().forEach((field, text) -> queryBuilder.must(QueryBuilders.matchQuery(field, text)));
		// Filters (as of right now it only accept term filters so I'll leave it like this
		request.filters().forEach((field, text) -> queryBuilder.filter(QueryBuilders.termsQuery(field, text)));
		// Build and add the the aggregations
		request.aggregationBuckets().forEach((name, type) -> builder.aggregation(
				AggregationBuilders.terms(name).field(type)
		));
		// Build and launch the request
		builder.query(queryBuilder);
		return launchSearch(builder, indices);
	}

	@Override
	public SearchResult crossSearch(String query, String[] fields, String... indices) throws IOException {
		// Build the multi match query
		var multiBuilder = new MultiMatchQueryBuilder(query, fields);
		multiBuilder.type(MultiMatchQueryBuilder.Type.CROSS_FIELDS);
		multiBuilder.operator(Operator.OR);
		multiBuilder.tieBreaker(1.0F);
		// Build and launch the search
		SearchSourceBuilder builder = new SearchSourceBuilder();
		builder.query(multiBuilder);
		return launchSearch(builder, indices);
	}

	@Override
	public String getVersion() throws IOException {
		// ElasticSearch server info request
		MainResponse response = esClient.info(RequestOptions.DEFAULT);
		return response.getVersion().getNumber();
	}

	@Override
	public boolean hasIndex(String key) throws IOException {
		// Request and existence retrieval
		GetIndexRequest request = new GetIndexRequest(key);
		return esClient.indices().exists(request, RequestOptions.DEFAULT);
	}

	@Override
	public void createIndex(IndexConfiguration configuration) throws IOException {
		CreateIndexRequest create = new CreateIndexRequest(configuration.getKey());
		String mapping = configuration.getSource(info);
		create.source(mapping, XContentType.JSON);
		esClient.indices().create(create, RequestOptions.DEFAULT);
		LOG.info("New index {} created", configuration.getKey());
	}

	@Override
	public void deleteIndex(String key) throws IOException {
		DeleteIndexRequest request = new DeleteIndexRequest(key);
		esClient.indices().delete(request, RequestOptions.DEFAULT);
		LOG.info("Index {} deleted", key);
	}

	/**
	 * Create and send the search request
	 * @param builder		built query
	 * @param indices		indices to search
	 * @return				search result
	 * @throws IOException	if an error with the search engine has occurred
	 */
	private SearchResult launchSearch(SearchSourceBuilder builder, String... indices) throws IOException {
		// Creates the request
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices(indices);
		searchRequest.source(builder);
		// Invokes the search
		SearchResponse response = esClient.search(searchRequest, RequestOptions.DEFAULT);
		return SearchResult.builder(response);
	}

}
