package bezier.geom;

/**
 * Matrix class.
 * 
 * @author bjculkin
 *
 */
public class Matrix {
	/**
	 * Matrix values.
	 */
	public final double[][] mat;

	/**
	 * Create a new matrix, with the specified values.
	 * 
	 * @param mat
	 *                The matrix to use.
	 */
	public Matrix(double[][] mat) {
		this.mat = mat;
	}

	/**
	 * Add two matrices together.
	 * 
	 * @param m
	 *                The matrix to add.
	 * @return An added matrix.
	 */
	public Matrix add(Matrix m) {
		return add(m.mat);
	}

	/**
	 * Multiply this matrix by a scalar vector.
	 * 
	 * @param vec
	 *                The vector to multiply by.
	 * @return The resulting vector.
	 */
	public double[] scalarMultiply(double[] vec) {
		double[] ret = new double[vec.length];

		for (int i = 0; i < mat[0].length; i++) {
			for (int j = 0; j < mat.length; j++) {
				ret[i] += mat[i][j] * vec[j];
			}
		}

		return ret;
	}

	/**
	 * Add a matrix to this one.
	 * 
	 * @param matr
	 *                The matrix to add.
	 * @return The resulting matrix.
	 */
	public Matrix add(double[][] matr) {
		double[][] ret = new double[mat.length][mat[0].length];

		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				ret[i][j] = mat[i][j] + matr[i][j];
			}
		}

		return new Matrix(ret);
	}

	/**
	 * Multiply this matrix by another.
	 * 
	 * @param m
	 *                The matrix to multiply by
	 * @return The multiplied matrix.
	 */
	public Matrix multiply(Matrix m) {
		return multiply(m.mat);
	}

	/**
	 * Multiply this matrix by another.
	 * 
	 * @param matr
	 *                The matrix to multiply by
	 * @return The multiplied matrix.
	 */
	public Matrix multiply(double[][] matr) {
		double[][] ret = new double[mat.length][matr[0].length];

		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < matr[0].length; j++) {
				for (int k = 0; k < matr.length; k++) {
					ret[i][j] += mat[i][k] * matr[k][j];
				}
			}
		}

		return new Matrix(ret);
	}
}
