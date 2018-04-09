import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListCellRenderer;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

@SuppressWarnings("javadoc")
public class CulkinAsssignmentNine {
	public static void main(String[] args) {
		JFrame fram = new JFrame("Bezier Grapher");
		fram.setLayout(new GridLayout(1, 1));

		JPanel canvasPanel = new JPanel();
		canvasPanel.setLayout(new GridLayout(1, 1));

		Bezier curve = new Bezier();

		BezierPanel canvas = new BezierPanel();
		canvas.curves.add(curve);

		canvasPanel.add(canvas);

		JPanel points = new JPanel();
		points.setLayout(new BorderLayout());

		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout(1, 1));

		DefaultListModel<TDPoint> pointModel = new DefaultListModel<>();
		pointModel.addListDataListener(new CanvasRepainter(canvas));

		JList<TDPoint> pointList = new JList<>(pointModel);
		pointList.setCellRenderer(new TDPointRenderer());

		JScrollPane listScroller = new JScrollPane(pointList);

		listPanel.add(listScroller);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));

		JButton addPoint = new JButton("Add Control Points...");
		addPoint.addActionListener(new PointAdder(pointModel, curve, fram));

		JButton remPoint = new JButton("Remove Control Point");
		remPoint.addActionListener(new PointRemover(fram, pointList, pointModel));

		buttonPanel.add(addPoint);
		buttonPanel.add(remPoint);

		points.add(listPanel, BorderLayout.CENTER);
		points.add(buttonPanel, BorderLayout.PAGE_END);

		JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, points, canvasPanel);

		fram.add(main);

		fram.setSize(640, 480);
		fram.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fram.setVisible(true);
	}
}

class CanvasRepainter implements ListDataListener {
	private final BezierPanel canvas;

	public CanvasRepainter(BezierPanel canvas) {
		this.canvas = canvas;
	}

	@Override
	public void intervalRemoved(ListDataEvent e) {
		canvas.repaint();
	}

	@Override
	public void intervalAdded(ListDataEvent e) {
		canvas.repaint();
	}

	@Override
	public void contentsChanged(ListDataEvent e) {
		canvas.repaint();
	}
}

class PointAdder implements ActionListener {
	private final DefaultListModel<TDPoint> pointModel;
	private final Bezier curve;
	private final JFrame fram;

	public PointAdder(DefaultListModel<TDPoint> pointModel, Bezier curve, JFrame fram) {
		this.pointModel = pointModel;
		this.curve = curve;
		this.fram = fram;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		JDialog dia = new JDialog(fram);
		dia.setTitle("Add Control Point");
		dia.setModalityType(ModalityType.MODELESS);
		dia.setLayout(new BorderLayout());

		JPanel fields = new JPanel();
		fields.setLayout(new GridLayout(2, 1));

		DoubleInputPanel xPanel = new DoubleInputPanel("X Coordinate: ");
		DoubleInputPanel yPanel = new DoubleInputPanel("Y Coordinate: ");

		fields.add(xPanel);
		fields.add(yPanel);

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 2));

		JButton add = new JButton("Add Control Point");

		AddListener addListener = new AddListener(xPanel, yPanel);
		add.addActionListener(addListener);

		JButton cancel = new JButton("Cancel");
		cancel.addActionListener((aev) -> {
			dia.dispose();
		});

		buttons.add(add);
		buttons.add(cancel);

		xPanel.field.addActionListener((aev) -> {
			yPanel.field.requestFocusInWindow();
		});

		yPanel.field.addActionListener((aev) -> {
			addListener.actionPerformed(null);
		});

		dia.add(fields, BorderLayout.CENTER);
		dia.add(buttons, BorderLayout.PAGE_END);

		dia.pack();
		dia.setVisible(true);
	}

	class AddListener implements ActionListener {
		private final DoubleInputPanel xPanel;
		private final DoubleInputPanel yPanel;

		public AddListener(DoubleInputPanel xPanel, DoubleInputPanel yPanel) {
			this.xPanel = xPanel;
			this.yPanel = yPanel;
		}

		@Override
		public void actionPerformed(ActionEvent aev) {
			double xVal = (Double) xPanel.field.getValue();
			double yVal = (Double) yPanel.field.getValue();

			TDPoint punkt = new TDPoint(xVal, yVal);

			pointModel.addElement(punkt);
			curve.controls.add(punkt);

			xPanel.field.setValue(0.0);
			yPanel.field.setValue(0.0);

			xPanel.field.requestFocusInWindow();
		}
	}
}

