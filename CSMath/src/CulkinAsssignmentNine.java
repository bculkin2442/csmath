import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.ListCellRenderer;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Main class for Bezier graphing.
 * 
 * @author acm
 *
 */
public class CulkinAsssignmentNine {
	/**
	 * The current bezier curve.
	 */
	public final Holder<Bezier> currentCurve;

	/**
	 * The directory of all bezier curves.
	 */
	public final Map<String, Bezier> curveDirectory;

	/**
	 * Create a new main class.
	 */
	public CulkinAsssignmentNine() {
		curveDirectory = new HashMap<>();

		/*
		 * Set current curve.
		 */
		currentCurve = new Holder<>();
		currentCurve.setVal(new Bezier());

		/*
		 * Install current curve into directory.
		 */
		curveDirectory.put("Default", currentCurve.getVal());
	}

	public static void main(String[] args) {
		CulkinAsssignmentNine assign = new CulkinAsssignmentNine();

		/*
		 * Use the native look and feel.
		 */
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		JFrame fram = assign.setupFrame();

		/*
		 * Display the GUI.
		 */
		fram.setSize(640, 480);
		fram.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fram.setVisible(true);
	}

	/*
	 * Setup the main JFrame
	 */
	private JFrame setupFrame() {
		JFrame fram = new JFrame("Bezier Grapher");
		fram.setLayout(new BorderLayout());

		JPanel canvasPanel = new JPanel();
		canvasPanel.setLayout(new GridLayout(1, 1));

		BezierPanel canvas = new BezierPanel(curveDirectory.values());

		canvasPanel.add(canvas);

		JPanel points = new JPanel();
		points.setLayout(new BorderLayout());

		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout(1, 1));

		DefaultListModel<TDPoint> pointModel = new DefaultListModel<>();

		/*
		 * Repaint the canvas whenever the list changes.
		 */
		pointModel.addListDataListener(new CanvasRepainter(canvas));

		/*
		 * Update the list whenever the curve changes.
		 */
		currentCurve.addHolderListener((val) -> {
			pointModel.clear();

			for (TDPoint punkt : val.controls) {
				pointModel.addElement(punkt);
			}
		});

		JList<TDPoint> pointList = new JList<>(pointModel);

		/*
		 * Use special point renderer.
		 */
		pointList.setCellRenderer(new TDPointRenderer());

		JScrollPane listScroller = new JScrollPane(pointList);

		listPanel.add(listScroller);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new GridLayout(1, 2));

		/*
		 * Setup action buttons.
		 */
		JButton addPoint = new JButton("Add Control Points...");
		addPoint.addActionListener(new PointAdder(pointModel, currentCurve, fram));

		JButton remPoint = new JButton("Remove Control Point");
		remPoint.addActionListener(new PointRemover(fram, pointList, pointModel, currentCurve));

		buttonPanel.add(addPoint);
		buttonPanel.add(remPoint);

		points.add(listPanel, BorderLayout.CENTER);
		points.add(buttonPanel, BorderLayout.PAGE_END);

		JSplitPane main = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, points, canvasPanel);

		JPanel menuPanel = new JPanel();
		menuPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 1));

		JPanel editingPanel = new JPanel();
		editingPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 1));
		editingPanel.setBorder(new BevelBorder(BevelBorder.RAISED));

		JButton editCurveProperties = new JButton("Edit Curve Properties");
		editCurveProperties.addActionListener(new CurveEditor(fram, currentCurve, canvas));

		editingPanel.add(editCurveProperties);

		JPanel destructivePanel = new JPanel();
		destructivePanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 1));
		destructivePanel.setBorder(new BevelBorder(BevelBorder.RAISED));

		/*
		 * Clear all curve points.
		 */
		JButton clearPoints = new JButton("Clear Points");
		clearPoints.addActionListener((ev) -> {
			int confirm = JOptionPane.showConfirmDialog(fram, "Are you sure you want to clear the points?",
					"Clear Points", JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				currentCurve.getVal().controls.clear();
				pointModel.clear();
			}
		});

		/*
		 * Reset to use new curve.
		 */
		JButton resetCurve = new JButton("Reset Curve");
		resetCurve.addActionListener((ev) -> {
			int confirm = JOptionPane.showConfirmDialog(fram, "Are you sure you want to reset the curve?",
					"Clear Points", JOptionPane.YES_NO_OPTION);

			if (confirm == JOptionPane.YES_OPTION) {
				currentCurve.getVal().controls.clear();

				currentCurve.setVal(new Bezier());

				curveDirectory.put("Default", currentCurve.getVal());

				canvas.repaint();
			}
		});

		destructivePanel.add(clearPoints);
		destructivePanel.add(resetCurve);

		/*
		 * This isn't implemented yet.
		 */
		JPanel curvesPanel = new JPanel();
		curvesPanel.setLayout(new FlowLayout(FlowLayout.LEADING, 5, 1));
		curvesPanel.setBorder(new BevelBorder(BevelBorder.RAISED));

		menuPanel.add(editingPanel);
		menuPanel.add(destructivePanel);
		// menuPanel.add(curvesPanel);

		fram.add(main, BorderLayout.CENTER);
		fram.add(menuPanel, BorderLayout.PAGE_START);

		return fram;
	}
}

