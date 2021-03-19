package co.empathy.search.request.aggregations;

import javax.validation.constraints.NotEmpty;

public abstract class BaseRequestAggregation implements RequestAggregation {

	@NotEmpty
	protected final String name;

	@NotEmpty
	protected final String field;

	public BaseRequestAggregation(@NotEmpty String name, @NotEmpty String field) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("The aggregation name is required");
		}
		this.name = name;
		if (field == null || field.isEmpty()) {
			throw new IllegalArgumentException("The aggregation field is required");
		}
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
