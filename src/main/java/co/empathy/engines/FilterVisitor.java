package co.empathy.engines;

import co.empathy.search.request.filters.RangeFilter;
import co.empathy.search.request.filters.TermsFilter;

/**
 * Visitors that transform general filters to engine specific ones
 */
public interface FilterVisitor {

	/**
	 * @param filter    range filter to transform
	 * @return          engine specific range filter
	 */
	Object transform(RangeFilter filter);

	/**
	 *
	 * @param filter    terms filter to transform
	 * @return          engine specific range filter
	 */
	Object transform(TermsFilter filter);

}
