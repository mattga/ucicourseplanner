package edu.uci.ics.mattg.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import edu.uci.ics.mattg.collections.Map;
/**
 * 
 * @author Matt Gardner
 * 
 * This class dynamically displays items of type AbstractButton based off a map
 * with strings to search, and their corresponding button component.
 * 
 */

public class DynamicMapDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField searchField;
	private JLabel selectLabel, searchIcon;
	private JPanel listPanel, nPanel, buttonPanel, searchPanel;
//	private DigitalTree courseDT;
	private PanelButtonModel panelModel;
	private JButton done;
	private Map<String,? extends AbstractButton> map;

	public DynamicMapDialog(JFrame parent, String label, String title, Map<String,? extends AbstractButton> map) {
		super(parent);
		this.map = map;
		
		// Setup swing components
		searchIcon = new JLabel(createImageIcon("1380024235_search.png", ""));
		searchIcon.setSize(20,20);
		
		listPanel = new JPanel();
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
		
		panelModel = new PanelButtonModel(listPanel);
		panelModel.addAll(map.values());
		
		searchField = new JTextField(30);
		searchField.addKeyListener(new MapSearchKeyListener(map, searchField, panelModel));

		selectLabel = new JLabel(label);
		
		searchPanel = new JPanel();
		searchPanel.setLayout(new BorderLayout());
		searchPanel.add(searchIcon, BorderLayout.WEST);
		searchPanel.add(searchField);
		
		nPanel = new JPanel();
		nPanel.setLayout(new GridLayout(2,1));
		nPanel.add(selectLabel);
		nPanel.add(searchPanel);
		
		JScrollPane scrollPane = new JScrollPane(listPanel);
		
		done = new JButton("Done");
		done.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				DynamicMapDialog.this.setVisible(false);
			}
		});
		
		buttonPanel = new JPanel();
		buttonPanel.add(done);
		
		this.setLayout(new BorderLayout());
		this.add(nPanel, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setTitle(title);
		this.setSize(350, 400);
	}
	
	public void updateModel() {
		panelModel.clear();
		panelModel.addAll(map.values());
	}

	/** Returns an ImageIcon, or null if the path was invalid. */
	protected ImageIcon createImageIcon(String path,
	                                           String description) {
	    java.net.URL imgURL = getClass().getClassLoader().getResource(path);
	    if (imgURL != null) {
	        return new ImageIcon(imgURL, description);
	    } else {
	        System.err.println("Couldn't find file: " + path);
	        return null;
	    }
	}
}
