package co.empathy.search.request.filters;

import co.empathy.engines.FilterVisitor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TermsFilter implements RequestFilter {

	@NotEmpty
	private final String field;

	private String[] terms;

	public TermsFilter(@NotEmpty String field, @NotNull String terms) {
		this.field = field;
		this.terms = terms.split(",");
	}

	@Override
	public String getField() {
		return this.field;
	}

	/**
	 * @return  the list of terms to use in the filter
	 */
	public String[] getTerms() {
		return terms;
	}

	@Override
	public Object accept(FilterVisitor visitor) {
		return visitor.visit(this);
	}
}
