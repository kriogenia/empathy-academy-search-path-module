package co.empathy.search.request.filters;

import javax.validation.constraints.NotEmpty;

public abstract class BaseRequestFilter implements RequestFilter {

	@NotEmpty
	protected String field;

	public BaseRequestFilter(@NotEmpty String field) {
		this.field = field;
	}

	@NotEmpty
	@Override
	public String getField() {
		return this.field;
	}

}