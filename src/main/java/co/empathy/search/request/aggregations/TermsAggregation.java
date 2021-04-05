package co.empathy.search.request.aggregations;

import co.empathy.engines.AggregationVisitor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class TermsAggregation extends BaseRequestAggregation {

	@PositiveOrZero(message = "The size can't be less than zero")
	private int size;

	public TermsAggregation(@NotEmpty String name, @NotEmpty String field) {
		super(name, field);
		this.size = 10;
	}

	/**
	 * @return  the maximum size of buckets to return
	 */
	public int getSize() {
		return size;
	}

	/**
	 * @param size  new maximum number of buckets to return
	 * @return      modified item
	 */
	public TermsAggregation setSize(int size) {
		this.size = size;
		return this;
	}

	@Override
	@NotNull
	public Object accept(@NotNull AggregationVisitor visitor) {
		return visitor.transform(this);
	}
}
