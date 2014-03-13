package edu.uci.ics.mattg.ui;

import java.awt.Color;
import java.awt.ItemSelectable;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.Icon;
import javax.swing.JButton;

public class NakedButton extends JButton implements ItemSelectable {
	/**
	 * 
	 * @author Matt Gardner
	 * 
	 * This class implements a naked JButton with no border and transparent background. The button is
	 * selectable with blue font when selected, and black font when not, for use with a ButtonGroup.
	 * 
	 */
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NakedButton(String text) {
		super(text);

		setFocusable(false);
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
		setModel(new SelectableButtonModel());

		// Implement selected state
		addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED)
					NakedButton.this.setForeground(Color.BLUE);
				else
					NakedButton.this.setForeground(Color.BLACK);
			}
		});
	}

	public NakedButton(Icon icon) {
		super(icon);

		setFocusable(false);
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
	}
}