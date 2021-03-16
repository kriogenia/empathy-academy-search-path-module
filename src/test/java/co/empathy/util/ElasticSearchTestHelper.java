package co.empathy.util;

import co.empathy.common.ImdbItem;
import co.empathy.engines.SearchEngine;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ElasticSearchTestHelper {

	public static final String INDEX = "test";

	/**
	 * Common call to perform the single match search operation
	 * @param engine	engine to test
	 * @param query		query to call
	 * @param total		expected total hits
	 * @param size		expected retrieved results
	 * @return			list of results
	 * @throws IOException    if the engine fails
	 */
	public static List<Map<String, Object>> performSingleMatch(
			SearchEngine engine, String query, int total, int size)
			throws IOException {
		var result = engine.searchSingleMatch(query, ImdbItem.ORIGINAL_TITLE, INDEX);
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
	public static List<Map<String, Object>> performMultiMatch(
			SearchEngine engine, String query, int total, int size)
			throws IOException {
		var result = engine.crossSearch(query,
				new String[]{ImdbItem.ORIGINAL_TITLE, ImdbItem.TYPE}, INDEX);
		assertEquals(total, result.getTotal());
		var items = result.getItems();
		assertEquals(size, items.size());
		return items;
	}

	/**
	 * Compares all items on a list to the specified
	 * @param property	property to check
	 * @param expected	expected result
	 * @param items		results to compare
	 * @return		true if all the items titles contains the expected string
	 */
	public static boolean allContains(String property, String expected, List<Map<String, Object>> items) {
		return items.stream().map(x -> x.get(property).toString())
				.allMatch(x -> x.contains(expected));
	}

	/**
	 * Compares all items on a list to the specified
	 * @param property	property to check
	 * @param expected	expected result
	 * @param items		results to compare
	 * @return		true if any of the items titles contains the expected string
	 */
	public static boolean anyContains(String property, String expected, List<Map<String, Object>> items) {
		return items.stream().map(x -> x.get(property).toString())
				.anyMatch(x -> x.contains(expected));
	}
}
