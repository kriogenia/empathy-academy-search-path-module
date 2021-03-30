package co.empathy.util;

import co.empathy.common.ImdbItem;
import co.empathy.engines.SearchEngine;
import co.empathy.search.request.MyRequest;

import javax.inject.Singleton;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Singleton
public class ElasticSearchTestHelper {

	public static final String INDEX = "test";

	/**
	 * Common call to perform the single match search operation
	 * @param engine	engine to test
	 * @param request	request to search
	 * @param total		expected total hits
	 * @param size		expected retrieved results
	 * @return			list of results
	 * @throws IOException    if the engine fails
	 */
	public List<Map<String, Object>> performSingleMatch(
			SearchEngine engine, MyRequest request, int total, int size)
			throws IOException {
		var result = engine.scoredSearch(request, INDEX);
		assertEquals(total, result.getTotal());
		var items = result.getItems();
		assertEquals(size, items.size());
		return items;
	}

	/**
	 * Common call to perform the multi match search operation
	 * @param engine	engine to test
	 * @param query		query to call
	 * @param total		expected total hits
	 * @param size		expected retrieved results
	 * @return			list of results
	 * @throws IOException	if the engine fails
	 */
	public List<Map<String, Object>> performCrossSearch(
			SearchEngine engine, String query, int total, int size)
			throws IOException {
		var result = engine.crossSearch(query,
				new String[]{ImdbItem.ORIGINAL_TITLE, ImdbItem.TYPE}, INDEX);
		assertEquals(total, result.getTotal());
		var items = result.getItems();
		assertEquals(size, items.size());
		return items;
	}

}
