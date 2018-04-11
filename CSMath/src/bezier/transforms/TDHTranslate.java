package bezier.transforms;

import bezier.TDHPoint;

/**
 * A transform that does coordinate translation.
 * 
 * @author bjculkin
 *
 */
public class TDHTranslate implements TDHTransform {
	/**
	 * The amount to translate the x-coordinate by.
	 */
	public final double h;
	/**
	 * The amount to translate the y-coordinate by.
	 */
	public final double k;

	/**
	 * Create a new translation transform.
	 * 
	 * @param h
	 *            The amount to translate x by.
	 * @param k
	 *            The amount to translate y by.
	 */
	public TDHTranslate(double h, double k) {
		this.h = h;
		this.k = k;
	}

	@Override
	public TDHTransformType type() {
		return TDHTransformType.TRANSLATE;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		double x = punkt.x + (punkt.z * h);
		double y = punkt.y + (punkt.z * k);

		return new TDHPoint(x, y, punkt.z);
	}

	@Override
	public double[][] matrix() {
		return new double[][] { new double[] { 1, 0, 0 }, new double[] { 0, 1, 0 }, new double[] { h, k, 1 } };
	}

	@Override
	public TDHTransform invert() {
		return new TDHTranslate(-h, -k);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(h);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(k);
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
		TDHTranslate other = (TDHTranslate) obj;
		if (Double.doubleToLongBits(h) != Double.doubleToLongBits(other.h))
			return false;
		if (Double.doubleToLongBits(k) != Double.doubleToLongBits(other.k))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDHTranslate [h=" + h + ", k=" + k + "]";
	}
}