/*
 * Do editing of curve properties.
 */
class CurveEditor implements ActionListener {
	private Bezier curve;
	private JFrame fram;
	private BezierPanel canvas;

	public CurveEditor(JFrame fram, Holder<Bezier> currentCurve, BezierPanel canvas) {
		this.fram = fram;
		this.canvas = canvas;

		curve = currentCurve.getVal();

		/*
		 * Set the curve to the right one.
		 */
		currentCurve.addHolderListener((val) -> {
			curve = val;
		});
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		JDialog dia = new JDialog(fram, "Curve Editor", ModalityType.MODELESS);
		dia.setLayout(new BorderLayout());

		JPanel fields = new JPanel();
		fields.setLayout(new BorderLayout());

		LabeledInputPanel partsPanel = new LabeledInputPanel("# of Points to Graph", curve.data.parts);

		JTabbedPane colorPanel = new JTabbedPane();
		colorPanel.setBorder(new TitledBorder(new BevelBorder(BevelBorder.RAISED), "Colors"));

		ColorInputPanel curveColor = new ColorInputPanel(curve.data.curveColor, "Curve Color");
		ColorInputPanel pointColor = new ColorInputPanel(curve.data.pointColor, "Point Color");
		ColorInputPanel boxColor = new ColorInputPanel(curve.data.boxColor, "Bounding Box Color");

		colorPanel.addTab("Curve Color", curveColor);
		colorPanel.addTab("Point Color", pointColor);
		colorPanel.addTab("Bounding Box Color", boxColor);

		LabeledInputPanel scalePanel = new LabeledInputPanel("Curve Scaling Multiplier", curve.data.scale);

		fields.add(partsPanel, BorderLayout.PAGE_START);
		fields.add(colorPanel, BorderLayout.CENTER);
		fields.add(scalePanel, BorderLayout.PAGE_END);

		JPanel buttons = new JPanel();
		buttons.setLayout(new GridLayout(1, 3));

		/*
		 * Persist changes to curve.
		 */
		JButton saveButton = new JButton("Save Changes");
		saveButton.addActionListener((aev) -> {
			curve.data.parts = (Integer) partsPanel.field.getValue();
			curve.data.scale = (Double) scalePanel.field.getValue();

			curve.data.curveColor = curveColor.picker.getColor();
			curve.data.pointColor = pointColor.picker.getColor();
			curve.data.boxColor = boxColor.picker.getColor();

			canvas.repaint();
		});

		/*
		 * Reset fields to match curve.
		 */
		JButton resetButton = new JButton("Reset Changes");
		resetButton.addActionListener((aev) -> {
			partsPanel.field.setValue(curve.data.parts);
			scalePanel.field.setValue(curve.data.scale);

			curveColor.picker.setColor(curve.data.curveColor);
			pointColor.picker.setColor(curve.data.pointColor);
			boxColor.picker.setColor(curve.data.boxColor);
		});

		JButton cancelButton = new JButton("Cancel Changes");
		cancelButton.addActionListener((aev) -> {
			dia.dispose();
		});

		buttons.add(saveButton);
		buttons.add(resetButton);
		buttons.add(cancelButton);

		dia.add(fields, BorderLayout.CENTER);
		dia.add(buttons, BorderLayout.PAGE_END);

		dia.pack();
		dia.setVisible(true);
	}
}

