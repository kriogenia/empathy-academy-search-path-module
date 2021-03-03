package co.empathy.index;

import java.io.Serializable;
import java.util.Map;

/**
 * Objects that can be indexed into the search engines
 */
public interface Indexable extends Serializable {

	/**
	 * @return	the ID
	 */
	String getId();

	/**
	 * @return	the JSON map of the entry
	 */
	Map<String, Object> getJsonMap();

}
