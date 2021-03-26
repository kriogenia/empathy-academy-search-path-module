package co.empathy.search.request.functions;

import co.empathy.engines.AggregationVisitor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

/**
 * Scoring weighting function to term queries
 */
public class TermWeightingFunction extends BaseRequestFunction {

	@NotEmpty(message = "The term weighting needs a term to filter")
	private final String text;

	@PositiveOrZero(message = "The weight can't be negative")
	private final float weight;

	public TermWeightingFunction(@NotEmpty String field, @NotEmpty String text,
	                             @PositiveOrZero float weight) {
		super(field);
		this.text = text;
		this.weight = weight;
	}

	/**
	 * @return  text applied to the query
	 */
	public String getText() {
		return text;
	}

	@Override
	@NotNull
	public @NotNull Object accept(@NotEmpty AggregationVisitor visitor) {
		return null;
	}
}
