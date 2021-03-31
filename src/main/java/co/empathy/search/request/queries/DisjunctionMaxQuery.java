package co.empathy.search.request.queries;

import co.empathy.engines.QueryVisitor;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class DisjunctionMaxQuery implements RequestQuery {

	@NotNull
	private final List<RequestQuery> queries;

	public DisjunctionMaxQuery() {
		queries = new ArrayList<>();
	}

	/**
	 * Adds the query to the disjunction
	 * @param query query to add
	 */
	public void add(RequestQuery query){
		queries.add(query);
	}

	/**
	 * @return  queries of the disjunction
	 */
	public List<RequestQuery> getQueries() {
		return queries;
	}

	@Override
	public @NotNull Object accept(@NotNull QueryVisitor visitor) {
		return visitor.transform(this);
	}

}
