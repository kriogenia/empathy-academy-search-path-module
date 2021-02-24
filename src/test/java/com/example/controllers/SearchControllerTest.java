package com.example.controllers;

import com.example.pojos.SearchResult;
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

import java.net.URI;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class SearchControllerTest {

	@Inject
	@Client("/")
	RxHttpClient client;

	private final UriBuilder baseUri = UriBuilder.of("/search");

	@Test
	public void testSearchWithValidQuery() {
		String uri = baseUri.queryParam("query", "test").toString();
		HttpRequest<String> request = HttpRequest.GET(uri);
		String body = client.toBlocking().retrieve(request);
		ObjectMapper mapper = new ObjectMapper();
		SearchResult expected = new SearchResult("test", "7.11.1");

		assertNotNull(body);
		SearchResult retrieved = null;
		try {
			retrieved = mapper.readValue(body, SearchResult.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		assertNotNull(retrieved);
		assertEquals(expected.getQuery(), retrieved.getQuery());
		assertEquals(expected.getCluster_name(), retrieved.getCluster_name());
	}

	@Test
	public void testSearchWithoutQuery() {
		HttpRequest<String> request = HttpRequest.GET("/search");
		HttpClientResponseException exception = assertThrows(HttpClientResponseException.class,
				() -> client.toBlocking().exchange(request));
		assertEquals(404, exception.getStatus().getCode());
	}
}
