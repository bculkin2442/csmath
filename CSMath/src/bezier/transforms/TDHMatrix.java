package bezier.transforms;

import bezier.TDHPoint;

public class TDHMatrix implements TDHTransform {
	public final double[][] mat;

	public TDHMatrix(double[][] mat) {
		super();
		this.mat = mat;
	}

	@Override
	public TDHTransformType type() {
		return TDHTransformType.MATRIX;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		double x = (punkt.x * mat[0][0]) + (punkt.y * mat[1][0]) + (punkt.z * mat[2][0]);
		double y = (punkt.x * mat[0][1]) + (punkt.y * mat[1][1]) + (punkt.z * mat[2][1]);
		double z = (punkt.x * mat[0][2]) + (punkt.y * mat[1][2]) + (punkt.z * mat[2][2]);

		return new TDHPoint(x, y, z);
	}

	public TDHTransform then(double[][] matr) {
		double[][] ret = new double[3][3];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					ret[i][j] += mat[i][k] * matr[k][j];
				}
			}
		}

		return new TDHMatrix(ret);
	}
}