package com.example.controllers;

import com.example.pojos.SearchResult;
import com.example.search.Searcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;

import javax.inject.Inject;
import java.io.IOException;

@Controller("/search")
public class SearchController {

    @Inject
    Searcher searcher;

	@Get
	@Produces(MediaType.APPLICATION_JSON)
	public HttpResponse<SearchResult> searchQuery(@QueryValue String query) {
		try {
            SearchResult result = searcher.search(query);
			return HttpResponse.ok().body(result);
		} catch (JsonProcessingException e) {
		    // Error mapping the query
            return HttpResponse.badRequest();
		} catch (IOException e) {
			// Error contacting the ElasticSearch server
			return HttpResponse.serverError();
		}
	}

}
