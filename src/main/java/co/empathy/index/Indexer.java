package co.empathy.index;

import java.io.IOException;

/**
 * Objects that can create an index in the Search Engines
 */
public interface Indexer {

	/**
	 * Reads and inserts the content of the file into an index
	 * @param filePath	path of the file to index
	 * @throws IOException	if an I/O error occurs opening the file
	 */
	void indexFile(String filePath) throws IOException;

	/**
	 * Reads and inserts the content of the file into an index
	 * through Bulk Requests
	 * @param filePath	path of the file to index
	 * @throws IOException	if an I/O error occurs opening the file
	 */
	void bulkIndexFile(String filePath) throws IOException;

}
