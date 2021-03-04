package co.empathy.controllers;

import co.empathy.beans.ImdbItem;
import co.empathy.beans.SearchResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;


import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class SearchControllerTest {

	@Inject
	@Client("/")
	RxHttpClient client;

	@Inject
	ObjectMapper mapper;

	private final UriBuilder baseUri = UriBuilder.of("/search");

	@Test
	public void testSearchTitleWithSingleWordAndFewResults() throws JsonProcessingException {
		// Less than 10 results
		String uri = baseUri.queryParam("title", "Carmencita").toString();
		HttpRequest<String> request = HttpRequest.GET(uri);
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, getImdbResponseType());
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
		var uri = baseUri.queryParam("title", "Jumanji").toString();
		var request = HttpRequest.GET(uri);
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, getImdbResponseType());

		assertNotNull(retrieved);
		assertEquals(98, retrieved.getTotal());
		assertEquals(10, retrieved.getItems().size());
		assertTrue(retrieved.getItems().stream().map(ImdbItem::getPrimaryTitle).allMatch(x -> x.contains("Jumanji")));
	}


	@Test
	public void testSearchWithoutQuery() {
		HttpRequest<String> request = HttpRequest.GET(baseUri.toString());
		HttpClientResponseException exception = assertThrows(HttpClientResponseException.class,
				() -> client.toBlocking().exchange(request));
		assertEquals(400, exception.getStatus().getCode());
	}

	/**
	 * Builds the type to parse the responses
	 * @return	SearchResponse<ImdbItem> TypeReference
	 */
	private TypeReference<SearchResponse<ImdbItem>> getImdbResponseType() {
		return new TypeReference<>() {};
	}
}
