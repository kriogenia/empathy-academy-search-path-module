package co.empathy.controllers.search;

import co.empathy.search.request.ImdbRequest;
import co.empathy.search.Searcher;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.Valid;
import java.io.IOException;
import java.io.Serializable;

/**
 * Controller of the API search calls
 */
@Controller("/search")
public class SearchController {

	private final static Logger LOG = LoggerFactory.getLogger(SearchController.class);

	@Inject
	Searcher searcher;

	/**
	 * Get the search result of the specified request
	 * @param request	Parameters of the search:
	 *                      ?query: specified query to search an entry based on the original title
	 *                      [?genres]: comma separated list of genres to which titles must belong to
	 *                      [?type]: comma separated list of types to which titles must belong to
	 *                      [?year]: comma separated list of ranges, such as 2000/2010, to which
	 *                                  titles have been released during such period
	 * @return	Search result
	 */
	@Get
	@Produces(MediaType.APPLICATION_JSON)
	public Serializable searchByQuery(@Valid @RequestBean ImdbRequest request) throws IOException {
		return searcher.searchByQuery(request);
	}

	/**
	 * Handles the internal parsing exceptions
	 * @param request   request associated to the error
	 * @param e         IllegalArgumentException of the error
	 * @return          response with the error json
	 */
	@Error
	public HttpResponse<JsonError> iaeError(HttpRequest<?> request, IllegalArgumentException e) {
		LOG.error(e.getMessage());
		JsonError error = new JsonError("Invalid request: "+ e.getMessage())
				.link(Link.SELF, Link.of(request.getUri()));
		return HttpResponse.<JsonError>status(HttpStatus.BAD_REQUEST, "Invalid request")
				.body(error);
	}

	/**
	 * Handles the JSON parsing exceptions
	 * @param request   request associated to the error
	 * @param e         JsonException of the error
	 * @return          response with the error json
	 */
	@Error
	public HttpResponse<JsonError> jsonError(HttpRequest<?> request, JsonProcessingException e) {
		LOG.error(e.getMessage());
		JsonError error = new JsonError("Invalid JSON: "+ e.getMessage())
				.link(Link.SELF, Link.of(request.getUri()));
		return HttpResponse.<JsonError>status(HttpStatus.BAD_REQUEST, "Invalid JSON")
				.body(error);
	}

}
