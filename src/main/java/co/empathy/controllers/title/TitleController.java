package co.empathy.controllers.title;

import co.empathy.exceptions.NoResultException;
import co.empathy.search.Searcher;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;

import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;

@Controller("/titles")
public class TitleController {

	@Inject
	Searcher searcher;

	@Get("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Serializable returnTitle(@PathVariable String id) throws IOException {
		return searcher.searchById(id);
	}

}
