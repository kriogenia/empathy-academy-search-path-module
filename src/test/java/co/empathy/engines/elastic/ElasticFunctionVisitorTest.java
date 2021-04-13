package co.empathy.engines.elastic;

import co.empathy.search.request.functions.FieldValueFunction;
import co.empathy.search.request.functions.GaussDecayFunction;
import co.empathy.search.request.functions.TermWeightingFunction;
import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.functionscore.FieldValueFactorFunctionBuilder;
import org.elasticsearch.index.query.functionscore.FunctionScoreQueryBuilder;
import org.elasticsearch.index.query.functionscore.GaussDecayFunctionBuilder;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest
public class ElasticFunctionVisitorTest {

	@Inject
	ElasticFunctionVisitor visitor;

	/**
	 * Test the correct transformation of the function to the ES equivalent
	 */
	@Test
	public void testTermWeightingFunction() {
		var function = new TermWeightingFunction("field", "text", 1.0f);
		var transformed = (FunctionScoreQueryBuilder.FilterFunctionBuilder) function.accept(visitor);
		// Check weight
		assertEquals(1.0f, transformed.getScoreFunction().getWeight());
		// And query filter
		var filter = (MatchQueryBuilder) transformed.getFilter();
		assertEquals("field", filter.fieldName());
		assertEquals("text", filter.value());
	}

	/**
	 * Test the correct transformation of the function to the ES equivalent
	 */
	@Test
	public void testFieldValueFunction() {
		// Partial
		var partial = new FieldValueFunction("partial").setFactor(1.0f);
		var pTransformed = (FunctionScoreQueryBuilder.FilterFunctionBuilder) partial.accept(visitor);
		var pFunction = (FieldValueFactorFunctionBuilder) pTransformed.getScoreFunction();
		assertEquals("partial", pFunction.fieldName());
		assertEquals(1.0f, pFunction.factor());
		assertEquals("none", pFunction.modifier().toString());
		assertNull(pFunction.missing());
		// Complete
		var complete = new FieldValueFunction("complete", 0.1f,
				FieldValueFunction.Modifier.LOG1P, 0.5f);
		var cTransformed = (FunctionScoreQueryBuilder.FilterFunctionBuilder) complete.accept(visitor);
		var cFunction = (FieldValueFactorFunctionBuilder) cTransformed.getScoreFunction();
		assertEquals("complete", cFunction.fieldName());
		assertEquals(0.1f, cFunction.factor());
		assertEquals("log1p", cFunction.modifier().toString());
		assertEquals(0.5f, cFunction.missing());
	}

	/**
	 * Test the correct transformation of the function to the ES equivalent
	 */
	@Test
	public void testGaussDecayFunction() {
		var partial = new GaussDecayFunction("field", "origin", "scale", 0.1f);
		var pTransformed = (FunctionScoreQueryBuilder.FilterFunctionBuilder) partial.accept(visitor);
		assertTrue(pTransformed.getScoreFunction() instanceof GaussDecayFunctionBuilder);
		var pFunction = (GaussDecayFunctionBuilder) pTransformed.getScoreFunction();
		assertEquals("field", pFunction.getFieldName());
	}

}
