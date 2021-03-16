package co.empathy.common;

import co.empathy.search.response.SearchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.apache.lucene.search.TotalHits;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@MicronautTest
public class SearchResultTest {

	@Inject
	ObjectMapper mapper;

	private SearchResult result;
	private Map<String, Object> map1;
	private Map<String, Object> map2;

	@BeforeEach
	public void resetResult() {
		List<Map<String, Object>> list = new ArrayList<>();
		map1 = new LinkedHashMap<>();
		map2 = new LinkedHashMap<>();
		map1.put("id", "tt0000001");
		map1.put("title", "test");
		list.add(map1);
		map2.put("id", "tt0000002");
		map2.put("title", "test");
		list.add(map2);
		result = new SearchResult(2, list, null);
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
	}

	/**
	 * Test the automatic conversion from JSON to object
	 * @throws JsonProcessingException	if the parsing fails
	 */
	@Test
	public void fromJsonTest() throws JsonProcessingException {
		String json1 = mapper.writeValueAsString(map1);
		String json2 = mapper.writeValueAsString(map2);
		String json = "{" + "\"" + SearchResult.TOTAL + "\": 2," +
				"\"" + SearchResult.ITEMS + "\": [" +
				json1 + ", " + json2 + "]}";
		var read = mapper.readValue(json, SearchResult.class);
		// Total
		assertEquals(result.getTotal(), read.getTotal());
		// Items
		assertEquals(result.getItems().size(), read.getItems().size());
		assertEquals(result.getItems(), read.getItems());
	}

	/**
	 * Tests the SearchResult builder
	 */
	@Test
	public void builderTest() {
		long value = 2;
		TotalHits total = new TotalHits(value, TotalHits.Relation.EQUAL_TO);
		SearchHit hit = mock(SearchHit.class);
		when(hit.getId()).thenReturn("tt000000");
		when(hit.getSourceAsMap()).thenReturn(new HashMap<>());
		SearchHit[] hits = {hit};
		SearchHits searchHits = mock(SearchHits.class);
		when(searchHits.getTotalHits()).thenReturn(total);
		when(searchHits.getHits()).thenReturn(hits);
		SearchResponse response = mock(SearchResponse.class);
		when(response.getHits()).thenReturn(searchHits);

		var built = SearchResult.builder(response);
		assertEquals(value, built.getTotal());
		var items = built.getItems();
		assertEquals("tt000000", items.get(0).get("id"));
	}

}
