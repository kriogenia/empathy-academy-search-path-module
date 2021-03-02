package co.empathy.engines;

import co.empathy.index.Indexable;

import java.io.IOException;

/**
 * Common interface to adapt the search engines
 */
public interface SearchEngine extends AutoCloseable {

	// void search();

	/**
	 * Inserts an entry into the specified index
	 * @param index	Where to insert the entry
	 * @param entry	Document to insert
	 * @throws IOException	when an error occurred with the Search Engine
	 */
	void index(String index, Indexable entry) throws IOException;

	// void bulk(collection);

	/**
	 * Retrieves the version of the cluster of the SearchEngine
	 * @return	Version number of the SearchEngine in use
	 * @throws IOException	to SearchEngine failures
	 */
	String getVersion() throws IOException;

}
