package bezier.transforms.geom;

import bezier.geom.TDHPoint;

public class TDHShear implements TDHTransform {
	public final double shx;
	public final double shy;

	@Override
	public TDHTransformType type() {
		return TDHTransformType.SHEAR;
	}

	public TDHShear(double shx, double shy) {
		this.shx = shx;
		this.shy = shy;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		double x = punkt.x + (punkt.y * shx);
		double y = punkt.y + (punkt.x * shy);

		return new TDHPoint(x, y, punkt.z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(shx);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(shy);
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
		TDHShear other = (TDHShear) obj;
		if (Double.doubleToLongBits(shx) != Double.doubleToLongBits(other.shx))
			return false;
		if (Double.doubleToLongBits(shy) != Double.doubleToLongBits(other.shy))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDHShear [shx=" + shx + ", shy=" + shy + "]";
	}
}