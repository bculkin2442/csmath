package bezier;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import bezier.geom.TDPoint;

/**
 * Renderer for TDPoints in JLists.
 * 
 * @author bjculkin
 *
 */
public final class TDPointRenderer extends JLabel implements ListCellRenderer<TDPoint> {
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