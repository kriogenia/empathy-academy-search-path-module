package co.empathy.controllers.index;

import co.empathy.index.Indexer;
import co.empathy.index.configuration.ConfigurationsManager;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;

import javax.inject.Inject;
import java.io.IOException;

/**
 * Controller of the API index calls
 */
@Controller("/index")
public class IndexController {

	@Inject
	Indexer indexer;

	@Inject
	ConfigurationsManager configs;

	@Get("/{index}")
	@Produces(MediaType.APPLICATION_JSON)
	public String searchByQuery(@PathVariable String index) throws IOException {
		indexer.setConfiguration(configs.getConfiguration(index))
				.setExtensions(configs.getExtensions(index));
		indexer.delete();
		indexer.bulkIndex();
		indexer.bulkExtend();
		return "The indexing of " + index + " has been completed";
	}

	/**
	 * Handles the internal parsing exceptions          // TODO change to new own exception
	 * @param request   request associated to the error
	 * @param e         IllegalArgumentException of the error
	 * @return          response with the error json
	 */
	@Error
	public HttpResponse<JsonError> iaeError(HttpRequest<?> request, IllegalArgumentException e) {
		// TODO make global?
		JsonError error = new JsonError("Invalid request: "+ e.getMessage())
				.link(Link.SELF, Link.of(request.getUri()));
		return HttpResponse.<JsonError>status(HttpStatus.BAD_REQUEST, "Invalid request")
				.body(error);
	}

}
