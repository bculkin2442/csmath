package bezier.transforms.geom;

import bezier.geom.TDHPoint;

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