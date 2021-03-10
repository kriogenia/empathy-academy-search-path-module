package co.empathy.engines;

/**
 * Enumerated list of search engines
 */
public enum EEngine {
	ELASTIC_SEARCH("es"),
	TEST("test");

	private final String key;

	EEngine(String key) {
		this.key = key;
	}

	/**
	 * @return	string key of the item
	 */
	public String getKey() {
		return key;
	}
}
