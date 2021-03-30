package co.empathy.search.request;

import co.empathy.common.ImdbItem;
import co.empathy.search.request.filters.TermsFilter;
import co.empathy.search.request.queries.DisjunctionMaxQuery;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class MovieRequestTest {

	@Test
	public void testMusts() {
		MovieRequest request = new MovieRequest(null, "test",
				null, null, null);
		var musts = request.musts();
		assertEquals(1, musts.size());
		assertTrue(musts.get(0) instanceof DisjunctionMaxQuery);
		var queries = ((DisjunctionMaxQuery) musts.get(0)).getQueries();
		for (var query : queries) {
			assertTrue(query instanceof BoolQueryBuilder);
			var builder = (BoolQueryBuilder) query;
			System.out.println(builder.toString());
			assertEquals(1, builder.must().size());
		}
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
	public void testAggregations() {
		var request = new MovieRequest(null, "test",
				null, null, null);
		var aggs = request.aggregations();
		assertEquals(3, aggs.size());
	}

}
