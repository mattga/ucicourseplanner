package edu.uci.ics.mattg.ui;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.AbstractButton;
import javax.swing.text.JTextComponent;

import edu.uci.ics.mattg.collections.Map;
/**
 * 
 * @author Matt Gardner
 * 
 * This class listens to keypresses and dynamically searches map key strings,
 * adding their corresponding AbstractButtons to the panel model.
 * 
 */

public class MapSearchKeyListener implements KeyListener {
	private JTextComponent textComponent;
	private Map<String,? extends AbstractButton> map;
	private PanelButtonModel model;

	public MapSearchKeyListener(Map<String,? extends AbstractButton> map, JTextComponent textComponent, PanelButtonModel model) {
		this.map = map;
		this.model = model;
		this.textComponent = textComponent;
	}

	public void keyPressed(KeyEvent e) {

	}
	
	// TODO: Least common sequence search with limit after substring search? (Adds O(nm), n=input string, m=map key string)
	@SuppressWarnings("unchecked")
	public void keyReleased(KeyEvent e) {
		model.clear();
		char keyTyped = e.getKeyChar();
		System.out.println((int)keyTyped);

//				if (keyTyped > 31 && keyTyped < 127)
		for (Map.Entry<String,?> entry : map.entries())
			if (entry.getKey().contains(textComponent.getText().toLowerCase().replaceAll(" ", ""))) {
				System.out.print(entry.getKey() + ",");
				model.addButton((AbstractButton)entry.getValue());
			}
		if (textComponent.getText() == "")
			model.addAll((Iterable<AbstractButton>)map.values());


		System.out.println();
	}

	public void keyTyped(KeyEvent e) {

	}
}
