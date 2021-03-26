package co.empathy.search.request.functions;

import javax.validation.constraints.NotEmpty;

public abstract class BaseRequestFunction implements RequestFunction {

	@NotEmpty(message = "Functions needs a field to work with")
	protected String field;

	public BaseRequestFunction(@NotEmpty String field) {
		if (field == null || field.isEmpty()) {
			throw new IllegalArgumentException("Functions needs a field to work with");
		}
		this.field = field;
	}

	@NotEmpty
	@Override
	public String getField() {
		return this.field;
	}

}
