package co.empathy.search;

import co.empathy.common.ImdbItem;
import co.empathy.engines.MockSearchEngine;
import co.empathy.search.request.ImdbRequest;
import co.empathy.search.response.SearchResponse;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ImdbSearcherTest {

	@Inject
	MockSearchEngine mockSearchEngine;

	ImdbSearcher searcher;

	@BeforeAll
	public void setMock() {
		searcher = new ImdbSearcher(mockSearchEngine);
	}

	@Test
	public void searchByQueryTest() throws IOException {
		var request = new ImdbRequest(null, "query",
				null, null, null);
		var result = searcher.searchByQuery(request);
		assertTrue(result instanceof SearchResponse);
		SearchResponse<?> response = (SearchResponse<?>) result;
		// Total
		assertEquals(1, response.getTotal());
		// Items
		assertEquals(1, response.getItems().size());
		var item = response.getItems().get(0);
		assertEquals("id", ((ImdbItem)item).getId());
		// Aggs
		var aggs = response.getAggregations().get("aggs");
		for (var key: aggs.keySet()) {
			assertEquals(1, aggs.get(key));
		}
	}

}
