package co.empathy.engines;

import co.empathy.search.request.filters.DateRangesFilter;
import co.empathy.search.request.filters.TermsFilter;

import javax.validation.constraints.NotNull;

/**
 * Visitors that transform general filters to engine specific ones
 */
public interface FilterVisitor {

	/**
	 * @param filter    range filter to transform
	 * @return          engine specific range filter
	 */
	@NotNull
	Object transform(@NotNull DateRangesFilter filter);

	/**
	 *
	 * @param filter    terms filter to transform
	 * @return          engine specific range filter
	 */
	@NotNull
	Object transform(@NotNull TermsFilter filter);

}
