package co.empathy.controllers;

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

	private final UriBuilder baseUri = UriBuilder.of("/search");
/*
	@Test
	public void testSearchWithValidQuery() {
		String uri = baseUri.queryParam("query", "test").toString();
		HttpRequest<String> request = HttpRequest.GET(uri);
		SearchResult expected = new SearchResult("test", "7.11.1");
		SearchResult retrieved = client.toBlocking().retrieve(request, SearchResult.class);

		assertNotNull(retrieved);
		assertEquals(expected.getQuery(), retrieved.getQuery());
		assertEquals(expected.getCluster_name(), retrieved.getCluster_name());
	}
*/
	@Test
	public void testSearchWithoutQuery() {
		HttpRequest<String> request = HttpRequest.GET(baseUri.toString());
		HttpClientResponseException exception = assertThrows(HttpClientResponseException.class,
				() -> client.toBlocking().exchange(request));
		assertEquals(400, exception.getStatus().getCode());
	}
}
