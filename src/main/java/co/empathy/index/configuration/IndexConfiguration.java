package co.empathy.index.configuration;

/**
 * Classes holding all the needed configuration to create an index
 */
public interface IndexConfiguration {

	/**
	 * @return	key of the index
	 */
	String getKey();

	/**
	 * @return	the path of the file to index
	 */
	String getFilePath();

	/**
 	 * @return	size for the bulk requests
	 */
	int getBulkSize();

	/**
	 * @return	number of bulks to index
	 */
	int getTotalBulks();

}
