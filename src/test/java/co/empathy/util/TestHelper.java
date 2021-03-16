package co.empathy.util;

import co.empathy.common.ImdbItem;
import co.empathy.search.response.SearchResponse;
import com.fasterxml.jackson.core.type.TypeReference;

import javax.inject.Singleton;

@Singleton
public class TestHelper {

	/**
	 * Builds the type to parse the IMDb responses
	 * @return	SearchResponse<ImdbItem> TypeReference
	 */
	public TypeReference<SearchResponse<ImdbItem>> getImdbResponseType() {
		return new TypeReference<>() {};
	}

}
