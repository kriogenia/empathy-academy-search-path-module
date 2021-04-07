package co.empathy.search;

import co.empathy.exceptions.NoResultException;
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

	/**
	 * Returns the object in the index related to the specified ID
	 * @param id    unique identified of the entry
	 * @return      indexed entry with that id
	 * @throws NoResultException    if there's no results for the specified id
	 * @throws IOException          if the search engine fails
	 */
	Serializable searchById(String id) throws IOException;

}
