package co.empathy.engines;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.io.IOException;

import static co.empathy.util.ElasticSearchTestHelper.performMultiMatch;
import static co.empathy.util.ElasticSearchTestHelper.performSingleMatch;
import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class ElasticSearchEngineTest {

	public static final String CURRENT_VERSION = "7.11.1";

	@Inject
	ElasticSearchEngine engine;

	//TODO test index
	//TODO test bulk index

	/**
	 *	Test the correct return of the search results
	 *	and the implementation of the single match.
	 *	Test with a single and more than one result.
	 * @throws IOException	produced in the tested function
	 */
	@Test
	public void searchSingleMatchTest() throws IOException {
		// One result
		var items = performSingleMatch(engine, "Carmencita", 1, 1);
		var item = items.get(0);
		assertEquals("tt0000001", item.get("id"));
		assertEquals("Carmencita", item.get("title"));
		assertEquals("short", item.get("type"));
		assertEquals("1894", item.get("start_year"));
		assertEquals("\\N", item.get("end_year"));

		// More than one result
		items = performSingleMatch(engine, "the", 4, 4);
		assertTrue(items.stream().map(x -> x.get("title").toString()).allMatch(
				x -> x.matches(".*[Tt]he.*")));
	}

	/**
	 * Test the multi match query with less than 10 results and
	 * more than 10 hits
	 * @throws IOException	produced in the tested function
	 */
	@Test
	public void searchMultiMatchTest() throws IOException {
		// Less than 10 results
		var items = performMultiMatch(engine, "the tvEpisode", 6, 6);
		for (var item: items) {
			String title = item.get("title").toString();
			String type = item.get("type").toString();
			assertTrue(title.matches(".*[Tt]he.*")
					|| title.matches(".*[Tt][Vv].*") || type.equals("tvEpisode"));
		}
		// More then 10 results
		items = performMultiMatch(engine, "the movie", 11, 10);
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
