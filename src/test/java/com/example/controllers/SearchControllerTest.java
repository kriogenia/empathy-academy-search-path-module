package com.example.controllers;

import com.example.pojos.SearchResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class SearchControllerTest {

	@Inject
	@Client("/")
	RxHttpClient client;

	@Test
	public void testSearchWithParam() {
		HttpRequest<String> request = HttpRequest.GET("/search/test");
		String body = client.toBlocking().retrieve(request);
		SearchResult expected = new SearchResult("test", "version");

		assertNotNull(body);
		ObjectMapper mapper = new ObjectMapper();
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
	public void testSearchWithoutParams() {
		HttpRequest<String> request = HttpRequest.GET("/search");
		HttpClientResponseException exception = assertThrows(HttpClientResponseException.class,
				() -> client.toBlocking().exchange(request));
		assertEquals(404, exception.getStatus().getCode());
	}
}