/*
 * Panel for inputting colors.
 */
class ColorInputPanel extends JPanel {
	private static final long serialVersionUID = 5201595672794938745L;

	public final JColorChooser picker;

	public ColorInputPanel(String label) {
		this(Color.WHITE, label);
	}

	public ColorInputPanel(Color init, String label) {
		picker = new JColorChooser(init);

		setLayout(new BorderLayout());

		JLabel lab = new JLabel(label);

		add(lab, BorderLayout.LINE_START);
		add(picker, BorderLayout.CENTER);
	}
}

/*
 * Panel that graphs a set of bezier curves.
 */
class BezierPanel extends JPanel {
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

/**
 * Represents a bezier curve.
 */
class Bezier {
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

/**
 * Represents a two-dimensional point.
 * 
 * @author bjculkin
 *
 */
class TDPoint {
	/**
	 * The x coordinate.
	 */
	public final double x;

	/**
	 * The y coordinate.
	 */
	public final double y;

	/**
	 * Create a new two-dimensional point.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 */
	public TDPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Create a new two-dimensional point.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @return A point with those coordinates.
	 */
	public static TDPoint p2(double x, double y) {
		return new TDPoint(x, y);
	}

	/**
	 * Return a new scaled point.
	 * 
	 * @param s
	 *            The amount to scale by.
	 * @return A point scaled by the specified amount.
	 */
	public TDPoint multiply(double s) {
		return p2(x * s, y * s);
	}

	/**
	 * Add two points together.
	 * 
	 * @param p1
	 *            The first point to add.
	 * @param p2
	 *            The second point to add.
	 * @return The sum of the points.
	 */
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

	/**
	 * Convert this point to a two-dimensional homogeneous point.
	 * 
	 * @return A homogeneous version of this point.
	 */
	public TDHPoint toTDHPoint() {
		return new TDHPoint(x, y);
	}
}

/**
 * A two-dimensional homogeneous point.
 * 
 * @author bjculkin
 *
 */
class TDHPoint extends TDPoint {
	/**
	 * The homogeneous coordinate for the point.
	 */
	public final double z;

	/**
	 * Create a new two-dimensional homogeneous point.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 * @param z
	 *            The homogeneous coordinate.
	 */
	public TDHPoint(double x, double y, double z) {
		super(x, y);

		this.z = z;
	}

	/**
	 * Create a new two-dimensional homogeneous point.
	 * 
	 * The homogeneous coordinate is set to 1.
	 * 
	 * @param x
	 *            The x coordinate.
	 * @param y
	 *            The y coordinate.
	 */
	public TDHPoint(double x, double y) {
		this(x, y, 1);
	}

	/**
	 * Convert this point to a plain two-dimensional point.
	 * 
	 * @return A two-dimensional version of this point.
	 */
	public TDPoint toTDPoint() {
		/*
		 * Convert back down by dividing each coordinate by the homogeneous value.
		 */
		return new TDPoint(x / z, y / z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TDHPoint other = (TDHPoint) obj;
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDHPoint [z=" + z + ", x=" + x + ", y=" + y + "]";
	}
}

/**
 * Renderer for TDPoints in JLists.
 * 
 * @author bjculkin
 *
 */
final class TDPointRenderer extends JLabel implements ListCellRenderer<TDPoint> {
	private static final long serialVersionUID = 629873168260730449L;

