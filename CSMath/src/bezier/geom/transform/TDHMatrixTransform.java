package bezier.geom.transform;

import bezier.geom.Matrix;
import bezier.geom.TDHPoint;

/**
 * 2D homogenous transform defined by a matrix.
 * 
 * @author bjculkin
 *
 */
public class TDHMatrixTransform implements TDHTransform {
	/**
	 * The matrix for the transform.
	 */
	public final Matrix mat;

	/**
	 * Create a new matrix-based transform.
	 * 
	 * @param mat
	 *                The matrix that defines the transform.
	 */
	public TDHMatrixTransform(Matrix mat) {
		this.mat = mat;
	}

	/**
	 * Create a new matrix-based transform.
	 * 
	 * @param mat
	 *                The matrix that defines the transform.
	 */
	public TDHMatrixTransform(double[][] mat) {
		this.mat = new Matrix(mat);
	}

	@Override
	public TDHTransformType type() {
		return TDHTransformType.MATRIX;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		double[] mult = mat.scalarMultiply(new double[] { punkt.x, punkt.y, punkt.z });

		return new TDHPoint(mult[0], mult[1], mult[2]);
	}

	/**
	 * Chain two matrix transforms together.
	 * 
	 * @param trans
	 *                The next transform to use.
	 * @return A transform that represents the two transforms in serial.
	 */
	public TDHTransform then(TDHMatrixTransform trans) {
		return new TDHMatrixTransform(mat.multiply(trans.mat));
	}
}