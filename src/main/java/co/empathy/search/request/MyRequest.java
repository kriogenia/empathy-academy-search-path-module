package co.empathy.search.request;

import javax.validation.constraints.NotNull;
import java.util.Map;

public interface MyRequest {

	/**
	 * @return  map with the specified queries
	 */
	@NotNull
	Map<String, String> queries();

}