	/**
	 * Create a new TDPoint renderer.
	 */
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

/**
 * Listener to remove points from a bezier curve.
 * 
 * @author bjculkin
 *
 */
class PointRemover implements ActionListener {
	private final JFrame fram;
	private final JList<TDPoint> pointList;
	private final DefaultListModel<TDPoint> pointModel;

	private Bezier curve;

	/**
	 * Create a new listener to remove points from a bezier curve.
	 * 
	 * @param fram
	 *            The frame to use.
	 * @param pointList
	 *            The list the points are stored in.
	 * @param pointModel
	 *            The data backing the list.
	 * @param curveHolder
	 *            The current curve.
	 */
	public PointRemover(JFrame fram, JList<TDPoint> pointList, DefaultListModel<TDPoint> pointModel,
			Holder<Bezier> curveHolder) {
		this.fram = fram;
		this.pointList = pointList;
		this.pointModel = pointModel;

		/*
		 * Change our curve if the current one changes.
		 */
		curve = curveHolder.getVal();
		curveHolder.addHolderListener((val) -> {
			curve = val;
		});
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		int selectedIndex = pointList.getSelectedIndex();

		/*
		 * Nothing selected.
		 */
		if (selectedIndex == -1)
			return;

		TDPoint punkt = pointModel.get(selectedIndex);

		String msg = String.format("Do you want to remove the control point (%.2f, %.2f)?", punkt.x, punkt.y);

		int confirmed = JOptionPane.showConfirmDialog(fram, msg, "Remove Control Point?", JOptionPane.YES_NO_OPTION);

		if (confirmed == JOptionPane.YES_OPTION) {
			pointModel.remove(selectedIndex);
			curve.controls.remove(punkt);
		}
	}
}

/**
 * Listener for adding points to a bezier curve.
 * 
 * @author bjculkin
 *
 */
class PointAdder implements ActionListener {
	private final DefaultListModel<TDPoint> pointModel;
	private final JFrame fram;

	private Bezier curve;

	/**
	 * Create a listener that adds points to a bezier curve.
	 * 
	 * @param pointModel
	 *            The place to store points.
	 * @param curveHolder
	 *            The curve to add to.
	 * @param fram
	 *            The frame to use.
	 */
	public PointAdder(DefaultListModel<TDPoint> pointModel, Holder<Bezier> curveHolder, JFrame fram) {
		this.pointModel = pointModel;
		this.curve = curveHolder.getVal();
		this.fram = fram;

		/*
		 * Change our curve if the current one changes.
		 */
		curveHolder.addHolderListener((curv) -> {
			curve = curv;
		});
	}

	@Override
	public void actionPerformed(ActionEvent ev) {
		JDialog dia = new JDialog(fram);
		dia.setTitle("Add Control Point");
		dia.setModalityType(ModalityType.MODELESS);
		dia.setLayout(new BorderLayout());

		JPanel fields = new JPanel();
		fields.setLayout(new GridLayout(2, 1));

		LabeledInputPanel xPanel = new LabeledInputPanel("X Coordinate: ", 0.0);
		LabeledInputPanel yPanel = new LabeledInputPanel("Y Coordinate: ", 0.0);

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

		/*
		 * Change focus to each field on action
		 */
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

	/**
	 * Listener for adding points to a curve.
	 * 
	 * @author bjculkin
	 *
	 */
	class AddListener implements ActionListener {
		private final LabeledInputPanel xPanel;
		private final LabeledInputPanel yPanel;

		public AddListener(LabeledInputPanel xPanel, LabeledInputPanel yPanel) {
			this.xPanel = xPanel;
			this.yPanel = yPanel;
		}

		@Override
		public void actionPerformed(ActionEvent aev) {
			/*
			 * Add point to curve.
			 */
			double xVal = (Double) xPanel.field.getValue();
			double yVal = (Double) yPanel.field.getValue();

			TDPoint punkt = new TDPoint(xVal, yVal);

			pointModel.addElement(punkt);
			curve.controls.add(punkt);

			/*
			 * Reset field values.
			 */
			xPanel.field.setValue(0.0);
			yPanel.field.setValue(0.0);

			xPanel.field.requestFocusInWindow();
		}
	}
}

/**
 * Listener to repaint a component whenever a list changes.
 * 
 * @author bjculkin
 *
 */
class CanvasRepainter implements ListDataListener {
	private final JComponent comp;

