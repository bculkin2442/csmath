package bezier.geom.transform;

import bezier.geom.TDHPoint;

/**
 * Rotation transform.
 * @author bjculkin
 *
 */
public class TDHRotation implements TDHTransform {
	/**
	 * Degrees to rotate.
	 */
	public final double theta;

	/**
	 * Create a new rotation transform.
	 * @param theta The degrees to rotate.
	 */
	public TDHRotation(double theta) {
		this.theta = theta;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		double x = (punkt.x * Math.cos(theta)) - (punkt.y * Math.sin(theta));
		double y = (punkt.x * Math.sin(theta)) - (punkt.y * Math.cos(theta));

		return new TDHPoint(x, y, punkt.z);
	}

	@Override
	public double[][] matrix() {
		return new double[][] { new double[] { Math.cos(theta), Math.sin(theta), 0 },
				new double[] { -Math.sin(theta), Math.cos(theta), 0 }, new double[] { 0, 0, 1 } };
	}

	@Override
	public TDHTransform invert() {
		return new TDHRotation(-theta);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(theta);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TDHRotation other = (TDHRotation) obj;
		if (Double.doubleToLongBits(theta) != Double.doubleToLongBits(other.theta))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDHRotation [theta=" + theta + "]";
	}
}