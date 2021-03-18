package co.empathy.search.request.aggregations;

import co.empathy.engines.AggregationVisitor;

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
		this.gap = gap;
	}

	@Override
	public @NotNull Object accept(AggregationVisitor visitor) {
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
	public int getGap() {
		return gap;
	}
}
