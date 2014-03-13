package edu.uci.ics.mattg;

import javax.swing.JCheckBox;

	public class SelectionSetItem extends JCheckBox {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private int id;
		private String set;
		
		public SelectionSetItem(int id, String set) {
			this.id = id;
			this.set = set;

			this.setText("<html><b>Set #" + id + ":</b> " + set);
		}
		
		public void setID(int id) {
			this.id = id;
		}
		
		public int getID() {
			return id;
		}
		
		public void setSet(String set) {
			this.set = set;
		}

		public String getSet() {
			return set;
		}
	}