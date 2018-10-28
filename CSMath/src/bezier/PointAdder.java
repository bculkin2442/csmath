package bezier;

import java.awt.BorderLayout;
import java.awt.Dialog.ModalityType;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import bezier.geom.Bezier;
import bezier.geom.TDPoint;

/**
 * Listener for adding points to a bezier curve.
 * 
 * @author bjculkin
 *
 */
public class PointAdder implements ActionListener {
	final DefaultListModel<TDPoint> pointModel;
	private final JFrame fram;

	Bezier curve;

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