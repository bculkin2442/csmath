package bezier.geom.transform;

/**
 * Types of transform to apply to TDHPoints.
 * 
 * @author bjculkin
 *
 */
public enum TDHTransformType {
	/**
	 * Coordinate translation.
	 */
	TRANSLATE,
	/**
	 * Do nothing transform.
	 */
	IDENTITY,
	/**
	 * Coordinate scaling.
	 */
	SCALE,
	/**
	 * Coordinate rotation.
	 */
	ROTATION,
	/**
	 * Coordinate reflection.
	 */
	REFLECTION,
	/**
	 * Coordinate shearing.
	 */
	SHEAR,
	/**
	 * Multiple transformations.
	 */
	COMBINATION,
	/**
	 * Arbitrary matrix transformation.
	 */
	MATRIX
}