class PointRemover implements ActionListener {
	private final JFrame fram;
	private final JList<TDPoint> pointList;
	private final DefaultListModel<TDPoint> pointModel;

	public PointRemover(JFrame fram, JList<TDPoint> pointList, DefaultListModel<TDPoint> pointModel) {
		this.fram = fram;
		this.pointList = pointList;
		this.pointModel = pointModel;
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		int selectedIndex = pointList.getSelectedIndex();
		TDPoint punkt = pointModel.get(selectedIndex);

		String msg = String.format("Do you want to remove the control point (%.2f, %.2f)?", punkt.x, punkt.y);

		int confirmed = JOptionPane.showConfirmDialog(fram, msg, "Remove Control Point?", JOptionPane.YES_NO_OPTION);

		if (confirmed == JOptionPane.YES_OPTION) {
			pointModel.remove(selectedIndex);
		}
	}
}

class DoubleInputPanel extends JPanel {
	private static final long serialVersionUID = 1031310890698539040L;

	public final JFormattedTextField field;

	public DoubleInputPanel(String label) {
		super();
		setLayout(new BorderLayout());

		JLabel xLabel = new JLabel(label);

		field = new JFormattedTextField(0.0);

		add(xLabel, BorderLayout.LINE_START);
		add(field, BorderLayout.CENTER);
	}
}

class BezierPanel extends JPanel {
	public final List<Bezier> curves;

	private static final long serialVersionUID = 8748298173487657108L;

	public BezierPanel() {
		super();

		curves = new LinkedList<>();

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

		g.translate(halfWidth, halfHeight);

		g.fillRect(-halfWidth, -halfHeight, ourWidth, ourHeight);

		g.setColor(Color.BLACK);

		for (Bezier curve : curves) {
			for (int i = 0; i < curve.parts; i++) {
				double dT = 1.0 / curve.parts;

				TDPoint first = curve.scaleEval(dT * i);
				TDPoint second = curve.scaleEval(dT * (i + 1));

				g.drawLine((int) first.x, (int) first.y, (int) second.x, (int) second.y);
			}
		}
	}
}

class Bezier {
	public final List<TDPoint> controls;

	public int parts = 100;
	public int scale = 5;

	public Bezier(TDPoint... points) {
		controls = new ArrayList<>();

		for (TDPoint punkt : points) {
			controls.add(punkt);
		}
	}

	public TDPoint scaleEval(double t) {
		return eval(t).multiply(scale);
	}

	public TDPoint eval(double t) {
		if (controls.isEmpty())
			return new TDPoint(0, 0);

		TDPoint punkt = evalIntern(t, controls.size() - 1, 0);

		return punkt;
	}

	private TDPoint evalIntern(double t, int j, int k) {
		if (j <= 0) {
			return controls.get(k);
		}

		TDPoint l = evalIntern(t, j - 1, k);
		TDPoint r = evalIntern(t, j - 1, k + 1);

		l = l.multiply(1 - t);
		r = r.multiply(t);

		return TDPoint.add(l, r);
	}

	public Bezier[] decompose(double t) {
		Bezier[] curves = new Bezier[2];

		{
			TDPoint[] points = new TDPoint[controls.size()];
			for (int i = 0; i < points.length; i++) {
				points[i] = evalIntern(t, i, 0);
			}
			curves[0] = new Bezier(points);
		}

		{
			TDPoint[] points = new TDPoint[controls.size()];
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

		setText(String.format("(%.2f, %.2f)", value.x, value.y));

		return this;
	}
}