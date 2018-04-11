package bezier.transforms;

import bezier.TDHPoint;

/**
 * A transform that does nothing.
 * 
 * @author bjculkin
 *
 */
public class TDHIdentity implements TDHTransform {
	@Override
	public TDHPoint transform(TDHPoint punkt) {
		return punkt;
	}

	@Override
	public String toString() {
		return "TDHIdentity []";
	}
}