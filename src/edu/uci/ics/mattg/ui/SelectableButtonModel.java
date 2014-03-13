package edu.uci.ics.mattg.ui;

import javax.swing.DefaultButtonModel;

public class SelectableButtonModel extends DefaultButtonModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void setPressed(boolean b) {
		super.setPressed(b);
		this.getGroup().setSelected(this, true);
	}
}
