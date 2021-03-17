package co.empathy.engines;

import co.empathy.search.request.filters.TermsFilter;

public interface FilterVisitor {

	Object visit(TermsFilter filter);

}