	/**
	 * Create a new repaint listener.
	 * 
	 * @param canvas
	 *            The component to repaint.
	 */
	public CanvasRepainter(JComponent canvas) {
		this.comp = canvas;
	}

	@Override
	public void intervalRemoved(ListDataEvent e) {
		comp.repaint();
	}

	@Override
	public void intervalAdded(ListDataEvent e) {
		comp.repaint();
	}

	@Override
	public void contentsChanged(ListDataEvent e) {
		comp.repaint();
	}
}

/**
 * A component for getting formatted input with a label.
 * 
 * @author bjculkin
 *
 */
class LabeledInputPanel extends JPanel {
	private static final long serialVersionUID = 1031310890698539040L;

	/**
	 * The field input is stored in.
	 */
	public final JFormattedTextField field;

	/**
	 * Create a new labeled input component.
	 * 
	 * @param label
	 *            The label for the component.
	 * @param val
	 *            The initial value for the field.
	 */
	public LabeledInputPanel(String label, Object val) {
		super();
		setLayout(new BorderLayout());

		JLabel xLabel = new JLabel(label);

		field = new JFormattedTextField(val);

		add(xLabel, BorderLayout.LINE_START);
		add(field, BorderLayout.CENTER);
	}
}

/**
 * Dummy class for holding a value, and being notified when it changes.
 * 
 * Essentially a pointer with change notifications.
 * 
 * @author bjculkin
 *
 * @param <E>
 *            The type of held value.
 */
class Holder<E> {
	/*
	 * The held value.
	 */
	private E val;

	/*
	 * The listeners to notify.
	 */
	private final List<Consumer<E>> listeners;

	/**
	 * Create a new holder holding nothing.
	 */
	public Holder() {
		listeners = new LinkedList<>();
	}

	/**
	 * Create a new holder holding a specific value.
	 * 
	 * @param val
	 *            The value being held.
	 */
	public Holder(E val) {
		this();

		this.val = val;
	}

	/**
	 * Get the contained value.
	 * 
	 * @return The contained value.
	 */
	public E getVal() {
		return val;
	}

	/**
	 * Set the contained value, and notify listeners.
	 * 
	 * @param val
	 *            The new value.
	 */
	public void setVal(E val) {
		this.val = val;

		/*
		 * Notify listeners.
		 */
		for (Consumer<E> listen : listeners) {
			listen.accept(val);
		}
	}

	/**
	 * Add a listener.
	 * 
	 * @param listen
	 *            The listener to add.
	 */
	public void addHolderListener(Consumer<E> listen) {
		listeners.add(listen);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((val == null) ? 0 : val.hashCode());
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
		Holder<?> other = (Holder<?>) obj;
		if (val == null) {
			if (other.val != null)
				return false;
		} else if (!val.equals(other.val))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Holder [val=" + val + "]";
	}
}

/**
 * Types of transform to apply to TDHPoints.
 * 
 * @author bjculkin
 *
 */
enum TDHTransformType {
	/**
	 * Coordinate translation.
	 */
	TRANSLATE,
	/**
	 * Do nothing transform.
	 */
	IDENTITY,
	/**
	 * Coordinate scaling.
	 */
	SCALE,
	/**
	 * Coordinate rotation.
	 */
	ROTATION,
	/**
	 * Coordinate reflection.
	 */
	REFLECTION,
	/**
	 * Coordinate shearing.
	 */
	SHEAR,
	/**
	 * Multiple transformations.
	 */
	COMBINATION,
	/**
	 * Arbitrary matrix transformation.
	 */
	MATRIX
}

/**
 * Transformation applicable to TDHPoints.
 * 
 * @author bjculkin
 *
 */
@FunctionalInterface
interface TDHTransform {
	/**
	 * Get the type of this transform.
	 * 
	 * Unknown transformations are assumed to be identity transforms.
	 * 
	 * @return The type of this transform.
	 */
	default TDHTransformType type() {
		return TDHTransformType.IDENTITY;
	}

