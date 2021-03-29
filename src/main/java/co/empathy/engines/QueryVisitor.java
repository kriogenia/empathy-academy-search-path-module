package co.empathy.engines;

import co.empathy.search.request.queries.PartialPlusPerfectQuery;

import javax.validation.constraints.NotNull;

public interface QueryVisitor {

	@NotNull
	Object transform(PartialPlusPerfectQuery query);

}
