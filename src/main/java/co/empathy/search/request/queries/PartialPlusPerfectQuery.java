package co.empathy.search.request.queries;

import co.empathy.engines.QueryVisitor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PartialPlusPerfectQuery implements RequestQuery {

	@NotEmpty(message = "The field to query is needed to partial and perfect queries")
	private final String field;

	@NotNull(message = "The text string is necessary, use empty string to make empty searches")
	private final String text;

	public PartialPlusPerfectQuery(@NotEmpty String field, @NotNull String text) {
		this.field = field;
		this.text = text;
	}

	/**
	 * @return  field where the query will search
	 */
	public String getField() {
		return field;
	}

	/**
	 * @return  string query to search
	 */
	public String getText() {
		return text;
	}

	@Override
	public @NotNull Object accept(@NotNull QueryVisitor visitor) {
		return visitor.transform(this);
	}
}
