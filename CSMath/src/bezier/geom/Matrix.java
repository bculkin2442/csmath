package bezier.geom;

public class Matrix {
	public final double[][] mat;

	public Matrix(double[][] mat) {
		this.mat = mat;
	}
	
	public Matrix add(Matrix m) {
		return add(m.mat);
	}
	
	public double[] scalarMultiply(double[] vec) {
		double[] ret = new double[vec.length];
		
		for(int i = 0; i < mat[0].length; i++) {
			for(int j = 0; j < mat.length; j++) {
				ret[i] += mat[i][j] * vec[j];
			}
		}
		
		return ret;
	}
	
	public Matrix add(double[][] matr) {
		double[][] ret = new double[mat.length][mat[0].length];
		
		for(int i = 0; i < mat.length; i++) {
			for(int j = 0; j < mat[0].length; j++) {
				ret[i][j] = mat[i][j] + matr[i][j];
			}
		}
		
		return new Matrix(ret);
	}
	
	public Matrix multiply(Matrix m) {
		return multiply(m.mat);
	}
	
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
