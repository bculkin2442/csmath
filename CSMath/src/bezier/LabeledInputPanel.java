package bezier;

import java.awt.BorderLayout;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * A component for getting formatted input with a label.
 * 
 * @author bjculkin
 *
 */
public class LabeledInputPanel extends JPanel {
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