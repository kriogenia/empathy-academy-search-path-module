package co.empathy.engines;

import co.empathy.common.ImdbItem;
import co.empathy.index.Indexable;
import co.empathy.index.configuration.IndexConfiguration;
import co.empathy.search.request.MyRequest;
import co.empathy.search.response.SearchResult;
import io.micronaut.context.annotation.Replaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.*;

@Singleton
public class MockSearchEngine implements SearchEngine {

	private static final Logger LOG = LoggerFactory.getLogger(MockSearchEngine.class);

	public List<String> index = new ArrayList<>();

	@Override
	public void index(String index, Indexable entry) throws IOException {
		LOG.info("Mock called to index " + entry + " on " + index);
	}

	@Override
	public void bulkIndex(String index, List<Indexable> entries) throws IOException {
		LOG.info("Mock called to bulk index " + entries.size() + " entries on " + index);
	}

	@Override
	public void bulkUpdate(String index, List<Indexable> entries) throws IOException {
		LOG.info("Mock called to bulk update " + entries.size() + " entries on " + index);
	}

	@Override
	public SearchResult scoredSearch(MyRequest request, String... indices) throws IOException {
		List<Map<String, Object>> items = new ArrayList<>();
		if (Arrays.toString(indices).contains("imdb")) {
			items.add(mockImdbItem());
		}
		Map<String,Map<String, Long>> aggs = new HashMap<>();
		Map<String, Long> bucket = new HashMap<>();
		request.aggregations().forEach(agg -> bucket.put(agg.getName(), 1L));
		aggs.put("aggs", bucket);
		return new SearchResult(1, items, aggs);
	}

	@Override
	public SearchResult crossSearch(String query, String[] fields, String... indices) throws IOException {
		return null;
	}

	@Override
	public String getVersion() throws IOException {
		return "current version";
	}

	@Override
	public boolean hasIndex(String key) throws IOException {
		return index.contains(key);
	}

	@Override
	public void createIndex(IndexConfiguration configuration) throws IOException {
		LOG.info("Mock called to create index " + configuration.getKey());
		index.add(configuration.getKey());
	}

	@Override
	public void deleteIndex(String key) throws IOException {
		LOG.info("Mock called to delete index " + key);
		index.remove(key);
	}

	@Override
	public void close() throws Exception {
		LOG.info("MockSearchEngine close call invoked");
	}

	/**
	 * @return mock of a retrieved ImdbItem for testing
	 */
	private Map<String, Object> mockImdbItem() {
		Map<String, Object> item = new HashMap<>();
		item.put(ImdbItem.ID, "id");
		item.put(ImdbItem.TYPE, "type");
		item.put(ImdbItem.TITLE, "title");
		item.put(ImdbItem.ORIGINAL_TITLE, "original");
		item.put(ImdbItem.IS_ADULT, "0");
		item.put(ImdbItem.START, "1900");
		item.put(ImdbItem.RUNTIME_MINUTES, "100");
		item.put(ImdbItem.GENRES, new ArrayList<>(Arrays.asList("genre1", "genre2")));
		return item;
	}

}
