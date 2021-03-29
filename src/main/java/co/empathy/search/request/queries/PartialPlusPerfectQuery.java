package co.empathy.search.request.queries;

import co.empathy.engines.QueryVisitor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class PartialPlusPerfectQuery implements RequestQuery {

	@NotEmpty(message = "The field to query is needed to partial and perfect queries")
	private String field;

	@NotNull(message = "The query string is necessary, use empty string to make empty searches")
	private String query;

	public PartialPlusPerfectQuery(@NotEmpty String field, @NotNull String query) {
		this.field = field;
		this.query = query;
	}

	@Override
	public @NotNull Object accept(@NotNull QueryVisitor visitor) {
		return visitor.transform(this);
	}
}
