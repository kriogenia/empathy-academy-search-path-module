package co.empathy.search.request;

import javax.validation.constraints.NotNull;
import java.util.Map;

public interface MyRequest {

	/**
	 * @return  map with the required queries
	 */
	@NotNull
	Map<String, String> musts();

	/**
	 * @return  map with the filtering queries
	 */
	@NotNull
	Map<String, String[]> filters();

}
