package co.empathy.engines;

import co.empathy.search.request.aggregations.TermsAggregation;

/**
 * Visitors that transform general aggregations to engine specific ones
 */
public interface AggregationVisitor {

	/**
	 * @param terms     terms aggregation to transform
	 * @return          engine specific terms aggregation
	 */
	Object transform(TermsAggregation terms);

}
