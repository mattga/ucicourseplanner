package edu.uci.ics.mattg.ui;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

public class RegexTextField extends JTextField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String regex;
	private int maxLength;
	
	public RegexTextField(int columns) {
		super(columns);
		regex = ".*";
		maxLength = -1; // Unlimited
	}

	public RegexTextField(int columns, String regex, int maxLength) {
		super(columns);
		this.regex = regex;
		this.maxLength = maxLength;
	}
	
	public void setRegex(String regex) {
		this.regex = regex;
	}
	
	public String getRegex() {
		return regex;
	}
	
	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}
	
	public int getMaxLength() {
		return maxLength;
	}
	
	@Override
	protected Document createDefaultModel() {
		return new RegexDocument();
	}
	
	private final class RegexDocument extends PlainDocument {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Override
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			if(str == null)
				return;
			
			if(maxLength < 0 && str.matches(regex)) 
				super.insertString(offs, str, a);
			else
				if(offs < maxLength && offs >= 0 && str.matches(regex) && getLength() < maxLength)
					super.insertString(offs, str, a);
		}
	}
}
