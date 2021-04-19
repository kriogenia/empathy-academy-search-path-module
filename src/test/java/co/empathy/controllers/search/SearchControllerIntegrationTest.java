package co.empathy.controllers.search;

import co.empathy.common.ImdbItem;
import co.empathy.search.request.ImdbRequest;
import co.empathy.util.TestHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static co.empathy.util.TestHelper.*;

@MicronautTest
public class SearchControllerIntegrationTest {

	@Inject
	@Client("/")
	RxHttpClient client;

	@Inject
	ObjectMapper mapper;

	@Inject
	TestHelper helper;

	private final UriBuilder baseUri = UriBuilder.of("/search");

	/**
	 * Test the search with a single or few results and the correct
	 * format of the response
	 * @throws JsonProcessingException  if the mapper can't build the object
	 */
	@Test
	public void testSearchTitleWithSingleWordAndFewResults() throws JsonProcessingException {
		// Less than 10 results
		String uri = baseUri.queryParam("query", "Carmencita").toString();
		HttpRequest<String> request = HttpRequest.GET(uri);
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());
		// Result check
		assertNotNull(retrieved);
		assertEquals(7, retrieved.getTotal());
		assertEquals(7, retrieved.getItems().size());
		assertTrue(retrieved.getItems().stream().map(ImdbItem::getPrimaryTitle).allMatch(x -> x.contains("Carmencita")));
		// Single unit check
		var carmencita = ImdbItem.buildFromString("tt0000001\tshort\tCarmencita\tCarmencita\t0\t1894\t\\N\t1\tDocumentary,Short");
		var retrievedCarmencita = retrieved.getItems().stream().filter(x -> x.getStartYear().equals("1894")).findFirst();
		var first = retrievedCarmencita.orElse(null);
		assertNotNull(first);
		assertEquals(carmencita.getId(), first.getId());
		assertEquals(carmencita.getPrimaryTitle(), first.getPrimaryTitle());
		assertArrayEquals(carmencita.getGenres(), first.getGenres());
		assertEquals(carmencita.getTitleType(), first.getTitleType());
		assertEquals(carmencita.getStartYear(), first.getStartYear());
		assertEquals(carmencita.getEndYear(), first.getEndYear());
	}

	/**
	 * Tests the search with a single word and more than 10 results
	 * @throws JsonProcessingException  if the mapper can't build the object
	 */
	@Test
	public void testSearchTitleWithSingleWordAndManyResults() throws JsonProcessingException {
		// More than 10 results
		var uri = baseUri.queryParam("query", "Jumanji").toString();
		var request = HttpRequest.GET(uri);
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());

		assertNotNull(retrieved);
		assertEquals(96, retrieved.getTotal());
		assertEquals(10, retrieved.getItems().size());
		assertTrue(retrieved.getItems().stream().map(ImdbItem::getPrimaryTitle).allMatch(x -> x.contains("Jumanji")));
	}

	/**
	 * Tests de search with multiple words
	 * @throws JsonProcessingException  if the mapper can't build the object
	 */
	@Test
	public void testSearchTitleWithMultipleWords() throws JsonProcessingException {
		var uri = baseUri.queryParam("query", "Shawshank Redemption").toString();
		var request = HttpRequest.GET(uri);
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());

		assertNotNull(retrieved);
		assertEquals(1162, retrieved.getTotal());
		assertEquals(10, retrieved.getItems().size());
		assertTrue(retrieved.getItems().stream().map(ImdbItem::getPrimaryTitle).filter(Objects::nonNull)
				.allMatch(x -> x.contains("Shawshank") || x.contains("Redemption")));
	}

	/**
	 * Test the search applying the genre filter.
	 * Asserting that all the items have that genre and aggregations change correctly.
	 * @throws JsonProcessingException  if the mapper can't build the object
	 */
	@Test
	public void testSearchTitleWithGenre() throws JsonProcessingException {
		var uri = baseUri.queryParam("query", "Spiderman");
		uri.queryParam("genres", "action");
		var request = HttpRequest.GET(uri.toString());
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());

		assertNotNull(retrieved);
		assertEquals(207, retrieved.getTotal());
		assertEquals(10, retrieved.getItems().size());
		// Matching words test
		assertTrue(retrieved.getItems().stream().allMatch(
				x -> 	Objects.requireNonNull(x.getPrimaryTitle()).matches(".*[Ss]pider.*[Mm]an.*")
						&& Objects.requireNonNull(Arrays.toString(x.getGenres())).contains("Action")));

		// Test faceting
		var aggs = retrieved.getAggregations();
		// Genres still showing the results the whole set
		assertEquals(118, aggs.get(ImdbRequest.GENRES_AGG).get("adventure"));
		// Types and year only showing the results of the filtered set
		assertEquals(122, aggs.get(ImdbRequest.YEAR_AGG).get("2010-2020"));
		assertEquals(20, aggs.get(ImdbRequest.TYPES_AGG).get("tvseries"));
	}

	/**
	 * Test the search applying the type filter.
	 * Asserting that all the items belong to that type and aggregations change correctly.
	 * @throws JsonProcessingException  if the mapper can't build the object
	 */
	@Test
	public void testSearchTitleWithType() throws JsonProcessingException {
		var uri = baseUri.queryParam("query", "Spiderman");
		uri.queryParam("type", "movie");
		var request = HttpRequest.GET(uri.toString());
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());

		assertNotNull(retrieved);
		assertEquals(42, retrieved.getTotal());
		assertEquals(10, retrieved.getItems().size());
		// Matching words test
		assertTrue(retrieved.getItems().stream().allMatch(
				x -> 	Objects.requireNonNull(x.getPrimaryTitle()).matches(".*[Ss]pider.*[Mm]an.*")
						&& Objects.requireNonNull(x.getTitleType()).contains("movie")));

		// Test faceting
		var aggs = retrieved.getAggregations();
		// Type still showing the results the whole set
		assertEquals(25, aggs.get(ImdbRequest.TYPES_AGG).get("tvseries"));
		// Genres and year only showing the results of the filtered set
		assertEquals(24, aggs.get(ImdbRequest.YEAR_AGG).get("2010-2020"));
		assertEquals(13, aggs.get(ImdbRequest.GENRES_AGG).get("adventure"));
	}

	/**
	 * Test the search applying the year filter.
	 * Asserting that all the items belong to that span and aggregations change correctly.
	 * @throws JsonProcessingException  if the mapper can't build the object
	 */
	@Test
	public void testSearchTitleWithYear() throws JsonProcessingException {
		var uri = baseUri.queryParam("query", "Spiderman");
		uri.queryParam("year", "2000/2010");
		var request = HttpRequest.GET(uri.toString());
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());

		assertNotNull(retrieved);
		assertEquals(119, retrieved.getTotal());
		assertEquals(10, retrieved.getItems().size());
		// Matching words test
		assertTrue(retrieved.getItems().stream().allMatch(
				x -> 	Objects.requireNonNull(x.getPrimaryTitle()).matches(".*[Ss]pider.*[Mm]an.*")));
		var startYears = retrieved.getItems().stream().map(ImdbItem::getStartYear)
				.filter(Objects::nonNull).map(Integer::parseInt);
		assertTrue(startYears.allMatch(x -> x >= 2000 & x <= 2010));

		// Test faceting
		var aggs = retrieved.getAggregations();
		// Year still showing the results the whole set
		assertEquals(1097, aggs.get(ImdbRequest.YEAR_AGG).get("2010-2020"));
		// Type and genres only showing the results of the filtered set
		assertEquals(26, aggs.get(ImdbRequest.GENRES_AGG).get("adventure"));
		assertEquals(2, aggs.get(ImdbRequest.TYPES_AGG).get("tvseries"));
	}

	/**
	 * Test the search applying two filters.
	 * Asserting that all the items matches them and aggregations are what they should.
	 * @throws JsonProcessingException  if the mapper can't build the object
	 */
	@Test
	public void testSearchTitleWithMultipleFilters() throws JsonProcessingException {
		var uri = baseUri.queryParam("query", "Spiderman");
		uri.queryParam("genres", "Action");
		uri.queryParam("type", "movie");
		var request = HttpRequest.GET(uri.toString());
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());

		assertNotNull(retrieved);
		assertEquals(27, retrieved.getTotal());
		assertEquals(10, retrieved.getItems().size());
		// Matching title
		assertTrue(retrieved.getItems().stream().allMatch(
				x -> 	Objects.requireNonNull(x.getPrimaryTitle()).matches(".*[Ss]pider.*[Mm]an.*")));
		// Matching genres
		var genres = retrieved.getItems().stream().map(ImdbItem::getGenres)
				.filter(Objects::nonNull).map(Arrays::toString);
		assertTrue(genres.allMatch(x -> x.contains("Action")));
		// Matching type
		var types = retrieved.getItems().stream().map(ImdbItem::getTitleType);
		assertTrue(types.allMatch(x -> x.equals("movie")));

		// Test faceting
		var aggs = retrieved.getAggregations();
		// Applied filters keep the global set
		assertEquals(13, aggs.get(ImdbRequest.GENRES_AGG).get("adventure"));
		assertEquals(20, aggs.get(ImdbRequest.TYPES_AGG).get("tvseries"));
		// Year test the filtered set
		assertEquals(13, aggs.get(ImdbRequest.YEAR_AGG).get("2010-2020"));
	}

	/**
	 * Test the search applying all the filters.
	 * Asserting that all the items matches them and aggregations are still global.
	 * @throws JsonProcessingException  if the mapper can't build the object
	 */
	@Test
	public void testSearchTitleWithAllFilters() throws JsonProcessingException {
		var uri = baseUri.queryParam("query", "Spiderman");
		uri.queryParam("genres", "Action");
		uri.queryParam("type", "movie");
		uri.queryParam("year", "2000/2010");
		var request = HttpRequest.GET(uri.toString());
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());

		assertNotNull(retrieved);
		assertEquals(4, retrieved.getTotal());
		assertEquals(4, retrieved.getItems().size());
		// Matching title
		assertTrue(retrieved.getItems().stream().allMatch(
				x -> 	Objects.requireNonNull(x.getPrimaryTitle()).matches(".*[Ss]pider.*[Mm]an.*")));
		// Matching genres
		var genres = retrieved.getItems().stream().map(ImdbItem::getGenres)
				.filter(Objects::nonNull).map(Arrays::toString);
		assertTrue(genres.allMatch(x -> x.contains("Action")));
		// Matching type
		var types = retrieved.getItems().stream().map(ImdbItem::getTitleType);
		assertTrue(types.allMatch(x -> x.equals("movie")));
		// Matching years
		var startYears = retrieved.getItems().stream().map(ImdbItem::getStartYear)
				.filter(Objects::nonNull).map(Integer::parseInt);
		assertTrue(startYears.allMatch(x -> x >= 2000 & x <= 2010));

		// Test faceting
		var aggs = retrieved.getAggregations();
		assertEquals(3, aggs.get(ImdbRequest.GENRES_AGG).get("adventure"));
		assertEquals(2, aggs.get(ImdbRequest.TYPES_AGG).get("tvseries"));
		assertEquals(13, aggs.get(ImdbRequest.YEAR_AGG).get("2010-2020"));
	}

	@Test
	public void testAggregationsOrder() throws JsonProcessingException {
		var uri = baseUri.queryParam("query", "Spiderman");
		var request = HttpRequest.GET(uri.toString());
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());
		// Order asserting
		assertAscendantOrder(retrieved.getAggregations().get(ImdbRequest.GENRES_AGG));
		assertAscendantOrder(retrieved.getAggregations().get(ImdbRequest.TYPES_AGG));
		assertDescendantOrder(retrieved.getAggregations().get(ImdbRequest.YEAR_AGG));
	}

	/**
	 * Tests the search without query, it should return the top 10
	 * @throws JsonProcessingException if the mapper can't build the object
	 */
	@Test
	public void testSearchWithoutQuery() throws JsonProcessingException {
		HttpRequest<String> request = HttpRequest.GET(baseUri.toString());
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());
		// Result check
		assertNotNull(retrieved);
		assertEquals(10000, retrieved.getTotal());
		assertEquals(10, retrieved.getItems().size());
	}

}
