package co.empathy.index;

import java.util.Map;

/**
 * Objects that can be indexed into the search engines
 */
public interface Indexable {

	/**
	 * @return	the ID
	 */
	String getId();

	/**
	 * @return	the JSON map of the entry
	 */
	Map<String, Object> getJson();
}
