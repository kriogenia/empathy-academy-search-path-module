package co.empathy.engines;

import co.empathy.common.ImdbItem;
import co.empathy.search.request.MockMyRequest;
import co.empathy.search.request.MovieRequest;
import co.empathy.util.ElasticSearchTestHelper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static co.empathy.util.TestHelper.*;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ElasticSearchEngineTest {

	public static final String CURRENT_VERSION = "7.11.1";

	@Inject
	ElasticSearchEngine engine;

	@Inject
	ElasticSearchTestHelper helper;

	@Inject
	MockMyRequest request;

	// Maps of the request
	Map<String, String> must = new HashMap<>();
	Map<String, String[]> filter = new HashMap<>();
	Map<String, String> aggs = new HashMap<>();

	//TODO test close
	//TODO test index
	//TODO test bulk index

	/**
	 * Specifies the maps to use on the mocks
	 */
	@BeforeAll
	public void createRequest() {
		request.mockMusts(must);
		request.mockFilters(filter);
		request.mockAggregationBuckets(aggs);
	}

	/**
	 * Clears all the maps between tests
	 */
	@BeforeEach
	public void clearMaps() {
		must.clear();
		filter.clear();
		aggs.clear();
	}

	/**
	 *	Test the correct return of the search results
	 *	and the implementation of the single match.
	 *	Test conditions are the number of results, the filter and the aggregations.
	 * @throws IOException	produced in the tested function
	 */
	@Test
	public void searchSingleMatchTest() throws IOException {
		// One result query, no filter, no aggregations
		must.put(ImdbItem.ORIGINAL_TITLE, "Carmencita");
		var items = helper.performSingleMatch(engine, request, 1, 1);
		var item = items.get(0);
		assertEquals("tt0000001", item.get("id"));
		assertEquals("Carmencita", item.get("title"));
		assertEquals("short", item.get("type"));
		assertEquals("1894", item.get("start_year"));
		assertNull(item.get("end_year"));

		// More than one result, no filter, no aggregations
		must.clear();
		must.put(ImdbItem.ORIGINAL_TITLE, "the");
		items = helper.performSingleMatch(engine, request, 4, 4);
		assertTrue(items.stream().map(x -> x.get("title").toString()).allMatch(
				x -> x.matches(".*[Tt]he.*")));

		// More than one result, one filter, no aggregations
		filter.put(ImdbItem.TYPE, new String[]{"movie"});
		items = helper.performSingleMatch(engine, request, 3, 3);
		assertTrue(allContains("type", "movie", items));

		// More than one result, one filter, one aggregation
		aggs.put(MovieRequest.GENRES_AGG, ImdbItem.GENRES);
		var result = engine.searchSingleMatch(request, ElasticSearchTestHelper.INDEX);
		assertNotNull(result.getAggregations().get(MovieRequest.GENRES_AGG));
		var genres = result.getAggregations().get(MovieRequest.GENRES_AGG);
		assertEquals(2, genres.get("adventure"));
		assertEquals(1, genres.get("fantasy"));
		assertEquals(3, genres.get("action"));
		assertEquals(1, genres.get("family"));
	}

	/**
	 * Test the cross search (whose functionality is worse since the index changes)
	 * @throws IOException	produced in the tested function
	 */
	@Test
	public void crossSearchTest() throws IOException {
		// Less than 10 results
		var items = helper.performCrossSearch(engine, "the tvEpisode", 4, 4);
		for (var item: items) {
			String title = item.get("title").toString();
			String type = item.get("type").toString();
			assertTrue(title.matches(".*[Tt]he.*")
					|| title.matches(".*[Tt][Vv].*") || type.equals("tvEpisode"));
		}
		// More then 10 results
		items = helper.performCrossSearch(engine, "the movie", 4, 4);
		for (var item: items) {
			String title = item.get("title").toString();
			String type = item.get("type").toString();
			assertTrue(title.matches(".*[Tt]he.*")
					|| title.matches(".*[Mm]ovie") || type.equals("movie"));
		}
	}

	/**
	 * Tests the version retrieval of ES
	 * @throws IOException	produced in the tested function
	 */
	@Test
	public void getVersionTest() throws IOException {
		var result = engine.getVersion();
		assertEquals(CURRENT_VERSION, result);
	}

	/**
	 * Tests the index check of ES
	 * @throws IOException produced in the tested function
	 */
	@Test
	public void hasIndexTest() throws IOException {
		assertTrue(engine.hasIndex("test"));
		assertFalse(engine.hasIndex("this_index_does_not_exist"));
	}

	//TODO test createIndex

	//TODO test deleteIndex

}
