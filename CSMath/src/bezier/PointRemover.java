package bezier;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;

import bezier.geom.Bezier;
import bezier.geom.TDPoint;

/**
 * Listener to remove points from a bezier curve.
 * 
 * @author bjculkin
 *
 */
public class PointRemover implements ActionListener {
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