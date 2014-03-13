package edu.uci.ics.mattg;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;

import edu.uci.ics.mattg.collections.DigitalTree;

public class DigitalTreeKeyListener implements KeyListener {
	private JFrame frame;
	private JTextArea resultsArea;
	private DigitalTree dt;
	private Iterator<String> iter;
	private JTextComponent textComponent;

	// TODO: Abstract to be used generically
	public DigitalTreeKeyListener(DigitalTree dt, JTextComponent textComponent) {
		this.dt = dt;
		this.textComponent = textComponent;

		iter = dt.iterator();

		resultsArea = new JTextArea();
		resultsArea.setEditable(false);
		for (String s : dt.getWords())
			resultsArea.append(s + "\n");

		frame = new JFrame("DigitalTree results list");
		frame.setSize(200, 300);
		frame.add(resultsArea);
		frame.setVisible(true);
	}

	public void keyPressed(KeyEvent e) {

	}

	public void keyTyped(KeyEvent e) {
		resultsArea.setText("");
		int keyPressed = e.getKeyChar();
		System.out.println("Pressed key '" + keyPressed + "'. Words:");
		
		// TODO: queue key presses to iterate with so no keys are missed. new thread iterate with keys from queue?
		// Traverse DigitalTree if a letter, number, space, or special character is typed,
		// otherwise traverse from root using the contents of all 
		if (keyPressed > 31 && keyPressed < 127)
			for (String s : dt.getWordsFromNode(((DigitalTree.DigitalTreeIterator)iter).nextForKey(e.getKeyChar()))) {
				System.out.print(s);
				resultsArea.append(s + "\n");
			}
		else if (textComponent.getText().equals("")) {// Display all words
			((DigitalTree.DigitalTreeIterator)iter).reset();
			for (String s : dt.getWords()) {
				System.out.print(s);
				resultsArea.append(s + "\n");
			}
		}
		else {
			((DigitalTree.DigitalTreeIterator)iter).reset();
			String word = textComponent.getText();
			for(int i = 0; i < word.length(); i++) {
				if(i == word.length()-1)
					for (String s : dt.getWordsFromNode(((DigitalTree.DigitalTreeIterator)iter).nextForKey(word.charAt(i)))) {
						System.out.print(s);
						resultsArea.append(s + "\n");
					}
				else
					((DigitalTree.DigitalTreeIterator)iter).nextForKey(word.charAt(i));
			}
		}
		// TODO: more in depth search after
		System.out.println();
	}

	public void keyReleased(KeyEvent e) {
	}
}
