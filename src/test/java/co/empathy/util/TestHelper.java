package co.empathy.util;

import co.empathy.common.ImdbItem;
import co.empathy.search.response.SearchResponse;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.inject.Singleton;
import java.util.List;
import java.util.Map;

@Singleton
public class TestHelper {

	/**
	 * Builds the type to parse the IMDb responses
	 * @return	SearchResponse<ImdbItem> TypeReference
	 */
	public TypeReference<SearchResponse<ImdbItem>> getImdbResponseType() {
		return new TypeReference<>() {};
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
