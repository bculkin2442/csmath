package bezier.transforms;

import bezier.geom.TDHPoint;

public class TDHPointRotation extends TDHRotation {
	public final double x0;
	public final double y0;

	public TDHPointRotation(double theta, double x0, double y0) {
		super(theta);

		this.x0 = x0;
		this.y0 = y0;
	}

	@Override
	public TDHTransformType type() {
		return TDHTransformType.ROTATION;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		double x1 = (punkt.x * Math.cos(theta)) - (punkt.y * Math.sin(theta));
		double y1 = (punkt.x * Math.sin(theta)) - (punkt.y * Math.cos(theta));

		double x2 = (-x0 * Math.cos(theta)) + (y0 * Math.sin(theta)) + x0;
		double y2 = (-x0 * Math.sin(theta)) - (y0 * Math.cos(theta)) + y0;

		double x = x1 + (punkt.z * x2);
		double y = y1 + (punkt.z * y2);

		return new TDHPoint(x, y, punkt.z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(x0);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y0);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TDHPointRotation other = (TDHPointRotation) obj;
		if (Double.doubleToLongBits(x0) != Double.doubleToLongBits(other.x0))
			return false;
		if (Double.doubleToLongBits(y0) != Double.doubleToLongBits(other.y0))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDHPointRotation [x0=" + x0 + ", y0=" + y0 + "]";
	}
}