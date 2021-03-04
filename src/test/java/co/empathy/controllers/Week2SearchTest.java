package co.empathy.controllers;

import co.empathy.beans.ImdbItem;
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

import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class Week2SearchTest {

	@Inject
	@Client("/")
	RxHttpClient client;

	@Inject
	ObjectMapper mapper;

	@Inject
	TestHelper helper;

	private final UriBuilder baseUri = UriBuilder.of("/search");

	@Test
	public void testForrestGump() throws JsonProcessingException {
		var uri = baseUri.queryParam("title", "Forrest Gump").toString();
		var request = HttpRequest.GET(uri);
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());
		// Check we got the movie
		assertNotNull(retrieved);
		assertNotNull(retrieved.getItems());
		var forrestGumpMovies = retrieved.getItems().stream()
				.filter(x -> x.getTitleType().equals("movie")).collect(Collectors.toList());
		assertTrue(forrestGumpMovies.size() > 0);
		// What's its id?
		System.out.print("Forrest Gump Id:");
		forrestGumpMovies.stream().map(ImdbItem::getId).forEach(System.out::println);
	}

	@Test
	public void testTheAvengers() throws JsonProcessingException {
		var uri = baseUri.queryParam("title", "The Avengers").toString();
		var request = HttpRequest.GET(uri);
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());
		// Check we got the movie
		assertNotNull(retrieved);
		assertNotNull(retrieved.getItems());
		var theAvengers = retrieved.getItems().stream()
				.filter(x -> x.getTitleType().equals("movie") && x.getStartYear().equals("2012"));
		assertFalse(theAvengers.count() > 0);
		System.out.println("Movie The Avengers (2012) not found");
	}
	@Test
	public void testSpiderman() throws JsonProcessingException {
		var uri = baseUri.queryParam("title", "Spiderman").toString();
		var request = HttpRequest.GET(uri);
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());
		// Check we got the movie
		assertNotNull(retrieved);
		assertNotNull(retrieved.getItems());
		var spidermanTobey = retrieved.getItems().stream()
				.filter(x -> x.getTitleType().equals("movie") && x.getStartYear().equals("2002"));
		assertFalse(spidermanTobey.count() > 0);
		System.out.println("Movie Spiderman (2002) not found");
	}

}
