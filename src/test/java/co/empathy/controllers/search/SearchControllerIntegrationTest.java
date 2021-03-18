package co.empathy.controllers.search;

import co.empathy.common.ImdbItem;
import co.empathy.util.TestHelper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.Arrays;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

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


	@Test
	public void testSearchTitleWithMultipleWords() throws JsonProcessingException {
		var uri = baseUri.queryParam("query", "Shawshank Redemption").toString();
		var request = HttpRequest.GET(uri);
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());

		assertNotNull(retrieved);
		assertEquals(1137, retrieved.getTotal());
		assertEquals(10, retrieved.getItems().size());
		assertTrue(retrieved.getItems().stream().map(ImdbItem::getPrimaryTitle).filter(Objects::nonNull)
				.allMatch(x -> x.contains("Shawshank") || x.contains("Redemption")));
	}

	@Test
	public void testSearchTitleWithGenre() throws JsonProcessingException {
		var uri = baseUri.queryParam("query", "Spiderman");
		uri.queryParam("genres", "action");
		var request = HttpRequest.GET(uri.toString());
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());

		assertNotNull(retrieved);
		assertEquals(206, retrieved.getTotal());
		assertEquals(10, retrieved.getItems().size());
		// Matching words test
		assertTrue(retrieved.getItems().stream().allMatch(
				x -> 	Objects.requireNonNull(x.getPrimaryTitle()).matches(".*[Ss]pider.*[Mm]an.*")
						&& Objects.requireNonNull(Arrays.toString(x.getGenres())).contains("Action")));
	}

	// TODO test search title with type

	// TODO test search title with range

	// TODO test search with all filters

	@Test
	public void testSearchWithoutQuery() {
		HttpRequest<String> request = HttpRequest.GET(baseUri.toString());
		HttpClientResponseException exception = assertThrows(HttpClientResponseException.class,
				() -> client.toBlocking().exchange(request));
		assertEquals(400, exception.getStatus().getCode());
	}

	@Test
	public void testSearchWithInvalidRange() {
		var uri = baseUri.queryParam("query", "test");
		uri.queryParam("year", "2000");
		var request = HttpRequest.GET(uri.toString());
		var exception = assertThrows(HttpClientResponseException.class,
				() -> client.toBlocking().exchange(request));
		assertEquals(400, exception.getStatus().getCode());
	}


}
