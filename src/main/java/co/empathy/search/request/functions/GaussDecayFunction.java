package co.empathy.search.request.functions;

import co.empathy.engines.FunctionVisitor;

import javax.annotation.CheckForNull;
import javax.annotation.Nullable;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

public class GaussDecayFunction extends BaseRequestFunction {

	@NotEmpty
	private final String origin;

	@NotEmpty
	private final String scale;

	@PositiveOrZero
	@Max(1L)
	private final double decay;

	@CheckForNull
	private String offset;

	public GaussDecayFunction(@NotEmpty String field, @NotEmpty String origin,
	                          @NotEmpty String scale, @PositiveOrZero @Max(1L) double decay) {
		super(field);
		this.origin = origin;
		this.scale = scale;
		this.decay = decay;
	}

	public GaussDecayFunction(@NotEmpty String field, @NotEmpty String origin, @NotEmpty String scale,
	                          @PositiveOrZero @Max(1L) double decay, @Nullable String offset) {
		this(field, origin, scale, decay);
		this.offset = offset;
	}

	/**
	 * @return  middle point of the bell
	 */
	@NotEmpty
	public String getOrigin() {
		return origin;
	}

	/**
	 * @return  distance from origin where the decay matches
	 */
	@NotEmpty
	public String getScale() {
		return scale;
	}

	/**
	 * @return  y value on the point at the distance of the scale
	 */
	@PositiveOrZero
	public double getDecay() {
		return decay;
	}

	/**
	 * @return  offset applied to the bell
	 */
	@CheckForNull
	public String getOffset() {
		return offset;
	}

	/**
	 * @param offset    new offset around the origin applied to the bell
	 * @return          modified object
	 */
	public GaussDecayFunction setOffset(@Nullable String offset) {
		this.offset = offset;
		return this;
	}

	@Override
	public @NotNull Object accept(@NotNull FunctionVisitor visitor) {
		return visitor.transform(this);
	}
}
