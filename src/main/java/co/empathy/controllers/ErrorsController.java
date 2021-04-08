package co.empathy.controllers;

import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Error;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.hateoas.Link;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Controller
public class ErrorsController {

    private final static Logger LOG = LoggerFactory.getLogger(ErrorsController.class);

    @Get("/hello")
    @Produces(MediaType.TEXT_PLAIN)
    public String index() {
        return "Hello World";
    }

    /**
     * Handles the IOExceptions, caused in the server
     * @param request   request associated to the error
     * @param e         IOException of the error
     * @return          response with the error json
     */
    @Error(global = true)
    public HttpResponse<JsonError> ioError(HttpRequest<?> request, IOException e) {
        LOG.error(request + e.getMessage());
        JsonError error = new JsonError("Server internal error").link(Link.SELF, Link.of(request.getUri()));
        return HttpResponse.<JsonError>serverError().body(error);
    }

}
