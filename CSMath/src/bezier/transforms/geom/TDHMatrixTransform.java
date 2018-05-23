package bezier.transforms.geom;

import bezier.geom.Matrix;
import bezier.geom.TDHPoint;

public class TDHMatrixTransform implements TDHTransform {
	public final Matrix mat;

	public TDHMatrixTransform(Matrix mat) {
		this.mat = mat;
	}

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

	public TDHTransform then(TDHMatrixTransform trans) {
		return new TDHMatrixTransform(mat.multiply(trans.mat));
	}
}