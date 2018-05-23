package bezier;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Collection;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import bezier.geom.Bezier;
import bezier.geom.TDHPoint;
import bezier.geom.TDPoint;
import bezier.geom.transform.TDHCombination;
import bezier.geom.transform.TDHTransform;
import bezier.geom.transform.TDHTranslate;
import bezier.geom.transform.TDHXAxisReflection;

/*
 * Panel that graphs a set of bezier curves.
 */
public class BezierPanel extends JPanel {
	public final Collection<Bezier> curves;

	private static final long serialVersionUID = 8748298173487657108L;

	public BezierPanel() {
		super();

		curves = new LinkedList<>();

		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	}

	public BezierPanel(Collection<Bezier> curvs) {
		super();

		curves = curvs;

		setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		int ourWidth = getWidth();
		int ourHeight = getHeight();

		int halfWidth = ourWidth / 2;
		int halfHeight = ourHeight / 2;

		g.setColor(Color.WHITE);

		/*
		 * Draw background
		 */
		g.fillRect(0, 0, ourWidth, ourHeight);

		g.setColor(Color.BLUE);

		/*
		 * Draw coordinate grid.
		 */
		g.drawLine(halfWidth, 0, halfWidth, ourHeight);
		g.drawLine(0, halfHeight, ourWidth, halfHeight);

		/*
		 * Transform to place points at center of grid w/ right orientation.
		 */
		TDHTransform translate = new TDHCombination(new TDHXAxisReflection(), new TDHTranslate(halfWidth, halfHeight));

		for (Bezier curve : curves) {
			/*
			 * Skip curves with no points.
			 */
			if (curve.controls.isEmpty()) {
				continue;
			}

			{
				/*
				 * Draw curve bounding box.
				 */
				g.setColor(curve.data.boxColor);
				TDPoint[] ex = curve.extrema(translate);

				g.drawLine((int) ex[0].x, (int) ex[0].y, (int) ex[1].x, (int) ex[1].y);
				g.drawLine((int) ex[1].x, (int) ex[1].y, (int) ex[2].x, (int) ex[2].y);
				g.drawLine((int) ex[2].x, (int) ex[2].y, (int) ex[3].x, (int) ex[3].y);
				g.drawLine((int) ex[3].x, (int) ex[3].y, (int) ex[0].x, (int) ex[0].y);
			}

			g.setColor(curve.data.pointColor);
			for (TDPoint control : curve.controls) {
				/*
				 * Draw curve points.
				 */
				TDHPoint translatedPoint = translate.transform(control.multiply(curve.data.scale).toTDHPoint());

				control = translatedPoint.toTDPoint();

				drawCircle(g, control.x, control.y, 6);
			}

			g.setColor(curve.data.curveColor);
			for (int i = 0; i < curve.data.parts; i++) {
				/*
				 * Draw curve itself.
				 */
				double dT = 1.0 / curve.data.parts;

				TDPoint firt = curve.scaleEval(dT * i);
				TDPoint secod = curve.scaleEval(dT * (i + 1));

				TDPoint first = translate.transform(firt.toTDHPoint()).toTDPoint();
				TDPoint second = translate.transform(secod.toTDHPoint()).toTDPoint();

				g.drawLine((int) first.x, (int) first.y, (int) second.x, (int) second.y);
			}
		}
	}

	/*
	 * Draw a circle of a given diameter at a point.
	 */
	private static void drawCircle(Graphics g, double x, double y, int diameter) {
		g.fillOval((int) x - diameter / 2, (int) y - diameter / 2, diameter, diameter);
	}
}