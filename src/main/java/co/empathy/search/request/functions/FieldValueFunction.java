package co.empathy.search.request.functions;

import co.empathy.engines.FunctionVisitor;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import org.elasticsearch.common.lucene.search.function.FieldValueFactorFunction;

/**
 * Scoring weighting function to term queries
 */
public class FieldValueFunction extends BaseRequestFunction {

	@CheckForNull
	private Float factor;

	@Nullable
	private Modifier modifier;

	@Nullable
	private Float missing;

	public FieldValueFunction(@NotEmpty String field) {
		super(field);
	}

	/**
	 * @return  factor to multiply the score
	 */
	@CheckForNull
	public Float getFactor() {
		return factor;
	}

	/**
	 * @param factor    value to multiply the score
	 */
	@NotNull
	public FieldValueFunction setFactor(@PositiveOrZero Float factor) {
		this.factor = factor;
		return this;
	}

	/**
	 * @return  function to modify the result if it exists
	 */
	@CheckForNull
	public Modifier getModifier() {
		return modifier;
	}

	/**
	 * @param modifier   new function to modify the result
	 */
	@NotNull
	public FieldValueFunction setModifer(@NotNull Modifier modifier) {
		this.modifier = modifier;
		return this;
	}

	/**
	 * @return  value to apply when a document misses the specified field
	 */
	@CheckForNull
	public Float getMissing() {
		return missing;
	}

	/**
	 * @param missing   new value to apply to documents with the field missing
	 */
	@NotNull
	public FieldValueFunction setMissing(@PositiveOrZero Float missing) {
		this.missing = missing;
		return this;
	}

	@Override
	@NotNull
	public @NotNull Object accept(@NotEmpty FunctionVisitor visitor) {
		return visitor.transform(this);
	}

	public enum Modifier {
		LOG1P(FieldValueFactorFunction.Modifier.LOG1P),
		SQUARE(FieldValueFactorFunction.Modifier.SQUARE);

		private final FieldValueFactorFunction.Modifier elasticSearch;

		Modifier(FieldValueFactorFunction.Modifier elasticSearch) {
			this.elasticSearch = elasticSearch;
		}

		/**
		 * @return	string key of the item
		 */
		public FieldValueFactorFunction.Modifier elasticSearch() {
			return elasticSearch;
		}

	}


}
