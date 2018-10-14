package bezier.geom.transform;

import bezier.geom.TDHPoint;

/**
 * Reflect about x-axis.
 * @author bjculkin
 *
 */
public class TDHXAxisReflection implements TDHTransform {
	@Override
	public TDHTransformType type() {
		return TDHTransformType.REFLECTION;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		return new TDHPoint(punkt.x, -punkt.y, punkt.z);
	}

	@Override
	public String toString() {
		return "TDHXAxisReflection []";
	}
}