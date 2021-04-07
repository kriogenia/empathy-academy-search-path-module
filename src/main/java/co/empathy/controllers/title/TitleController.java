package co.empathy.controllers.title;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Produces;

import java.io.Serializable;

@Controller("/title")
public class TitleController {

	@Get("/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Serializable returnTitle(@PathVariable String id) {
		return "{ \"id\": " + id + "}";
	}

}
