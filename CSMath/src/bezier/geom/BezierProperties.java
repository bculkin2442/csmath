package bezier.geom;

import java.awt.Color;

/**
 * Properties for graphing a bezier curve.
 * 
 * @author bjculkin
 *
 */
public class BezierProperties {
	/**
	 * The number of separate points to graph from the curve.
	 */
	public int parts = 100;

	/**
	 * The multiplier to apply to coordinates.
	 */
	public double scale = 5;

	/**
	 * The color for the curve itself.
	 */
	public Color	curveColor	= Color.BLACK;
	/**
	 * The color for the points in the curve.
	 */
	public Color	pointColor	= Color.RED;
	/**
	 * The color for the bounding box.
	 */
	public Color	boxColor	= Color.GREEN;

	/**
	 * Create a new set of default bezier properties.
	 */
	public BezierProperties() {
	}

	/**
	 * Create a new set of bezier properties, with specific values.
	 * 
	 * @param parts
	 *                The number of points to graph in the curve.
	 * @param scale
	 *                The amount to scale the curve by.
	 * @param curveColor
	 *                The color for the curve.
	 * @param pointColor
	 *                The color for the points in the curve.
	 * @param boxColor
	 *                The color for the bounding box of the curve.
	 */
	public BezierProperties(int parts, double scale, Color curveColor, Color pointColor, Color boxColor) {
		this.parts = parts;
		this.scale = scale;
		this.curveColor = curveColor;
		this.pointColor = pointColor;
		this.boxColor = boxColor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((boxColor == null) ? 0 : boxColor.hashCode());
		result = prime * result + ((curveColor == null) ? 0 : curveColor.hashCode());
		result = prime * result + parts;
		result = prime * result + ((pointColor == null) ? 0 : pointColor.hashCode());
		long temp;
		temp = Double.doubleToLongBits(scale);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		BezierProperties other = (BezierProperties) obj;
		if (boxColor == null) {
			if (other.boxColor != null) return false;
		} else if (!boxColor.equals(other.boxColor)) return false;
		if (curveColor == null) {
			if (other.curveColor != null) return false;
		} else if (!curveColor.equals(other.curveColor)) return false;
		if (parts != other.parts) return false;
		if (pointColor == null) {
			if (other.pointColor != null) return false;
		} else if (!pointColor.equals(other.pointColor)) return false;
		if (Double.doubleToLongBits(scale) != Double.doubleToLongBits(other.scale)) return false;
		return true;
	}

	@Override
	public String toString() {
		return "BezierProperties [parts=" + parts + ", scale=" + scale + ", curveColor=" + curveColor
				+ ", pointColor=" + pointColor + ", boxColor=" + boxColor + "]";
	}
}