package co.empathy.controllers.search;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@MicronautTest
public class SearchControllerTest {

	@Inject
	@Client("/")
	RxHttpClient client;

	private final UriBuilder baseUri = UriBuilder.of("/search");

	/**
	 * Test that the controller sends an error message when the year is not complete
	 */
	@Test
	public void testSearchWithHalfRange() {
		// Only one range
		var uri = baseUri.queryParam("query", "test");
		uri.queryParam("year", "2000");
		var request = HttpRequest.GET(uri.toString());
		var exception = assertThrows(HttpClientResponseException.class,
				() -> client.toBlocking().exchange(request));
		assertEquals(400, exception.getStatus().getCode());
		assertEquals("Invalid request: Invalid range [2000] - Ranges of dates must have two edges", exception.getMessage());
	}

	/**
	 * Test that the controller sends an error message with empty year range
	 */
	@Test
	public void testSearchWithEmptyRange() {
		// Only one range
		var uri = baseUri.queryParam("query", "test");
		uri.queryParam("year", "");
		var request = HttpRequest.GET(uri.toString());
		var exception = assertThrows(HttpClientResponseException.class,
				() -> client.toBlocking().exchange(request));
		assertEquals(400, exception.getStatus().getCode());
		assertEquals("Invalid request: Invalid range [] - Ranges of dates must have two edges", exception.getMessage());
	}

	/**
	 * Test that the controller send an error message when the year has to many edges
	 */
	@Test
	public void testSearchWithExcessiveParamRange() {
		// Only one range
		var uri = baseUri.queryParam("query", "test");
		uri.queryParam("year", "2000/2001/2002");
		var request = HttpRequest.GET(uri.toString());
		var exception = assertThrows(HttpClientResponseException.class,
				() -> client.toBlocking().exchange(request));
		assertEquals(400, exception.getStatus().getCode());
		assertEquals("Invalid request: Invalid range [2000/2001/2002] - Ranges of dates must have two edges", exception.getMessage());
	}

}