	/**
	 * Get the matrix representation of the transform.
	 * 
	 * Unknown transformations are assumed to be identity transforms.
	 * 
	 * @return The matrix representation of the transform.
	 */
	default double[][] matrix() {
		return new double[][] { new double[] { 1, 0, 0 }, new double[] { 0, 1, 0 }, new double[] { 0, 0, 1 } };
	}

	/**
	 * Get the inverse of the transform.
	 * 
	 * Unknown transformations are assumed to be identity transforms.
	 * 
	 * @return The inverse the transform.
	 */
	default TDHTransform invert() {
		return new TDHIdentity();
	}

	/**
	 * Apply the transform to a point.
	 * 
	 * @param punkt
	 *            The point to transform.
	 * @return A transformed version of the point.
	 */
	TDHPoint transform(TDHPoint punkt);
}

/**
 * A transform that does nothing.
 * 
 * @author bjculkin
 *
 */
class TDHIdentity implements TDHTransform {
	@Override
	public TDHPoint transform(TDHPoint punkt) {
		return punkt;
	}

	@Override
	public String toString() {
		return "TDHIdentity []";
	}
}

/**
 * A transform that does coordinate translation.
 * 
 * @author bjculkin
 *
 */
class TDHTranslate implements TDHTransform {
	/**
	 * The amount to translate the x-coordinate by.
	 */
	public final double h;
	/**
	 * The amount to translate the y-coordinate by.
	 */
	public final double k;

	/**
	 * Create a new translation transform.
	 * 
	 * @param h
	 *            The amount to translate x by.
	 * @param k
	 *            The amount to translate y by.
	 */
	public TDHTranslate(double h, double k) {
		this.h = h;
		this.k = k;
	}

	@Override
	public TDHTransformType type() {
		return TDHTransformType.TRANSLATE;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		double x = punkt.x + (punkt.z * h);
		double y = punkt.y + (punkt.z * k);

		return new TDHPoint(x, y, punkt.z);
	}

	@Override
	public double[][] matrix() {
		return new double[][] { new double[] { 1, 0, 0 }, new double[] { 0, 1, 0 }, new double[] { h, k, 1 } };
	}

	@Override
	public TDHTransform invert() {
		return new TDHTranslate(-h, -k);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(h);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(k);
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
		TDHTranslate other = (TDHTranslate) obj;
		if (Double.doubleToLongBits(h) != Double.doubleToLongBits(other.h))
			return false;
		if (Double.doubleToLongBits(k) != Double.doubleToLongBits(other.k))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDHTranslate [h=" + h + ", k=" + k + "]";
	}
}

/**
 * Transform that does coordinate scaling.
 * 
 * @author bjculkin
 *
 */
class TDHScale implements TDHTransform {
	/**
	 * Amount to scale x by.
	 */
	public final double sx;
	/**
	 * Amount to scale y by.
	 */
	public final double sy;
	/**
	 * Amount to scale the homogeneous coordinate by.
	 */
	public final double sz;

	/**
	 * Create a new scaling that scales each coordinate by an equal amount
	 * 
	 * @param factor
	 *            The amount to scale the coordinates by.
	 */
	public TDHScale(double factor) {
		this(factor, factor);
	}

	/**
	 * Create a new scaling that scales each coordinate separately.
	 * 
	 * @param sx
	 *            The amount to scale x by.
	 * @param sy
	 *            The amount to scale y by.
	 */
	public TDHScale(double sx, double sy) {
		this(sx, sy, 1);
	}

