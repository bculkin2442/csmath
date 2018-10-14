package bezier.geom.transform;

import bezier.geom.TDHPoint;

/**
 * Reflect about y-axis.
 * @author bjculkin
 *
 */
public class TDHYAxisReflection implements TDHTransform {
	@Override
	public TDHTransformType type() {
		return TDHTransformType.REFLECTION;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		return new TDHPoint(-punkt.x, punkt.y, punkt.z);
	}

	@Override
	public String toString() {
		return "TDHYAxisReflection []";
	}
}