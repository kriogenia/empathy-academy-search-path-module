package co.empathy.search.request.aggregations;

import co.empathy.engines.AggregationVisitor;

import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class RangeAggregation extends BaseRequestAggregation {

	@Nullable
	private final Integer from;

	@Nullable
	private final Integer to;

	public RangeAggregation(@NotEmpty String name, @NotEmpty String field,
	                        @Nullable Integer from, @Nullable Integer to) {
		super(name, field);
		if (from == null && to == null) {
			throw new IllegalArgumentException("Ranges must have two edges");
		}
		this.from = from;
		this.to = to;
	}

	@Override
	public @NotNull Object accept(@NotNull AggregationVisitor visitor) {
		return visitor.transform(this);
	}

	/**
	 * @return  start of the range
	 */
	@Nullable
	public Integer getFrom() {
		return from;
	}

	/**
	 * @return  end of the range
	 */
	@Nullable
	public Integer getTo() {
		return to;
	}

}
