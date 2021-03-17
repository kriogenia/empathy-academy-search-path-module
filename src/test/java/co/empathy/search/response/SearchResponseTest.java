package co.empathy.search.response;

import co.empathy.common.ImdbItem;
import co.empathy.search.response.SearchResponse;
import co.empathy.util.TestHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class SearchResponseTest {

	@Inject
	ObjectMapper mapper;

	@Inject
	TestHelper helper;

	private SearchResponse<Serializable> response;

	@BeforeEach
	public void resetResponse() {
		List<Serializable> list = new ArrayList<>();
		list.add(ImdbItem.buildFromString("tt0000001\tshort\tCarmencita\tCarmencita\t0\t1894\t\\N\t0\tDocumentary,Short"));
		list.add(ImdbItem.buildFromString("tt0000002\tmovie\tThe Avengers\tThe Avengers\t0\t2014\t\\N\t143\tAction"));
		response = new SearchResponse<>().setTotal(2).setItems(list);
	}

	/**
	 * Tests the automatic conversion of the object to JSON
	 * @throws JsonProcessingException	if the parsing fails
	 */
	@Test
	public void toJsonTest() throws JsonProcessingException {
		var json = mapper.writeValueAsString(response);
		var jsonMap = mapper.readValue(json, Map.class);
		// Total
		Integer total = Math.toIntExact(response.getTotal());
		assertEquals(total, jsonMap.get(SearchResponse.TOTAL));
		// Items
		assertTrue(jsonMap.get(SearchResponse.ITEMS) instanceof List<?>);
		var items = jsonMap.get(SearchResponse.ITEMS);
		for (Object o: (List<?>) items) {
			assertTrue(o instanceof Map);
			var id = ((Map)o).get(ImdbItem.ID).toString();
			assertTrue(id.contains("tt000000"));
		}
	}

	/**
	 * Test the automatic conversion from JSON to object
	 * @throws JsonProcessingException	if the parsing fails
	 */
	@Test
	public void fromJsonTest() throws JsonProcessingException {
		var item1 = ImdbItem.buildFromString("tt0000001\tshort\tCarmencita\tCarmencita\t0\t1894\t\\N\t0\tDocumentary,Short");
		var json1 = mapper.writeValueAsString(item1);
		var item2 = ImdbItem.buildFromString("tt0000002\tmovie\tThe Avengers\tThe Avengers\t0\t2014\t\\N\t143\tAction");
		var json2 = mapper.writeValueAsString(item2);
		String json = "{" + "\"" + SearchResponse.TOTAL + "\": 2," +
				"\"" + SearchResponse.ITEMS + "\": [" +
				json1 + ", " + json2 + "]}";
		var read = mapper.readValue(json, helper.getImdbResponseType());
		// Total
		assertEquals(response.getTotal(), read.getTotal());
		// Items
		assertEquals(response.getItems().size(), read.getItems().size());
		Assertions.assertEquals("tt0000001",  read.getItems().get(0).getId());
		Assertions.assertEquals("tt0000002",  read.getItems().get(1).getId());

	}

}
