package co.empathy.controllers.title;

import co.empathy.common.ImdbItem;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.uri.UriBuilder;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class TitleControllerIntegrationTest {

	@Inject
	@Client("/")
	RxHttpClient client;

	@Inject
	ObjectMapper mapper;

	private final UriBuilder baseUri = UriBuilder.of("/titles");

	/**
	 * Test the retrieval of the details of a title with all its info
	 * @throws JsonProcessingException  if the mapper can't build the object
	 */
	@Test
	public void testCompleteTitle() throws JsonProcessingException {
		String uri = baseUri.path("/tt0108778").toString();
		HttpRequest<String> request = HttpRequest.GET(uri);
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, ImdbItem.class);
		// Result check
		assertNotNull(retrieved);
		assertEquals("tt0108778", retrieved.getId());
		assertEquals("Friends", retrieved.getPrimaryTitle());
		assertEquals("Friends", retrieved.getOriginalTitle());
		assertEquals("tvSeries", retrieved.getTitleType());
		assertArrayEquals(new String[]{"Comedy", "Romance"}, retrieved.getGenres());
		assertEquals("1994", retrieved.getStartYear());
		assertEquals("2004", retrieved.getEndYear());
		assertEquals(false, retrieved.getIsAdult());
		assertEquals("22", retrieved.getRuntime());
		assertEquals(8.9, retrieved.getAverageRating());
		assertEquals(836234, retrieved.getVotes());
	}

	/**
	 * Test the retrieval of the details of a title with not all the possible info
	 * @throws JsonProcessingException  if the mapper can't build the object
	 */
	@Test
	public void testPartialTitle() throws JsonProcessingException {
		String uri = baseUri.path("/tt0764727").toString();
		HttpRequest<String> request = HttpRequest.GET(uri);
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, ImdbItem.class);
		// Result check
		assertNotNull(retrieved);
		assertEquals("tt0764727", retrieved.getId());
		assertEquals("Carmencita mia", retrieved.getPrimaryTitle());
		assertEquals("Carmencita mia", retrieved.getOriginalTitle());
		assertEquals("movie", retrieved.getTitleType());
		assertArrayEquals(new String[]{"Comedy", "Musical", "Romance"}, retrieved.getGenres());
		assertEquals("1948", retrieved.getStartYear());
		assertEquals(false, retrieved.getIsAdult());
		assertNull(retrieved.getEndYear());
		assertNull(retrieved.getRuntime());
		assertNull(retrieved.getAverageRating());
		assertNull(retrieved.getVotes());
	}

	/**
	 * Tests empty result when no id matches
	 */
	@Test
	public void testNonExistentTitle() {
		String uri = baseUri.path("/no_match").toString();
		HttpRequest<String> request = HttpRequest.GET(uri);
		var jsonResult = client.toBlocking().retrieve(request);
		assertEquals("{}", jsonResult);
	}

}
