package co.empathy.search;

import co.empathy.search.request.MyRequest;
import io.micronaut.context.annotation.DefaultImplementation;

import java.io.IOException;
import java.io.Serializable;

/**
 * Module managing the search engines
 */
@DefaultImplementation(ImdbSearcher.class)
public interface Searcher {

	/**
	 * Process a query and gets the search result matching the specified parameters
	 * of the request
	 * @param   request list of specified parameters of the search
	 * @return	Indexable object with the found info
	 * @throws IOException	if entered invalid queries or the search engine fails
	 */
	Serializable searchByQuery(MyRequest request) throws IOException;

}
