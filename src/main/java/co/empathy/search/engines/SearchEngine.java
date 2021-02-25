package co.empathy.search.engines;

import java.io.IOException;

/**
 * Common interface to adapt the search engines
 */
public interface SearchEngine {

	// void search();

	/**
	 * Retrieves the version of the cluster of the SearchEngine
	 * @return	Version number of the SearchEngine in use
	 * @throws IOException	to SearchEngine failures
	 */
	String getVersion() throws IOException;

}
