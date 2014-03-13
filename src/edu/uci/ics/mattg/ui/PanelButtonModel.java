package edu.uci.ics.mattg.ui;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import edu.uci.ics.mattg.collections.ArrayList;
import edu.uci.ics.mattg.collections.List;
/**
 * 
 * @author Matt Gardner
 * 
 * This class dynamically displays AbstractButtons to the panel passed in.
 * 
 */

public class PanelButtonModel {
	private JPanel panel;
	public List<AbstractButton> buttons;
	
	public PanelButtonModel(JPanel panel) {
		this.panel = panel;
		this.buttons = new ArrayList<AbstractButton>();
	}

	public void addButton(AbstractButton button) {
		panel.add(buttons.add(button));

		panel.getRootPane().revalidate();
		panel.getRootPane().repaint();
	}
	
	public void removeButton(AbstractButton button) {
		buttons.remove(button);
		panel.remove(button);

		panel.getRootPane().revalidate();
		panel.getRootPane().repaint();
	}
	
	public void addAll(Iterable<? extends AbstractButton> buttons) {
		for (AbstractButton button : buttons)
			panel.add(button);

		if (panel.getRootPane() != null) {
			panel.getRootPane().revalidate();
			panel.getRootPane().repaint();
		} else {
			panel.revalidate();
			panel.repaint();
		}
	}
	
	public void clear() {
		panel.removeAll();
		buttons.clear();

		panel.getRootPane().revalidate();
		panel.getRootPane().repaint();
	}
}
