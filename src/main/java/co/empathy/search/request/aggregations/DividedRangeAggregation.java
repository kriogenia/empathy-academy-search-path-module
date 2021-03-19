package co.empathy.search.request.aggregations;

import co.empathy.engines.AggregationVisitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

public class DividedRangeAggregation extends BaseRequestAggregation {

	private final int from;
	private final int to;

	@Positive
	private final int gap;

	public DividedRangeAggregation(@NotEmpty String name, @NotEmpty String field,
	                               int from, int to, @Positive int gap) {
		super(name, field);
		if (from > to) {
			throw new IllegalArgumentException("Range start can't be after the end");
		}
		this.from = from;
		this.to = to;
		if (gap <= 0) {
			throw new IllegalArgumentException("The gap between ranges can't be negative");
		}
		this.gap = gap;
	}

	@Override
	public @NotNull Object accept(@NotNull AggregationVisitor visitor) {
		return visitor.transform(this);
	}

	/**
	 * @return  start of the range
	 */
	public int getFrom() {
		return from;
	}

	/**
	 * @return  end of the range
	 */
	public int getTo() {
		return to;
	}

	/**
	 * @return  ranges length
	 */
	@Positive
	public int getGap() {
		return gap;
	}
}
