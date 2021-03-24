package co.empathy.index;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Map;

/**
 * Objects that can be indexed into the search engines
 */
public interface Indexable extends Serializable {

	/**
	 * @return	alphanumeric unique identifier of the entry
	 */
	@NotEmpty
	String getId();

	/**
	 * @return	the JSON map of the entry
	 */
	@NotNull
	Map<String, Object> toJsonMap();

}
