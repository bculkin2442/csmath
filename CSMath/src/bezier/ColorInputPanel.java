package bezier;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Panel for inputting colors.
 */
public class ColorInputPanel extends JPanel {
	private static final long serialVersionUID = 5201595672794938745L;

	/**
	 * The color picker being used.
	 */
	public final JColorChooser picker;

	/**
	 * Create a color picker.
	 * @param label The label for the color.
	 */
	public ColorInputPanel(String label) {
		this(Color.WHITE, label);
	}

	/**
	 * Create a color picker.
	 * @param init The initial color to use.
	 * @param label The label for the color.
	 */
	public ColorInputPanel(Color init, String label) {
		picker = new JColorChooser(init);

		setLayout(new BorderLayout());

		JLabel lab = new JLabel(label);

		add(lab, BorderLayout.LINE_START);
		add(picker, BorderLayout.CENTER);
	}
}