package co.empathy.search.request.aggregations;

import co.empathy.search.request.filters.RequestFilter;

import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseRequestAggregation implements RequestAggregation {

	@NotEmpty
	protected final String name;

	@NotEmpty
	protected final String field;

	protected boolean ascendant;

	@NotNull
	protected List<RequestFilter> filters;

	public BaseRequestAggregation(@NotEmpty String name, @NotEmpty String field) {
		if (name == null || name.isEmpty()) {
			throw new IllegalArgumentException("The aggregation name is required");
		}
		this.name = name;
		if (field == null || field.isEmpty()) {
			throw new IllegalArgumentException("The aggregation field is required");
		}
		this.field = field;
		this.ascendant = true;
		this.filters = new ArrayList<>();
	}

	@Override
	@NotEmpty
	public String getName() { return this.name; }

	@Override
	public boolean orderAscendant() {
		return ascendant;
	}

	@Override
	public void setAscendant(boolean ascendant) {
		this.ascendant = ascendant;
	}

	@Override
	@NotEmpty
	public String getField() {
		return this.field;
	}

	@Override
	@NotNull
	public RequestAggregation setFilters(List<RequestFilter> filters) {
		this.filters = filters.stream().filter(f -> !f.getField().equals(field)).collect(Collectors.toList());
		return this;
	}

	@Override
	@NotNull
	public List<RequestFilter> getFilters() {
		return filters;
	}
}
