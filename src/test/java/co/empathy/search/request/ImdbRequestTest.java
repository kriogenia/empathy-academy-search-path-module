package co.empathy.search.request;

import co.empathy.common.ImdbItem;
import co.empathy.common.ImdbRating;
import co.empathy.search.request.aggregations.DividedRangeAggregation;
import co.empathy.search.request.aggregations.TermsAggregation;
import co.empathy.search.request.filters.DateRangesFilter;
import co.empathy.search.request.filters.TermsFilter;
import co.empathy.search.request.functions.FieldValueFunction;
import co.empathy.search.request.functions.GaussDecayFunction;
import co.empathy.search.request.functions.TermWeightingFunction;
import co.empathy.search.request.queries.DisjunctionMaxQuery;
import co.empathy.search.request.queries.PartialPlusPerfectQuery;
import co.empathy.search.request.suggestions.TermsSuggestion;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class ImdbRequestTest {

	/**
	 * Tests the correct generation of the musts with and without query
	 */
	@Test
	public void testMusts() {
		// With query
		ImdbRequest request = new ImdbRequest(null, "test",
				null, null, null);
		var musts = request.musts();
		assertEquals(1, musts.size());
		assertTrue(musts.get(0) instanceof DisjunctionMaxQuery);
		var queries = ((DisjunctionMaxQuery) musts.get(0)).getQueries();
		for (var query : queries) {
			assertTrue(query instanceof PartialPlusPerfectQuery);
			assertEquals("test", ((PartialPlusPerfectQuery) query).getText());
		}
		assertEquals(ImdbItem.ORIGINAL_TITLE, ((PartialPlusPerfectQuery) queries.get(0)).getField());
		assertEquals(ImdbItem.TITLE, ((PartialPlusPerfectQuery) queries.get(1)).getField());
		// Without query
		request = new ImdbRequest(null, null, null, null, null);
		musts = request.musts();
		assertEquals(0, musts.size());
	}

	/**
	 * Tests the correct generation of terms filters
	 */
	@Test
	public void testTermsFilters() {
		// No genre, no type, no year
		ImdbRequest request = new ImdbRequest(null, "test",
				null, null, null);
		var filters = request.filters();
		assertEquals(0, filters.size());
		// One genre, no type, no year
		request = new ImdbRequest(null, "test",
				"genre", null, null);
		filters = request.filters();
		assertEquals(1, filters.size());
		assertArrayEquals(new String[]{"genre"}, ((TermsFilter) filters.get(0)).getTerms());
		// One genre, one type, no year
		request = new ImdbRequest(null, "test",
				"genre", "movie", null);
		filters = request.filters();
		assertEquals(2, filters.size());
		assertArrayEquals(new String[]{"genre"}, ((TermsFilter) filters.get(0)).getTerms());
		assertArrayEquals(new String[]{"movie"}, ((TermsFilter) filters.get(1)).getTerms());
		// Various genres, one type, one year
		request = new ImdbRequest(null, "test",
				"a,b", "movie", null);
		filters = request.filters();
		assertEquals(2, filters.size());
		assertArrayEquals(new String[]{"a","b"}, ((TermsFilter) filters.get(0)).getTerms());
		assertArrayEquals(new String[]{"movie"}, ((TermsFilter) filters.get(1)).getTerms());
	}

	/**
	 * Test the correct generation of date range filters
	 */
	@Test
	public void testDatesFilters() {
		// No genre, no type, one year
		ImdbRequest request = new ImdbRequest(null, "test",
				null, null, "2000/2010");
		var filters = request.filters();
		assertEquals(1, filters.size());
		DateRangesFilter year = (DateRangesFilter) filters.get(0);
		assertEquals(1, year.getRanges().size());
		assertEquals("2000", year.getRanges().get(0).from);
		assertEquals("2010", year.getRanges().get(0).to);
		// No genre, no type, various year
		request = new ImdbRequest(null, "test",
				null, null, "2000/2010,1995/1998");
		filters = request.filters();
		assertEquals(1, filters.size());
		year = (DateRangesFilter) filters.get(0);
		assertEquals(2, year.getRanges().size());
		assertEquals("2000", year.getRanges().get(0).from);
		assertEquals("2010", year.getRanges().get(0).to);
		assertEquals("1995", year.getRanges().get(1).from);
		assertEquals("1998", year.getRanges().get(1).to);
	}

	/**
	 *  Test the correct generation of filters combining both types
	 */
	@Test
	public void testFilters() {
		// Various of each
		ImdbRequest request = new ImdbRequest(null, "test",
				"gen,re", "mo,vie", "2000/2010,1/2");
		var filters = request.filters();
		assertEquals(3, filters.size());
		assertArrayEquals(new String[]{"gen", "re"}, ((TermsFilter) filters.get(0)).getTerms());
		assertArrayEquals(new String[]{"mo", "vie"}, ((TermsFilter) filters.get(1)).getTerms());
		DateRangesFilter year = (DateRangesFilter) filters.get(2);
		assertEquals(2, year.getRanges().size());
		assertEquals("2000", year.getRanges().get(0).from);
		assertEquals("2010", year.getRanges().get(0).to);
		assertEquals("1", year.getRanges().get(1).from);
		assertEquals("2", year.getRanges().get(1).to);
		// One of each
		request = new ImdbRequest(null, "test",
				"genre", "movie", "2000/2010");
		filters = request.filters();
		assertEquals(3, filters.size());
		assertArrayEquals(new String[]{"genre"}, ((TermsFilter) filters.get(0)).getTerms());
		assertArrayEquals(new String[]{"movie"}, ((TermsFilter) filters.get(1)).getTerms());
		year = (DateRangesFilter) filters.get(2);
		assertEquals(1, year.getRanges().size());
		assertEquals("2000", year.getRanges().get(0).from);
		assertEquals("2010", year.getRanges().get(0).to);
	}

	/**
	 * Test the correct generation of aggregations
	 */
	@Test
	public void testAggregations() {
		var request = new ImdbRequest(null, "test",
				null, null, null);
		var aggs = request.aggregations();
		assertEquals(3, aggs.size());
		assertTrue(aggs.get(0) instanceof TermsAggregation);
		assertEquals(ImdbRequest.GENRES_AGG, aggs.get(0).getName());
		assertEquals(ImdbItem.GENRES, aggs.get(0).getField());
		assertTrue(aggs.get(1) instanceof TermsAggregation);
		assertEquals(ImdbRequest.TYPES_AGG, aggs.get(1).getName());
		assertEquals(ImdbItem.TYPE, aggs.get(1).getField());
		assertTrue(aggs.get(2) instanceof DividedRangeAggregation);
		assertEquals(ImdbRequest.YEAR_AGG, aggs.get(2).getName());
		assertEquals(ImdbItem.START, aggs.get(2).getField());
	}

	/**
	 * Test the correct generation of functions
	 */
	@Test
	public void testFunctions() {
		var request = new ImdbRequest(null, "test",
				null, null, null);
		var functions = request.functions();
		assertEquals(5, functions.size());
		assertTrue(functions.get(0) instanceof TermWeightingFunction);
		assertEquals(ImdbItem.TYPE, functions.get(0).getField());
		assertEquals("movie", ((TermWeightingFunction) functions.get(0)).getText());
		assertTrue(functions.get(1) instanceof TermWeightingFunction);
		assertEquals(ImdbItem.TYPE, functions.get(1).getField());
		assertEquals("tvEpisode", ((TermWeightingFunction) functions.get(1)).getText());
		assertTrue(functions.get(2) instanceof FieldValueFunction);
		assertEquals(ImdbRating.VOTES, functions.get(2).getField());
		assertTrue(functions.get(3) instanceof FieldValueFunction);
		assertEquals(ImdbRating.AVERAGE, functions.get(3).getField());
		assertTrue(functions.get(4) instanceof GaussDecayFunction);
		assertEquals(ImdbItem.START, functions.get(4).getField());
	}

	/**
	 * Test the correct generation of suggestions
	 */
	@Test
	public void testSuggestions() {
		// Request with query
		var request = new ImdbRequest(null, "test",
				null, null, null);
		var suggestions = request.suggestions();
		assertEquals(1, suggestions.size());
		assertTrue(suggestions.get(0) instanceof TermsSuggestion);
		assertEquals(ImdbItem.ORIGINAL_TITLE, suggestions.get(0).getField());
		assertEquals("test", suggestions.get(0).getText());
		// Request without query
		request = new ImdbRequest(null, null, null, null, null);
		suggestions = request.suggestions();
		assertEquals(0, suggestions.size());
	}

}
