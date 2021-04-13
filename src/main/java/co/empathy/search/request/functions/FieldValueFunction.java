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

	public FieldValueFunction(@NotEmpty String field, @Nullable Float factor,
	                          @Nullable Modifier modifier, @Nullable Float missing) {
		this(field);
		this.factor = factor;
		this.modifier = modifier;
		this.missing = missing;
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
	 * @return          modified object
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
	 * @return          modified object
	 */
	@NotNull
	public FieldValueFunction setModifier(@NotNull Modifier modifier) {
		this.modifier = modifier;
		return this;
	}

	/**
	 * @return  value to apply when a document misses the specified field
	 */
	@Nullable
	public Float getMissing() {
		return missing;
	}

	/**
	 * @param missing   new value to apply to documents with the field missing
	 * @return          modified object
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
