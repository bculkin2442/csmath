package bezier.transforms;

import bezier.TDHPoint;

/**
 * Transform that does coordinate scaling.
 * 
 * @author bjculkin
 *
 */
public class TDHScale implements TDHTransform {
	/**
	 * Amount to scale x by.
	 */
	public final double sx;
	/**
	 * Amount to scale y by.
	 */
	public final double sy;
	/**
	 * Amount to scale the homogeneous coordinate by.
	 */
	public final double sz;

	/**
	 * Create a new scaling that scales each coordinate by an equal amount
	 * 
	 * @param factor
	 *            The amount to scale the coordinates by.
	 */
	public TDHScale(double factor) {
		this(factor, factor);
	}

	/**
	 * Create a new scaling that scales each coordinate separately.
	 * 
	 * @param sx
	 *            The amount to scale x by.
	 * @param sy
	 *            The amount to scale y by.
	 */
	public TDHScale(double sx, double sy) {
		this(sx, sy, 1);
	}

	/**
	 * Create a new scaling that scales each coordinate separately.
	 * 
	 * Includes the homogeneous coordinate.
	 * 
	 * @param sx
	 *            The amount to scale x by.
	 * @param sy
	 *            The amount to scale y by.
	 * @param sz
	 *            The amount to scale the homogeneous coordinate by.
	 */
	public TDHScale(double sx, double sy, double sz) {
		this.sx = sx;
		this.sy = sy;
		this.sz = sz;
	}

	@Override
	public TDHTransformType type() {
		return TDHTransformType.SCALE;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		double x = punkt.x * sx;
		double y = punkt.y * sy;
		double z = punkt.z * sz;

		return new TDHPoint(x, y, z);
	}

	@Override
	public double[][] matrix() {
		return new double[][] { new double[] { sx, 0, 0 }, new double[] { 0, sy, 0 }, new double[] { 0, 0, sz } };
	}

	public TDHTransform invert() {
		return new TDHScale(1 / sx, 1 / sy, 1 / sy);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(sx);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(sy);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(sz);
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
		TDHScale other = (TDHScale) obj;
		if (Double.doubleToLongBits(sx) != Double.doubleToLongBits(other.sx))
			return false;
		if (Double.doubleToLongBits(sy) != Double.doubleToLongBits(other.sy))
			return false;
		if (Double.doubleToLongBits(sz) != Double.doubleToLongBits(other.sz))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDHScale [sx=" + sx + ", sy=" + sy + ", sz=" + sz + "]";
	}
}