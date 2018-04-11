package bezier;

/**
 * Represents a two-dimensional point.
 * 
 * @author bjculkin
 *
 */
public class TDPoint {
	/**
	 * The x coordinate.
	 */
	public final double x;

	/**
	 * The y coordinate.
	 */
	public final double y;

	/**
	 * Create a new two-dimensional point.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 */
	public TDPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Create a new two-dimensional point.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @return A point with those coordinates.
	 */
	public static TDPoint p2(double x, double y) {
		return new TDPoint(x, y);
	}

	/**
	 * Return a new scaled point.
	 * 
	 * @param s
	 *            The amount to scale by.
	 * @return A point scaled by the specified amount.
	 */
	public TDPoint multiply(double s) {
		return p2(x * s, y * s);
	}

	/**
	 * Add two points together.
	 * 
	 * @param p1
	 *            The first point to add.
	 * @param p2
	 *            The second point to add.
	 * @return The sum of the points.
	 */
	public static TDPoint add(TDPoint p1, TDPoint p2) {
		return p2(p1.x + p2.x, p1.y + p2.y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
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
		TDPoint other = (TDPoint) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDPoint [x=" + x + ", y=" + y + "]";
	}

	/**
	 * Convert this point to a two-dimensional homogeneous point.
	 * 
	 * @return A homogeneous version of this point.
	 */
	public TDHPoint toTDHPoint() {
		return new TDHPoint(x, y);
	}
}