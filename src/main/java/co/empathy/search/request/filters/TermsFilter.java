package co.empathy.search.request.filters;

import co.empathy.engines.FilterVisitor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class TermsFilter extends BaseRequestFilter {

	@NotNull
	private final String[] terms;

	public TermsFilter(@NotEmpty String field, @NotNull String terms) {
		super(field);
		this.terms = terms.split(",");
	}

	/**
	 * @return  the list of terms to use in the filter
	 */
	@NotNull
	public String[] getTerms() {
		return terms;
	}

	@Override
	@NotNull
	public Object accept(FilterVisitor visitor) {
		return visitor.transform(this);
	}
}
