package co.empathy.search.request.aggregations;

import co.empathy.engines.AggregationVisitor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TermsAggregation extends BaseRequestAggregation {

	public TermsAggregation(@NotEmpty String name, @NotEmpty String field) {
		super(name, field);
	}

	@Override
	@NotNull
	public Object accept(AggregationVisitor visitor) {
		return visitor.transform(this);
	}
}
