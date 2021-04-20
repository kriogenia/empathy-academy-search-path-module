package co.empathy.search.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class SearchResultTest {

	@Inject
	ObjectMapper mapper;

	private SearchResult result;
	private Map<String, Object> firstResult;
	private Map<String, Object> secondResult;
	private Map<String, Long> genres;
	private Map<String, Long> types;

	@BeforeEach
	public void resetResult() {
		// List
		List<Map<String, Object>> list = new ArrayList<>();
		firstResult = new LinkedHashMap<>();
		firstResult.put("id", "tt0000001");
		firstResult.put("title", "test");
		list.add(firstResult);
		secondResult = new LinkedHashMap<>();
		secondResult.put("id", "tt0000002");
		secondResult.put("title", "test");
		list.add(secondResult);
		// Aggregations
		Map<String, Map<String, Long>> aggs = new HashMap<>();
		types = new LinkedHashMap<>();
		types.put("movie", 1L);
		types.put("short", 1L);
		aggs.put("types", types);
		genres = new LinkedHashMap<>();
		genres.put("comedy", 2L);
		genres.put("action", 1L);
		aggs.put("genres", genres);
		// Suggestions
		List<String> suggs = Arrays.asList("a", "b", "c");
		result = new SearchResult(2, list)
				.setAggregations(aggs).setSuggestions(suggs);
	}

	/**
	 * Tests the automatic conversion of the object to Json
	 * @throws JsonProcessingException	if the parsing fails
	 */
	@Test
	public void toJsonTest() throws JsonProcessingException {
		var json = mapper.writeValueAsString(result);
		var jsonMap = mapper.readValue(json, Map.class);
		// Total
		Integer total = Math.toIntExact(result.getTotal());
		assertEquals(total, jsonMap.get(SearchResult.TOTAL));
		// Items
		assertTrue(jsonMap.get(SearchResult.ITEMS) instanceof List<?>);
		var items = jsonMap.get(SearchResult.ITEMS);
		for (Object o: (List<?>) items) {
			assertTrue(o instanceof Map);
			assertTrue(((Map)o).get("id").toString().contains("tt000000"));
			assertEquals("test", ((Map) o).get("title").toString());
		}
		// Aggregations
		assertTrue(jsonMap.get(SearchResult.AGGREGATIONS) instanceof Map<?,?>);
		Map<?, ?> aggs = (Map<?, ?>) jsonMap.get(SearchResult.AGGREGATIONS);
		assertTrue(aggs.get("types") instanceof Map);
		Map<?, ?> types = (Map<?, ?>) aggs.get("types");
		assertEquals(1, types.get("movie"));
		assertEquals(1, types.get("short"));
		assertTrue(aggs.get("genres") instanceof Map);
		Map<?, ?> genres = (Map<?, ?>) aggs.get("genres");
		assertEquals(2, genres.get("comedy"));
		assertEquals(1, genres.get("action"));
		// Suggestions
		assertTrue(jsonMap.get(SearchResult.SUGGESTIONS) instanceof List<?>);
		List<?> suggs = (List<?>) jsonMap.get(SearchResult.SUGGESTIONS);
		assertEquals("a", suggs.get(0));
		assertEquals("b", suggs.get(1));
		assertEquals("c", suggs.get(2));
	}

	/**
	 * Test the automatic conversion from JSON to object
	 * @throws JsonProcessingException	if the parsing fails
	 */
	@Test
	public void fromJsonTest() throws JsonProcessingException {
		String jsonFirstResult = mapper.writeValueAsString(firstResult);
		String jsonSecondResult = mapper.writeValueAsString(secondResult);
		String jsonTypes = mapper.writeValueAsString(types);
		String jsonGenres = mapper.writeValueAsString(genres);
		String json = "{" + "\"" + SearchResult.TOTAL + "\": 2," +
				"\"" + SearchResult.ITEMS + "\": [" +
				jsonFirstResult + ", " + jsonSecondResult + "]," +
				"\"" + SearchResult.AGGREGATIONS + "\": {" +
				"\"types\": " + jsonTypes + ", " +
				"\"genres\": " + jsonGenres +"}," +
				"\"" + SearchResult.SUGGESTIONS + "\": [" +
				"\"a\"," + "\"b\"," + "\"c\"]" + "}";
		var read = mapper.readValue(json, SearchResult.class);
		// Total
		assertEquals(result.getTotal(), read.getTotal());
		// Items
		assertEquals(result.getItems().size(), read.getItems().size());
		assertEquals(result.getItems(), read.getItems());
		// Aggregations
		assertEquals(1, result.getAggregations().get("types").get("movie"));
		assertEquals(1, result.getAggregations().get("types").get("short"));
		assertEquals(2, result.getAggregations().get("genres").get("comedy"));
		assertEquals(1, result.getAggregations().get("genres").get("action"));
		// Suggestions
		assertEquals(3, result.getSuggestions().size());
		assertArrayEquals(new String[]{"a", "b", "c"}, result.getSuggestions().toArray(new String[0]));
	}

}
