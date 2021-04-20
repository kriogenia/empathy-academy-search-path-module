package co.empathy.search.request.suggestions;

import javax.validation.constraints.NotEmpty;

public class TermsSuggestion implements RequestSuggestion {

	@NotEmpty(message = "The field to search for the suggestion is mandatory")
	private final String field;

	@NotEmpty(message = "The text to generate the suggestions for is mandatory")
	private final String text;

	public TermsSuggestion(@NotEmpty String field, @NotEmpty String text) {
		if (field == null || field.isEmpty()) {
			throw new IllegalArgumentException("Field to suggest can't be empty");
		}
		this.field = field;
		if (text == null || text.isEmpty()) {
			throw new IllegalArgumentException("Text to suggest can't be empty");
		}
		this.text = text;
	}

	@Override
	@NotEmpty
	public String getField() {
		return field;
	}

	@Override
	@NotEmpty
	public String getText() {
		return text;
	}
}
