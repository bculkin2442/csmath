package bezier;

import javax.swing.JComponent;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

/**
 * Listener to repaint a component whenever a list changes.
 * 
 * @author bjculkin
 *
 */
public class CanvasRepainter implements ListDataListener {
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