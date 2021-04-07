package co.empathy.controllers.title;

import co.empathy.search.Searcher;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;

import javax.inject.Inject;
import java.io.IOException;
import java.io.Serializable;

@Controller("/title")
public class TitleController {

	@Inject
	Searcher searcher;

	@Get("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Serializable returnTitle(@PathVariable String id) throws IOException {
		return searcher.searchById(id);
	}

}
