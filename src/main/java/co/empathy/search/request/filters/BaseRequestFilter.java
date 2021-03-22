package co.empathy.search.request.filters;

import javax.validation.constraints.NotEmpty;

public abstract class BaseRequestFilter implements RequestFilter {

	@NotEmpty(message = "Filtering needs the field to filter")
	protected String field;

	public BaseRequestFilter(@NotEmpty String field) {
		if (field == null || field.isEmpty()) {
			throw new IllegalArgumentException("Filtering needs the field to filter");
		}
		this.field = field;
	}

	@NotEmpty
	@Override
	public String getField() {
		return this.field;
	}

}