package co.empathy.search.beans;

import javax.validation.constraints.NotNull;
import java.util.Map;

public interface MyRequest {

	/**
	 * @return  base query of the search
	 */
	@NotNull
	String getQuery();

	/**
	 * @return  map with the specified buckets
	 */
	@NotNull
	Map<String, String[]> getBuckets();
}
