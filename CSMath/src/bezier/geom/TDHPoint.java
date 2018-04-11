package bezier.geom;

/**
 * A two-dimensional homogeneous point.
 * 
 * @author bjculkin
 *
 */
public class TDHPoint extends TDPoint {
	/**
	 * The homogeneous coordinate for the point.
	 */
	public final double z;

	/**
	 * Create a new two-dimensional homogeneous point.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @param z
	 *            The homogeneous coordinate.
	 */
	public TDHPoint(double x, double y, double z) {
		super(x, y);

		this.z = z;
	}

	/**
	 * Create a new two-dimensional homogeneous point.
	 * 
	 * The homogeneous coordinate is set to 1.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 */
	public TDHPoint(double x, double y) {
		this(x, y, 1);
	}

	/**
	 * Convert this point to a plain two-dimensional point.
	 * 
	 * @return A two-dimensional version of this point.
	 */
	public TDPoint toTDPoint() {
		/*
		 * Convert back down by dividing each coordinate by the homogeneous value.
		 */
		return new TDPoint(x / z, y / z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(z);
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
		TDHPoint other = (TDHPoint) obj;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDHPoint [z=" + z + ", x=" + x + ", y=" + y + "]";
	}
}