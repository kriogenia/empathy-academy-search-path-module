package co.empathy.controllers.index;

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
public class IndexControllerTest {

	@Inject
	@Client("/")
	RxHttpClient client;

	private final UriBuilder baseUri = UriBuilder.of("/index");

	/**
	 * Test the correct message error when requested to index a non-valid index
	 */
	@Test
	public void testInvalidIndex() {
		var uri = baseUri.path("/invalid").toString();
		HttpRequest<String> request = HttpRequest.GET(uri);
		var exception = assertThrows(HttpClientResponseException.class,
				() -> client.toBlocking().exchange(request));
		assertEquals(400, exception.getStatus().getCode());
		assertEquals("Invalid request: The index invalid does not exist", exception.getMessage());
	}

}