	/**
	 * Create a new scaling that scales each coordinate separately.
	 * 
	 * Includes the homogeneous coordinate.
	 * 
	 * @param sx
	 *            The amount to scale x by.
	 * @param sy
	 *            The amount to scale y by.
	 * @param sz
	 *            The amount to scale the homogeneous coordinate by.
	 */
	public TDHScale(double sx, double sy, double sz) {
		this.sx = sx;
		this.sy = sy;
		this.sz = sz;
	}

	@Override
	public TDHTransformType type() {
		return TDHTransformType.SCALE;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		double x = punkt.x * sx;
		double y = punkt.y * sy;
		double z = punkt.z * sz;

		return new TDHPoint(x, y, z);
	}

	@Override
	public double[][] matrix() {
		return new double[][] { new double[] { sx, 0, 0 }, new double[] { 0, sy, 0 }, new double[] { 0, 0, sz } };
	}

	public TDHTransform invert() {
		return new TDHScale(1 / sx, 1 / sy, 1 / sy);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(sx);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(sy);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(sz);
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
		TDHScale other = (TDHScale) obj;
		if (Double.doubleToLongBits(sx) != Double.doubleToLongBits(other.sx))
			return false;
		if (Double.doubleToLongBits(sy) != Double.doubleToLongBits(other.sy))
			return false;
		if (Double.doubleToLongBits(sz) != Double.doubleToLongBits(other.sz))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDHScale [sx=" + sx + ", sy=" + sy + ", sz=" + sz + "]";
	}
}

class TDHRotation implements TDHTransform {
	public final double theta;

	public TDHRotation(double theta) {
		this.theta = theta;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		double x = (punkt.x * Math.cos(theta)) - (punkt.y * Math.sin(theta));
		double y = (punkt.x * Math.sin(theta)) - (punkt.y * Math.cos(theta));

		return new TDHPoint(x, y, punkt.z);
	}

	public double[][] matrix() {
		return new double[][] { new double[] { Math.cos(theta), Math.sin(theta), 0 },
				new double[] { -Math.sin(theta), Math.cos(theta), 0 }, new double[] { 0, 0, 1 } };
	}

	public TDHTransform invert() {
		return new TDHRotation(-theta);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(theta);
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
		TDHRotation other = (TDHRotation) obj;
		if (Double.doubleToLongBits(theta) != Double.doubleToLongBits(other.theta))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDHRotation [theta=" + theta + "]";
	}
}

class TDHXAxisReflection implements TDHTransform {
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

class TDHYAxisReflection implements TDHTransform {
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

class TDHPointRotation extends TDHRotation {
	public final double x0;
	public final double y0;

	public TDHPointRotation(double theta, double x0, double y0) {
		super(theta);

		this.x0 = x0;
		this.y0 = y0;
	}

	@Override
	public TDHTransformType type() {
		return TDHTransformType.ROTATION;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		double x1 = (punkt.x * Math.cos(theta)) - (punkt.y * Math.sin(theta));
		double y1 = (punkt.x * Math.sin(theta)) - (punkt.y * Math.cos(theta));

		double x2 = (-x0 * Math.cos(theta)) + (y0 * Math.sin(theta)) + x0;
		double y2 = (-x0 * Math.sin(theta)) - (y0 * Math.cos(theta)) + y0;

		double x = x1 + (punkt.z * x2);
		double y = y1 + (punkt.z * y2);

		return new TDHPoint(x, y, punkt.z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(x0);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y0);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		TDHPointRotation other = (TDHPointRotation) obj;
		if (Double.doubleToLongBits(x0) != Double.doubleToLongBits(other.x0))
			return false;
		if (Double.doubleToLongBits(y0) != Double.doubleToLongBits(other.y0))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDHPointRotation [x0=" + x0 + ", y0=" + y0 + "]";
	}
}

class TDHLineReflection implements TDHTransform {
	public final double a;
	public final double b;
	public final double c;

	public TDHLineReflection(double a, double b, double c) {
		this.a = a;
		this.b = b;
		this.c = c;
	}

