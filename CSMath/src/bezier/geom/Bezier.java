package bezier.geom;

import java.util.ArrayList;
import java.util.List;

import bezier.transforms.TDHIdentity;
import bezier.transforms.TDHTransform;

/**
 * Represents a bezier curve.
 */
public class Bezier {
	/**
	 * The control points of the curve.
	 */
	public final List<TDPoint> controls;

	/**
	 * The display properties for the curve.
	 */
	public BezierProperties data;

	/**
	 * Create a new bezier curve from a set of control points.
	 * 
	 * @param points
	 *            The control points to use.
	 */
	public Bezier(TDPoint... points) {
		data = new BezierProperties();

		controls = new ArrayList<>();

		for (TDPoint punkt : points) {
			controls.add(punkt);
		}
	}

	/**
	 * Do a scaled evaluation of the curve.
	 * 
	 * @param t
	 *            The value to evaluate at.
	 * @return The point on the curve, scaled by the curves scaling factor.
	 */
	public TDPoint scaleEval(double t) {
		return eval(t).multiply(data.scale);
	}

	/**
	 * Do a scaled evaluation of the curve.
	 * 
	 * @param t
	 *            The value to evaluate at.
	 */
	public TDPoint eval(double t) {
		if (controls.isEmpty())
			return new TDPoint(0, 0);

		TDPoint punkt = evalIntern(t, controls.size() - 1, 0);

		return punkt;
	}

	/*
	 * Recursive evaluation of bezier curve.
	 */
	private TDPoint evalIntern(double t, int j, int k) {
		/*
		 * Base case.
		 */
		if (j <= 0) {
			return controls.get(k);
		}

		/*
		 * Get the two component points.
		 */
		TDPoint l = evalIntern(t, j - 1, k);
		TDPoint r = evalIntern(t, j - 1, k + 1);

		/*
		 * Adjust them by parameter value
		 */
		l = l.multiply(1 - t);
		r = r.multiply(t);

		/*
		 * Give back their sum
		 */
		return TDPoint.add(l, r);
	}

	/**
	 * Split a curve into two curves at a specific point.
	 * 
	 * @param t
	 *            The point to split the curve at.
	 * @return The two curves, split at that point.
	 */
	public Bezier[] decompose(double t) {
		Bezier[] curves = new Bezier[2];

		{
			/*
			 * Get the first curve.
			 */
			TDPoint[] points = new TDPoint[controls.size()];
			for (int i = 0; i < points.length; i++) {
				points[i] = evalIntern(t, i, 0);
			}
			curves[0] = new Bezier(points);
		}

		{
			/*
			 * Get the second curve.
			 */
			TDPoint[] points = new TDPoint[controls.size()];
			for (int i = 0; i < points.length; i++) {
				points[i] = evalIntern(t, (points.length - 1) - i, i);
			}
			curves[1] = new Bezier(points);
		}

		return curves;
	}

	/**
	 * Get the bounding box for this curves control points.
	 * 
	 * @return The bounding box for the control points.
	 */
	public TDPoint[] extrema() {
		return extrema(new TDHIdentity());
	}

	/**
	 * Get the bounding box for a transformed version of this curves control points.
	 * 
	 * @param transform
	 *            The transform to apply to the control points.
	 * @return The bounding box for the transformed control points.
	 */
	public TDPoint[] extrema(TDHTransform transform) {
		TDPoint[] box = new TDPoint[4];

		double xMin = Double.MAX_VALUE, xMax = Double.MIN_VALUE;
		double yMin = Double.MAX_VALUE, yMax = Double.MIN_VALUE;

		for (TDPoint punkt1 : controls) {
			/*
			 * Get transformed point
			 */
			TDPoint punkt = punkt1.multiply(data.scale);
			punkt = transform.transform(punkt.toTDHPoint()).toTDPoint();

			/*
			 * Set min/max x vals
			 */
			if (punkt.x < xMin) {
				xMin = punkt.x;
			}
			if (punkt.x > xMax) {
				xMax = punkt.x;
			}

			/*
			 * Set min/max y vals
			 */
			if (punkt.y < yMin) {
				yMin = punkt.y;
			}
			if (punkt.y > yMax) {
				yMax = punkt.y;
			}
		}

		/*
		 * Create returned points.
		 */
		box[0] = new TDPoint(xMin, yMin);
		box[1] = new TDPoint(xMax, yMin);
		box[2] = new TDPoint(xMax, yMax);
		box[3] = new TDPoint(xMin, yMax);

		return box;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((controls == null) ? 0 : controls.hashCode());
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
		Bezier other = (Bezier) obj;
		if (controls == null) {
			if (other.controls != null)
				return false;
		} else if (!controls.equals(other.controls))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return String.format("Bezier [controls=%s]", controls);
	}
}