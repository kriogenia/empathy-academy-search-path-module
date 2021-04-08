package co.empathy.engines;

import co.empathy.search.request.aggregations.DividedRangeAggregation;
import co.empathy.search.request.aggregations.RangeAggregation;
import co.empathy.search.request.aggregations.TermsAggregation;

import javax.validation.constraints.NotNull;

/**
 * Visitors that transform general aggregations to engine specific ones
 */
public interface AggregationVisitor {

	/**
	 * @param range     divided range aggregation to transform
	 * @return          engine specific multiple range
	 */
	@NotNull
	Object transform(@NotNull DividedRangeAggregation range);

	/**
	 * @param range     range aggregation to transform
	 * @return          engine specific range aggregation
	 */
	@NotNull
	Object transform(@NotNull RangeAggregation range);

	/**
	 * @param terms     terms aggregation to transform
	 * @return          engine specific terms aggregation
	 */
	@NotNull
	Object transform(@NotNull TermsAggregation terms);

}
