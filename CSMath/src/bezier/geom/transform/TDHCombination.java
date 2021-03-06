package bezier.geom.transform;

import java.util.ArrayList;
import java.util.List;

import bezier.geom.TDHPoint;

/**
 * Combination of 2D homogenous transforms.
 * @author bjculkin
 *
 */
public class TDHCombination implements TDHTransform {
	/**
	 * The transforms that make up this transform.
	 */
	public final List<TDHTransform> forms;

	@Override
	public TDHTransformType type() {
		return TDHTransformType.COMBINATION;
	}

	/**
	 * Create a new combined transform.
	 * @param forms The transforms to combine.
	 */
	public TDHCombination(TDHTransform... forms) {
		this.forms = new ArrayList<>(forms.length);

		for (TDHTransform form : forms) {
			this.forms.add(form);
		}
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		TDHPoint ret = punkt;

		for (TDHTransform form : forms) {
			ret = form.transform(ret);
		}

		return ret;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((forms == null) ? 0 : forms.hashCode());
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
		TDHCombination other = (TDHCombination) obj;
		if (forms == null) {
			if (other.forms != null)
				return false;
		} else if (!forms.equals(other.forms))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDHCombination [forms=" + forms + "]";
	}
}