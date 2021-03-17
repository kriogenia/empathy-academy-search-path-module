package co.empathy.search.request;

import co.empathy.common.ImdbItem;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class MovieRequestTest {

	@Test
	public void testMusts() {
		MovieRequest request = new MovieRequest(null, "test", null, null);
		assertTrue(request.musts().containsKey(ImdbItem.ORIGINAL_TITLE));
		assertEquals("test", request.musts().get(ImdbItem.ORIGINAL_TITLE));
	}

	@Test
	public void testFilters() {
		// One genre, no type
		MovieRequest request = new MovieRequest(null, "test", "genre", null);
		var filters = request.filters();
		assertTrue(filters.containsKey(ImdbItem.GENRES));
		assertFalse(filters.containsKey(ImdbItem.TYPE));
		assertArrayEquals(new String[]{"genre"}, filters.get(ImdbItem.GENRES));
		// One genre, one type
		request = new MovieRequest(null, "test", "genre", "movie");
		filters = request.filters();
		assertTrue(filters.containsKey(ImdbItem.GENRES));
		assertTrue(filters.containsKey(ImdbItem.TYPE));
		assertArrayEquals(new String[]{"genre"}, filters.get(ImdbItem.GENRES));
		assertArrayEquals(new String[]{"movie"}, filters.get(ImdbItem.TYPE));
		// Various genres, one type
		request = new MovieRequest(null, "test", "a,b", "movie");
		filters = request.filters();
		assertTrue(filters.containsKey(ImdbItem.GENRES));
		assertTrue(filters.containsKey(ImdbItem.TYPE));
		assertArrayEquals(new String[]{"a","b"}, filters.get(ImdbItem.GENRES));
		assertArrayEquals(new String[]{"movie"}, filters.get(ImdbItem.TYPE));
	}

	@Test
	public void testAggregationBuckets() {
		var request = new MovieRequest(null, "test", null, null);
		var aggs = request.aggregationBuckets();
		assertEquals(ImdbItem.GENRES, aggs.get(MovieRequest.GENRES_AGG));
		assertEquals(ImdbItem.TYPE, aggs.get(MovieRequest.TYPES_AGG));
	}

}
