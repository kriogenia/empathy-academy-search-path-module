package co.empathy.controllers;

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
import java.io.Serializable;

/**
 * Controller of the API search calls
 */
@Controller("/search")
public class SearchController {

    @Inject
	Searcher searcher;

	/**
	 * Get the search result of the input query
	 * @param title	Query to search original title
	 * @return	Search result
	 */
	@Get
	@Produces(MediaType.APPLICATION_JSON)
	public HttpResponse<Serializable> searchQuery(
			@QueryValue String title
	) {
		try {
            var result = searcher.searchByTitle(title);
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
