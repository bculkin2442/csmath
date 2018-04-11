package bezier;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;

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