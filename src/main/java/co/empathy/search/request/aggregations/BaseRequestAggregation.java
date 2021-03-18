package co.empathy.search.request.aggregations;

import javax.validation.constraints.NotEmpty;

public abstract class BaseRequestAggregation implements RequestAggregation {

	@NotEmpty
	protected final String name;

	@NotEmpty
	protected final String field;

	public BaseRequestAggregation(@NotEmpty String name, @NotEmpty String field) {
		this.name = name;
		this.field = field;
	}

	@NotEmpty
	@Override
	public String getName() { return this.name; }

	@NotEmpty
	@Override
	public String getField() {
		return this.field;
	}
}
