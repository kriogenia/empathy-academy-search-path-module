package co.empathy.search.request.suggestions;

import javax.validation.constraints.NotEmpty;

public interface RequestSuggestion {

	/**
	 * @return  field to search for the suggestions
	 */
	@NotEmpty
	String getField();

	/**
	 * @return  text to generate the suggestion
	 */
	@NotEmpty
	String getText();

}
