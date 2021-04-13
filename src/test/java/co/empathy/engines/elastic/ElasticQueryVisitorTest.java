package co.empathy.engines.elastic;

import co.empathy.search.request.queries.DisjunctionMaxQuery;
import co.empathy.search.request.queries.PartialPlusPerfectQuery;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.DisMaxQueryBuilder;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class ElasticQueryVisitorTest {

	@Inject
	ElasticQueryVisitor visitor;

	@Inject
	ObjectMapper mapper;

	/**
	 * Test the correct transformation of DisjunctionMaxQueries using only those
	 * @throws JsonProcessingException  if some mapping error occurs
	 */
	@Test
	public void testDisjunctionMaxQuery() throws JsonProcessingException {
		var dmQuery = new DisjunctionMaxQuery();
		dmQuery.add(new DisjunctionMaxQuery());
		dmQuery.add(new DisjunctionMaxQuery());
		var transformed = (DisMaxQueryBuilder) dmQuery.accept(visitor);
		// Check we got a dis_max query
		var map = mapper.readValue(transformed.toString(),
				new TypeReference<Map<String, Map<String, Object>>>() {});
		var internal = map.get("dis_max");
		assertNotNull(internal);
		// And check it contains two nested dis_max queries
		assertTrue(internal.get("queries") instanceof ArrayList);
		var queries = (ArrayList<?>) internal.get("queries");
		assertEquals(2, queries.size());
		for (var query: queries) {
			assertNotNull(((Map<?,?>) query).get("dis_max"));
		}
	}

	/**
	 * Test the correct transformation of this queries to ElasticSearch equivalent
	 * @throws JsonProcessingException  if some mapping error occurs
	 */
	@Test
	public void testPartialPlusPerfectQuery() throws JsonProcessingException {
		var query = new PartialPlusPerfectQuery("field", "text");
		var transformed = (BoolQueryBuilder) query.accept(visitor);
		// Check we got a bool query
		var map = mapper.readValue(transformed.toString(),
				new TypeReference<Map<String, Map<String, Object>>>() {});
		var internal = map.get("bool");
		assertNotNull(internal);
		// And check it contains two nested should queries
		assertTrue(internal.get("should") instanceof ArrayList);
		var shouldList = (ArrayList<?>) internal.get("should");
		assertEquals(2, shouldList.size());
		// First should be a match with the partial
		var partialMatch = ((Map<?,?>) shouldList.get(0)).get("match");
		assertNotNull(((Map<?,?>) partialMatch).get("field.partial"));
		// And the other should be a match phrase with the perfect
		var perfectMatch = ((Map<?,?>) shouldList.get(1)).get("match_phrase");
		assertNotNull(((Map<?,?>) perfectMatch).get("field.perfect"));
	}

	/**
	 * DisjunctionMaxQuery should work correctly while holding other types of queries
	 * @throws JsonProcessingException  if some mapping error occurs
	 */
	@Test
	public void testCombined() throws JsonProcessingException {
		var dmQuery = new DisjunctionMaxQuery();
		dmQuery.add(new PartialPlusPerfectQuery("field", "text"));
		var transformed = (DisMaxQueryBuilder) dmQuery.accept(visitor);
		// Check we got a dis_max query
		var map = mapper.readValue(transformed.toString(),
				new TypeReference<Map<String, Map<String, Object>>>() {});
		var internal = map.get("dis_max");
		// And check it contains the nested queries
		assertTrue(internal.get("queries") instanceof ArrayList);
		var queries = (ArrayList<?>) internal.get("queries");
		assertEquals(1, queries.size());
		// PartialPlusPerfectQuery -> bool query
		assertNotNull(((Map<?,?>) queries.get(0)).get("bool"));
	}

}
