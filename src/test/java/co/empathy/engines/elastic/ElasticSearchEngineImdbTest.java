package co.empathy.engines.elastic;

import co.empathy.common.ImdbItem;
import co.empathy.search.request.MockMyRequest;
import co.empathy.search.request.aggregations.RequestAggregation;
import co.empathy.search.request.filters.RequestFilter;
import co.empathy.search.request.filters.TermsFilter;
import co.empathy.search.request.queries.DisjunctionMaxQuery;
import co.empathy.search.request.queries.PartialPlusPerfectQuery;
import co.empathy.search.request.queries.RequestQuery;
import co.empathy.search.response.SearchResult;
import co.empathy.util.ElasticSearchTestHelper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import javax.inject.Inject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static co.empathy.util.TestHelper.*;

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ElasticSearchEngineImdbTest {

	@Inject
	ElasticSearchEngine engine;

	@Inject
	ElasticSearchTestHelper helper;

	@Inject
	MockMyRequest request;

	// Maps of the request
	List<RequestQuery> must = new ArrayList<>();
	List<RequestFilter> filter = new ArrayList<>();
	List<RequestAggregation> aggs = new ArrayList<>();

	/**
	 * Specifies the maps to use on the mocks
	 */
	@BeforeAll
	public void createRequest() {
		request.mockMusts(must);
		request.mockFilters(filter);
		request.mockAggregations(aggs);
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
	 * Test the ascii_folding filters applied to the titles
	 * @throws IOException	if the engine fails
	 */
	@Test
	public void testAsciiFoldingFilter() throws IOException {
		// Ñ
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "Ocana"));
		var items = helper.performSingleMatch(engine, request, 1, 1);
		assertTrue(allContains("title", "Ocaña", items));
	}

	/**
	 * Tests the lowercase filters applied to the titles
	 * @throws IOException	if the engine fails
	 */
	@Test
	public void testLowercaseFilter() throws IOException {
		List<SearchResult> results = new ArrayList<>();
		// All lowercase
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "carmencita"));
		results.add(engine.scoredSearch(request, ElasticSearchTestHelper.INDEX));
		must.clear();
		// All uppercase
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "CARMENCITA"));
		results.add(engine.scoredSearch(request, ElasticSearchTestHelper.INDEX));
		must.clear();
		// Combined
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "CarMeNCiTA"));
		results.add(engine.scoredSearch(request, ElasticSearchTestHelper.INDEX));

		for (var result: results) {
			assertEquals(1, result.getTotal());
			allContains(ImdbItem.TITLE, "Carmencita", result.getItems());
		}
	}

	/**
	 * Tests the number extensive search filter applied to the titles
	 * @throws IOException	if the engine fails
	 */
	@Test
	public void testNumberExtensionFilter() throws IOException {
		// Text and roman to arabic
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "two"));
		var result1 = engine.scoredSearch(request, ElasticSearchTestHelper.INDEX);
		must.clear();
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "ii"));
		var result2 = engine.scoredSearch(request, ElasticSearchTestHelper.INDEX);

		assertEquals(1, result1.getTotal());
		assertEquals(1, result2.getTotal());
		assertTrue(allContains("title","The Amazing Spider-Man 2", result1.getItems()));
		assertTrue(allContains("title","The Amazing Spider-Man 2", result2.getItems()));

		// Roman and arabic to text
		must.clear();
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "iv"));
		result1 = engine.scoredSearch(request, ElasticSearchTestHelper.INDEX);
		must.clear();
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "4"));
		result2 = engine.scoredSearch(request, ElasticSearchTestHelper.INDEX);

		assertEquals(1, result1.getTotal());
		assertEquals(1, result2.getTotal());
		assertTrue(allContains("title","The Fantastic Four", result1.getItems()));
		assertTrue(allContains("title","The Fantastic Four", result2.getItems()));

		// Arabic and text to roman
		must.clear();
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "5"));
		result1 = engine.scoredSearch(request, ElasticSearchTestHelper.INDEX);
		must.clear();
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "five"));
		result2 = engine.scoredSearch(request, ElasticSearchTestHelper.INDEX);

		assertEquals(1, result1.getTotal());
		assertEquals(1, result2.getTotal());
		assertTrue(allContains("title","Rocky V", result1.getItems()));
		assertTrue(allContains("title","Rocky V", result2.getItems()));
	}

	/**
	 * Tests the word delimited filter with the -
	 * @throws IOException	if the engine fails
	 */
	@Test
	public void testDelimiterWithHyphen() throws IOException {
		List<SearchResult> results = new ArrayList<>();
		// Concatenated
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "spiderman"));
		results.add(engine.scoredSearch(request, ElasticSearchTestHelper.INDEX));
		must.clear();
		// Split
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "spider man"));
		results.add(engine.scoredSearch(request, ElasticSearchTestHelper.INDEX));
		must.clear();
		// With hyphen
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "spider-man"));
		results.add(engine.scoredSearch(request, ElasticSearchTestHelper.INDEX));

		for (var result: results) {
			assertEquals(1, result.getTotal());
			allContains("title","The Amazing Spider-Man 2", result.getItems());
		}
	}

	/**
	 * Test the word delimiter filter with the '
	 * @throws IOException	if the engine fails
	 */
	@Test
	public void testDelimiterWithApostrophes() throws IOException {
		List<SearchResult> results = new ArrayList<>();
		// Concatenated
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "youre"));
		results.add(engine.scoredSearch(request, ElasticSearchTestHelper.INDEX));
		must.clear();
		// Split
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "you re"));
		results.add(engine.scoredSearch(request, ElasticSearchTestHelper.INDEX));
		must.clear();
		// With hyphen
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "you're"));
		results.add(engine.scoredSearch(request, ElasticSearchTestHelper.INDEX));

		for (var result: results) {
			assertEquals(1, result.getTotal());
			allContains("title","You're Fired!", result.getItems());
		}
	}

	/**
	 * Test the word delimiter with . and ?
	 * @throws IOException	if the engine fails
	 */
	@Test
	public void testDelimiterWithOtherSymbols() throws IOException {
		//dr who to Dr. Who and Dr. Who?
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "dr who"));
		var items = helper.performSingleMatch(engine, request, 2, 2);
		assertTrue(allContains("title", "Dr. Who", items));
	}

	/**
	 * Tests the matches are related to only the original_title
	 * @throws IOException	if the engine fails
	 */
	@Test
	public void testDifferentTitles() throws IOException {
		// Original title
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "Gisaengchung"));
		var items = helper.performSingleMatch(engine, request, 1,1);
		assertTrue(allContains(ImdbItem.TITLE,"Parasite", items));
		must.clear();
		// Primary title
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "Parasite"));
		items = helper.performSingleMatch(engine, request, 0, 0);
		assertEquals(0, items.size());
	}

	@Test
	public void testDisjunctionTitles() throws IOException {
		var query = new DisjunctionMaxQuery();
		query.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "Parasite"));
		query.add(new PartialPlusPerfectQuery(ImdbItem.TITLE, "Parasite"));
		must.add(query);
		var items = helper.performSingleMatch(engine, request, 1, 1);
		assertTrue(allContains(ImdbItem.TITLE, "Parasite", items));
	}

	/**
	 * Test that the search can find stop word titles like It
	 * @throws IOException	if the engine fails
	 */
	@Test
	public void testStopWords() throws IOException {
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "It"));
		var items = helper.performSingleMatch(engine, request, 1, 1);
		assertTrue(allContains(ImdbItem.TITLE, "It", items));
	}

	/**
	 * Test that the search can filter titles using the type
	 * @throws IOException  if the engine fails
	 */
	@Test
	public void testTypeFilter() throws IOException {
		// No types
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "the"));
		var items = helper.performSingleMatch(engine, request, 5, 5);
		// One type
		filter.add(new TermsFilter(ImdbItem.TYPE, "movie"));
		items = helper.performSingleMatch(engine, request, 3, 3);
		assertTrue(allContains(ImdbItem.TYPE, "movie", items));
		filter.clear();
		// Various types
		filter.add(new TermsFilter(ImdbItem.TYPE, "movie,tvSeries"));
		items = helper.performSingleMatch(engine, request, 4, 4);
		assertTrue(items.stream().map(x -> x.get(ImdbItem.TYPE).toString())
				.allMatch(x -> x.contains("movie") || x.contains("tvSeries")));
		filter.clear();
	}

	/**
	 * Test that the search can filter titles using the genres
	 * @throws IOException  if the engine fails
	 */
	@Test
	public void testGenresFilter() throws IOException {
		// No genres
		must.add(new PartialPlusPerfectQuery(ImdbItem.ORIGINAL_TITLE, "the"));
		var items = helper.performSingleMatch(engine, request, 5, 5);
		// One type
		filter.add(new TermsFilter(ImdbItem.GENRES, "action"));
		items = helper.performSingleMatch(engine, request, 3, 3);
		assertTrue(allContains(ImdbItem.GENRES, "Action", items));
		filter.clear();
		// Various types
		filter.add(new TermsFilter(ImdbItem.GENRES, "family,comedy"));
		items = helper.performSingleMatch(engine, request, 2, 2);
		assertTrue(items.stream().map(x -> x.get(ImdbItem.GENRES).toString())
				.allMatch(x -> x.contains("Family") || x.contains("Comedy")));
		filter.clear();
	}

	@Test
	public void testTypeSynonyms()  {/*
		// film => movie
		must.put(ImdbItem.TYPE, "film");
		var items = helper.performSingleMatch(engine, request, 10, 10);
		assertTrue(allContains("type", "movie", items));}
		// picture => movie
		items = performMultiMatch(engine, "it picture", 10, 10);
		assertTrue(allContains("type", "movie", items));
		// series => tvSeries
		items = performMultiMatch(engine,"mink series", 2, 2);
		assertTrue(anyContains("type", "tvSeries", items) ||
				anyContains("title", "Mink", items)
		);
		// ep => tvepisode
		items = performMultiMatch(engine, "who ep", 3, 3);
		assertTrue(allContains("type", "tvEpisode", items));
		// episode => tvepisode
		items = performMultiMatch(engine, "who episode", 3, 3);
		assertTrue(allContains("type", "tvEpisode", items));
		// special => tvspecial
		items = performMultiMatch(engine,"mink special", 2, 2);
		assertTrue(anyContains("type", "tvSpecial", items) ||
				anyContains("title", "Mink", items)
		);
		// miniseries => tvminiseries
		items = performMultiMatch(engine,"mink miniseries", 2, 2);
		assertTrue(anyContains("type", "tvMiniSeries", items) ||
				anyContains("title", "Mink", items)
		);
		// game => videogame
		items = performMultiMatch(engine,"mink game", 2, 2);
		assertTrue(anyContains("type", "videoGame", items) ||
				anyContains("title", "Mink", items)
		);*/
	}

}