	@Override
	public TDHTransformType type() {
		return TDHTransformType.REFLECTION;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		double com = (a * a - b * b);

		double x = (punkt.x * com) - (2 * a * b * punkt.y) - (2 * a * c * punkt.z);
		double y = (-2 * a * b * punkt.x) + (punkt.y * com) - (2 * b * c * punkt.z);
		double z = (punkt.z * com);

		return new TDHPoint(x, y, z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(a);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(b);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(c);
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
		TDHLineReflection other = (TDHLineReflection) obj;
		if (Double.doubleToLongBits(a) != Double.doubleToLongBits(other.a))
			return false;
		if (Double.doubleToLongBits(b) != Double.doubleToLongBits(other.b))
			return false;
		if (Double.doubleToLongBits(c) != Double.doubleToLongBits(other.c))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDHLineReflection [a=" + a + ", b=" + b + ", c=" + c + "]";
	}
}

class TDHShear implements TDHTransform {
	public final double shx;
	public final double shy;

	@Override
	public TDHTransformType type() {
		return TDHTransformType.SHEAR;
	}

	public TDHShear(double shx, double shy) {
		this.shx = shx;
		this.shy = shy;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		double x = punkt.x + (punkt.y * shx);
		double y = punkt.y + (punkt.x * shy);

		return new TDHPoint(x, y, punkt.z);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(shx);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(shy);
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
		TDHShear other = (TDHShear) obj;
		if (Double.doubleToLongBits(shx) != Double.doubleToLongBits(other.shx))
			return false;
		if (Double.doubleToLongBits(shy) != Double.doubleToLongBits(other.shy))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TDHShear [shx=" + shx + ", shy=" + shy + "]";
	}
}

class TDHMatrix implements TDHTransform {
	public final double[][] mat;

	public TDHMatrix(double[][] mat) {
		super();
		this.mat = mat;
	}

	@Override
	public TDHTransformType type() {
		return TDHTransformType.MATRIX;
	}

	@Override
	public TDHPoint transform(TDHPoint punkt) {
		double x = (punkt.x * mat[0][0]) + (punkt.y * mat[1][0]) + (punkt.z * mat[2][0]);
		double y = (punkt.x * mat[0][1]) + (punkt.y * mat[1][1]) + (punkt.z * mat[2][1]);
		double z = (punkt.x * mat[0][2]) + (punkt.y * mat[1][2]) + (punkt.z * mat[2][2]);

		return new TDHPoint(x, y, z);
	}

	public TDHTransform then(double[][] matr) {
		double[][] ret = new double[3][3];

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				for (int k = 0; k < 3; k++) {
					ret[i][j] += mat[i][k] * matr[k][j];
				}
			}
		}

		return new TDHMatrix(ret);
	}
}

class TDHCombination implements TDHTransform {
	public final List<TDHTransform> forms;

	@Override
	public TDHTransformType type() {
		return TDHTransformType.COMBINATION;
	}

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

class BezierProperties {
	/**
	 * The number of separate points to graph from the curve.
	 */
	public int parts = 100;

	/**
	 * The multiplier to apply to coordinates.
	 */
	public double scale = 5;

	/**
	 * The colors for varying parts of the curve.
	 */
	public Color curveColor = Color.BLACK;
	public Color pointColor = Color.RED;
	public Color boxColor = Color.GREEN;

	public BezierProperties() {
	}

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
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BezierProperties other = (BezierProperties) obj;
		if (boxColor == null) {
			if (other.boxColor != null)
				return false;
		} else if (!boxColor.equals(other.boxColor))
			return false;
		if (curveColor == null) {
			if (other.curveColor != null)
				return false;
		} else if (!curveColor.equals(other.curveColor))
			return false;
		if (parts != other.parts)
			return false;
		if (pointColor == null) {
			if (other.pointColor != null)
				return false;
		} else if (!pointColor.equals(other.pointColor))
			return false;
		if (Double.doubleToLongBits(scale) != Double.doubleToLongBits(other.scale))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BezierProperties [parts=" + parts + ", scale=" + scale + ", curveColor=" + curveColor + ", pointColor="
				+ pointColor + ", boxColor=" + boxColor + "]";
	}
}