package bezier.transforms;

import bezier.TDHPoint;

/**
 * Transformation applicable to TDHPoints.
 * 
 * @author bjculkin
 *
 */
@FunctionalInterface
public interface TDHTransform {
	/**
	 * Get the type of this transform.
	 * 
	 * Unknown transformations are assumed to be identity transforms.
	 * 
	 * @return The type of this transform.
	 */
	default TDHTransformType type() {
		return TDHTransformType.IDENTITY;
	}

	/**
	 * Get the matrix representation of the transform.
	 * 
	 * Unknown transformations are assumed to be identity transforms.
	 * 
	 * @return The matrix representation of the transform.
	 */
	default double[][] matrix() {
		return new double[][] { new double[] { 1, 0, 0 }, new double[] { 0, 1, 0 }, new double[] { 0, 0, 1 } };
	}

	/**
	 * Get the inverse of the transform.
	 * 
	 * Unknown transformations are assumed to be identity transforms.
	 * 
	 * @return The inverse the transform.
	 */
	default TDHTransform invert() {
		return new TDHIdentity();
	}

	/**
	 * Apply the transform to a point.
	 * 
	 * @param punkt
	 *            The point to transform.
	 * @return A transformed version of the point.
	 */
	TDHPoint transform(TDHPoint punkt);
}