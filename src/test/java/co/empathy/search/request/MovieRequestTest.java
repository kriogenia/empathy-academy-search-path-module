package co.empathy.search.request;

import co.empathy.common.ImdbItem;
import co.empathy.search.request.filters.TermsFilter;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class MovieRequestTest {

	@Test
	public void testMusts() {
		MovieRequest request = new MovieRequest(null, "test",
				null, null, null);
		assertTrue(request.musts().containsKey(ImdbItem.ORIGINAL_TITLE));
		assertEquals("test", request.musts().get(ImdbItem.ORIGINAL_TITLE));
	}

	@Test
	public void testTermsFilters() {
		// One genre, no type
		MovieRequest request = new MovieRequest(null, "test",
				"genre", null, null);
		var filters = request.filters();
		assertEquals(1, filters.size());
		assertArrayEquals(new String[]{"genre"}, ((TermsFilter) filters.get(0)).getTerms());
		// One genre, one type
		request = new MovieRequest(null, "test",
				"genre", "movie", null);
		filters = request.filters();
		assertEquals(2, filters.size());
		assertArrayEquals(new String[]{"genre"}, ((TermsFilter) filters.get(0)).getTerms());
		assertArrayEquals(new String[]{"movie"}, ((TermsFilter) filters.get(1)).getTerms());
		// Various genres, one type
		request = new MovieRequest(null, "test",
				"a,b", "movie", null);
		filters = request.filters();
		assertEquals(2, filters.size());
		assertArrayEquals(new String[]{"a","b"}, ((TermsFilter) filters.get(0)).getTerms());
		assertArrayEquals(new String[]{"movie"}, ((TermsFilter) filters.get(1)).getTerms());
	}

	@Test
	public void testAggregationBuckets() {
		var request = new MovieRequest(null, "test",
				null, null, null);
		var aggs = request.aggregationBuckets();
		assertEquals(ImdbItem.GENRES, aggs.get(MovieRequest.GENRES_AGG));
		assertEquals(ImdbItem.TYPE, aggs.get(MovieRequest.TYPES_AGG));
	}

}
