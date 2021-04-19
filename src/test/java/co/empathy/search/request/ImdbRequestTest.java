package co.empathy.search.request;

import co.empathy.common.ImdbItem;
import co.empathy.common.ImdbRating;
import co.empathy.search.request.aggregations.DividedRangeAggregation;
import co.empathy.search.request.aggregations.TermsAggregation;
import co.empathy.search.request.filters.TermsFilter;
import co.empathy.search.request.functions.FieldValueFunction;
import co.empathy.search.request.functions.GaussDecayFunction;
import co.empathy.search.request.functions.TermWeightingFunction;
import co.empathy.search.request.queries.DisjunctionMaxQuery;
import co.empathy.search.request.queries.PartialPlusPerfectQuery;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class ImdbRequestTest {

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

	@Test
	public void testTermsFilters() {
		// One genre, no type
		ImdbRequest request = new ImdbRequest(null, "test",
				"genre", null, null);
		var filters = request.filters();
		assertEquals(1, filters.size());
		assertArrayEquals(new String[]{"genre"}, ((TermsFilter) filters.get(0)).getTerms());
		// One genre, one type
		request = new ImdbRequest(null, "test",
				"genre", "movie", null);
		filters = request.filters();
		assertEquals(2, filters.size());
		assertArrayEquals(new String[]{"genre"}, ((TermsFilter) filters.get(0)).getTerms());
		assertArrayEquals(new String[]{"movie"}, ((TermsFilter) filters.get(1)).getTerms());
		// Various genres, one type
		request = new ImdbRequest(null, "test",
				"a,b", "movie", null);
		filters = request.filters();
		assertEquals(2, filters.size());
		assertArrayEquals(new String[]{"a","b"}, ((TermsFilter) filters.get(0)).getTerms());
		assertArrayEquals(new String[]{"movie"}, ((TermsFilter) filters.get(1)).getTerms());
	}

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

	//TODO test suggestions

}
