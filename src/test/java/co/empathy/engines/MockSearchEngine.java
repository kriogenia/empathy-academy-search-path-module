package co.empathy.engines;

import co.empathy.search.response.SearchResult;
import co.empathy.index.Indexable;
import co.empathy.index.configuration.IndexConfiguration;
import io.micronaut.context.annotation.Replaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Replaces(SearchEngine.class)
@Singleton
public class MockSearchEngine implements SearchEngine {

	// TODO finish this mock

	private final Logger LOG = LoggerFactory.getLogger(MockSearchEngine.class);

	@Override
	public void index(String index, Indexable entry) throws IOException {
		LOG.info("Indexed {} in {}", entry.getId(), index);
	}

	@Override
	public void bulkIndex(String index, List<Indexable> entries) throws IOException {
		LOG.info("Bulk index called to index {}", index);
	}

	@Override
	public SearchResult searchSingleMatch(String query, String field, String... indices) throws IOException {
		SearchResult result = generateSearchResult();
		return null;
	}

	@Override
	public SearchResult crossSearch(String query, String[] fields, String... indices) throws IOException {
		return null;
	}

	@Override
	public String getVersion() throws IOException {
		return null;
	}

	@Override
	public boolean hasIndex(String key) throws IOException {
		return false;
	}

	@Override
	public void createIndex(IndexConfiguration configuration) throws IOException {

	}

	@Override
	public void deleteIndex(String key) throws IOException {

	}

	@Override
	public void close() throws Exception {
		LOG.info("Closed MockSearchEngine connection");
	}

	/**
	 * @return  test SearchResult
	 */
	private SearchResult generateSearchResult() {
		Map<String, Object> map = new LinkedHashMap<>();
		map.put("key", "pair");
		List<Map<String, Object>> list = new ArrayList<>();
		list.add(map);
		// TODO change mock
		return new SearchResult(1L, list, null);
	}
}
