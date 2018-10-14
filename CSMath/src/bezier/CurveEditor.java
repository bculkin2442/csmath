package bezier;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import bezier.geom.Bezier;

/**
 * Do editing of curve properties.
 */
public class CurveEditor implements ActionListener {
	private Bezier curve;
	private JFrame fram;
	private BezierPanel canvas;

	/**
	 * Create a new curve property editor.
	 * @param fram The frame we came from.
	 * @param currentCurve The curve being edited.
	 * @param canvas The panel the curve is being drawn on.
	 */
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