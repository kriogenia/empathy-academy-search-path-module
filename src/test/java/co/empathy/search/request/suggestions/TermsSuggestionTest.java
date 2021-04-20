package co.empathy.search.request.suggestions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TermsSuggestionTest {

	/**
	 * Test the exception for invalid field
	 */
	@Test
	public void testInvalidField() {
		// field null
		var exception = assertThrows(IllegalArgumentException.class,
				() -> new TermsSuggestion(null, "text"));
		assertEquals("Field to suggest can't be empty", exception.getMessage());
		// field empty
		exception = assertThrows(IllegalArgumentException.class,
				() -> new TermsSuggestion("", "text"));
		assertEquals("Field to suggest can't be empty", exception.getMessage());
		// valid field
		var suggestion = new TermsSuggestion("field", "text");
		assertEquals("field", suggestion.getField());
	}

	@Test
	public void testInvalidText() {
		// field null
		var exception = assertThrows(IllegalArgumentException.class,
				() -> new TermsSuggestion("field", null));
		assertEquals("Text to suggest can't be empty", exception.getMessage());
		// field empty
		exception = assertThrows(IllegalArgumentException.class,
				() -> new TermsSuggestion("field", null));
		assertEquals("Text to suggest can't be empty", exception.getMessage());
		// valid field
		var suggestion = new TermsSuggestion("field", "text");
		assertEquals("text", suggestion.getText());
	}
}
