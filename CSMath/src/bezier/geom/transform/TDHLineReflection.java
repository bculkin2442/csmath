package bezier.geom.transform;

import bezier.geom.TDHPoint;

/**
 * Transform to reflect across a line.
 * @author bjculkin
 *
 */
public class TDHLineReflection implements TDHTransform {
	/**
	 * 'a' value for line.
	 */
	public final double a;
	/**
	 * 'b' value for line.
	 */
	public final double b;
	/**
	 * 'c' value for line.
	 */
	public final double c;

	/**
	 * Create a new line-reflect transform.
	 * @param a 'a' value for the line.
	 * @param b 'b' value for the line.
	 * @param c 'c' value for the line.
	 */
	public TDHLineReflection(double a, double b, double c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	@Override
	public TDHTransformType type() {
		return TDHTransformType.REFLECTION;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		double com = (a * a - b * b);

		double x = (punkt.x * com) - (2 * a * b * punkt.y) - (2 * a * c * punkt.z);
		double y = (-2 * a * b * punkt.x) + (punkt.y * com) - (2 * b * c * punkt.z);
		double z = (punkt.z * com);

		return new TDHPoint(x, y, z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(a);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(b);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(c);
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
		TDHLineReflection other = (TDHLineReflection) obj;
		if (Double.doubleToLongBits(a) != Double.doubleToLongBits(other.a))
			return false;
		if (Double.doubleToLongBits(b) != Double.doubleToLongBits(other.b))
			return false;
		if (Double.doubleToLongBits(c) != Double.doubleToLongBits(other.c))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDHLineReflection [a=" + a + ", b=" + b + ", c=" + c + "]";
	}
}