package co.empathy.controllers.search;

import co.empathy.search.request.MovieRequest;
import co.empathy.search.Searcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
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
	 * Get the search result of the specified request
	 * @param request	Parameters of the search:
	 *                      ?query: specified query to search a movie based on the original title
	 *                      [?genres]: comma separated list of genres to which titles must belong to
	 *                      [?type]: comma separated list of types to which titles must belong to
	 * @return	Search result
	 */
	@Get
	@Produces(MediaType.APPLICATION_JSON)
	public HttpResponse<Serializable> searchByQuery(@Valid @RequestBean MovieRequest request) {
		try {
            var result = searcher.searchByQuery(request);
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
