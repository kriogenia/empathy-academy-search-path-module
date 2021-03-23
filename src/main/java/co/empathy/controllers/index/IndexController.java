package co.empathy.controllers.index;

import co.empathy.index.Indexer;
import co.empathy.index.configuration.ImdbIndexConfiguration;
import co.empathy.index.configuration.TestIndexConfiguration;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;
import io.micronaut.http.sse.Event;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;

import javax.inject.Inject;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.concurrent.TimeUnit;

/**
 * Controller of the API index calls
 */
@Controller("/index")
public class IndexController {

	@Inject
	Indexer indexer;

	@Inject
	ImdbIndexConfiguration imdbConfig;

	@Inject
	TestIndexConfiguration testConfig;

	@Get("/{index}")
	@Produces(MediaType.APPLICATION_JSON)
	public String searchByQuery(@PathVariable String index) throws IOException {
		// TODO improve this -> stream current percentage
		if (index.equals("imdb")) {
			indexer.setConfiguration(imdbConfig);
		} else if (index.equals("test")) {
			indexer.setConfiguration(testConfig);
		} else {
			throw new IllegalArgumentException("The specified index does not exists");
		}
		if (indexer.deleteIndex()) {
			indexer.bulkIndexFile();
			return "Ok";
		} else {
			throw new IllegalArgumentException("The specified index does not exists");
		}
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
