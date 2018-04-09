import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.util.Arrays;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;

public class CulkinAsssignmentNine {
	public static void main(String[] args) {
		JFrame fram = new JFrame();
		fram.setLayout(new GridLayout(1, 1));

		JPanel canvas = new JPanel();
		canvas.setLayout(new GridLayout(1, 1));

		JPanel points = new JPanel();
		points.setLayout(new BorderLayout());

		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout(1, 1));

		DefaultListModel<TDPoint> pointModel = new DefaultListModel<>();

		JList<TDPoint> pointList = new JList<>(pointModel);
		pointList.setCellRenderer(new TDPointRenderer());

		JScrollPane listScroller = new JScrollPane(pointList);

		listPanel.add(listScroller);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));

		JButton addPoint = new JButton("Add Control Point");

		JButton remPoint = new JButton("Remove Control Point");

		buttonPanel.add(addPoint);
		buttonPanel.add(remPoint);

		points.add(listPanel, BorderLayout.CENTER);
		points.add(buttonPanel, BorderLayout.PAGE_END);

		JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, points, canvas);

		fram.add(main);

		fram.setSize(640, 480);
		fram.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fram.setVisible(true);
	}
}

class Bezier {
	public final TDPoint[] controls;

	public Bezier(TDPoint... points) {
		controls = points;
	}

	public TDPoint eval(double t) {
		return evalIntern(t, controls.length, 0);
	}

	private TDPoint evalIntern(double t, int j, int k) {
		if (j == 0)
			return controls[k];

		TDPoint l = evalIntern(t, j - 1, k);
		TDPoint r = evalIntern(t, j - 1, k + 1);

		l = l.multiply(1 - t);
		r = r.multiply(t);

		return TDPoint.add(l, r);
	}

	public Bezier[] decompose(double t) {
		Bezier[] curves = new Bezier[2];

		{
			TDPoint[] points = new TDPoint[controls.length];
			for (int i = 0; i < points.length; i++) {
				points[i] = evalIntern(t, i, 0);
			}
			curves[0] = new Bezier(points);
		}

		{
			TDPoint[] points = new TDPoint[controls.length];
			for (int i = 0; i < points.length; i++) {
				points[i] = evalIntern(t, (points.length - 1) - i, i);
			}
			curves[1] = new Bezier(points);
		}

		return curves;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(controls);
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
		if (!Arrays.equals(controls, other.controls))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Bezier [controls=" + Arrays.toString(controls) + "]";
	}
}

class TDPoint {
	public final double x;
	public final double y;

	public TDPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public static TDPoint p2(double x, double y) {
		return new TDPoint(x, y);
	}

	public TDPoint multiply(double s) {
		return p2(x * s, y * s);
	}

	public static TDPoint add(TDPoint p1, TDPoint p2) {
		return p2(p1.x + p2.x, p1.y + p2.y);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
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
		TDPoint other = (TDPoint) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDPoint [x=" + x + ", y=" + y + "]";
	}
}

final class TDPointRenderer extends JLabel implements ListCellRenderer<TDPoint> {
	private static final long serialVersionUID = 629873168260730449L;

	public TDPointRenderer() {
		setOpaque(true);
		setHorizontalAlignment(CENTER);
		setVerticalAlignment(CENTER);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends TDPoint> list, TDPoint value, int index,
			boolean isSelected, boolean cellHasFocus) {
		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		setText(String.format("(%.2d, %.2d)", value.x, value.y));

		return this;
	}
}