package com.example.controllers;

import com.example.pojos.SearchResult;
import com.example.search.Searcher;
import com.example.search.SearcherImpl;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.core.MainResponse;

import javax.swing.event.HyperlinkEvent;
import java.io.IOException;

@Controller("/search")
public class SearchController {

	@Get
	@Produces(MediaType.APPLICATION_JSON)
	public HttpResponse<String> searchQuery(@QueryValue String query) {
		try {
            Searcher searcher = new SearcherImpl();
            String json = searcher.search(query);
			return HttpResponse.ok().body(json);
		} catch (JsonProcessingException e) {
		    // Error mapping the query
            return HttpResponse.badRequest();
		} catch (IOException e) {
			// Error contacting the ElasticSearch server
			return HttpResponse.serverError();
		}
	}

}
