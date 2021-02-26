package co.empathy.controllers;

import co.empathy.pojos.SearchResult;
import co.empathy.search.Searcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Controller of the API search calls
 */
@Controller("/search")
public class SearchController {

    @Inject
	Searcher searcher;

	/**
	 * Get the search result of the input query
	 * @param query	Query to search
	 * @return	Search result
	 */
	@Get
	@Produces(MediaType.APPLICATION_JSON)
	public HttpResponse<SearchResult> searchQuery(@QueryValue String query) {
		try {
            SearchResult result = searcher.search(query);
			return HttpResponse.ok(result);
		} catch (JsonProcessingException e) {
		    // Error mapping the query
            return HttpResponse.badRequest();
		} catch (IOException e) {
			// Error contacting the ElasticSearch server
			return HttpResponse.serverError();
		}
	}

}
