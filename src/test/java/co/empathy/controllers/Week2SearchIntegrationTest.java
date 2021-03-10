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
public class Week2SearchIntegrationTest {

	@Inject
	@Client("/")
	RxHttpClient client;

	@Inject
	ObjectMapper mapper;

	@Inject
	TestHelper helper;

	private final UriBuilder baseUri = UriBuilder.of("/search");
	private final UriBuilder allUri = UriBuilder.of("/search/all");

	@Test
	public void testForrestGump() throws JsonProcessingException {
		System.out.println("Search the Forrest Gump movie with title query");
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
		System.out.print("Forrest Gump Id: ");
		forrestGumpMovies.stream().map(ImdbItem::getId).forEach(System.out::println);
	}

	@Test
	public void testTheAvengersSingle() throws JsonProcessingException {
		System.out.println("Search the 2012 The Avengers movie with title query");
		var uri = baseUri.queryParam("title", "The Avengers").toString();
		var request = HttpRequest.GET(uri);
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());
		// Check we didn't get the 2012 movie
		assertNotNull(retrieved);
		assertNotNull(retrieved.getItems());
		var theAvengers = retrieved.getItems().stream()
				.filter(x -> x.getTitleType().equals("movie") && x.getStartYear().equals("2012"));
		assertFalse(theAvengers.count() > 0);
		System.out.println("Movie The Avengers (2012) not found");
	}

	@Test
	public void testTheAvengersMulti() throws JsonProcessingException {
		System.out.println("Search the 2012 The Avengers movie with general query");
		var uri = allUri.queryParam("query", "The Avengers movie").toString();
		var request = HttpRequest.GET(uri);
		var jsonResult = client.toBlocking().retrieve(request);
		var retrieved = mapper.readValue(jsonResult, helper.getImdbResponseType());
		// Check we didn't get the 2012 movie
		assertNotNull(retrieved);
		assertNotNull(retrieved.getItems());
		var theAvengers = retrieved.getItems().stream()
				.filter(x -> x.getTitleType().equals("movie") && x.getStartYear().equals("2012"));
		assertTrue(theAvengers.count() > 0);
		System.out.println("Movie The Avengers (2012) found");
	}

	@Test
	public void testSpidermanSingle() throws JsonProcessingException {
		System.out.println("Search the 2002 Spiderman movie with title query");
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

	@Test
	public void testSpidermanMulti() throws JsonProcessingException {
		System.out.println("Search the 2002 Spiderman movie with general query");
		var uri = allUri.queryParam("query", "Spiderman movie").toString();
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
