package edu.uci.ics.mattg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScheduleGenDialog extends JDialog {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static JLabel startTerm, endTerm;
	private static JComboBox<String> startQuarter, endQuarter;
	private static JComboBox<Integer> startYear, endYear;
	private static JButton ok, cancel;
	private static JPanel buttonPanel, startPanel, endPanel, inputPanel;
	
	public ScheduleGenDialog (JFrame parent) {
		super(parent);
		
		startTerm = new JLabel("Schedule from: ");
		
		DefaultComboBoxModel<String> startQuarterModel = new DefaultComboBoxModel<String>(new String[]{"Fall", "Winter", "Spring"});//, "Summer 5 Week I", "Summer 5 Week II", "Summer 10 Week"});
		startQuarter = new JComboBox<String>(startQuarterModel); 
		
		int year = Calendar.getInstance().get(Calendar.YEAR);
		DefaultComboBoxModel<Integer> startYearModel = new DefaultComboBoxModel<Integer>();
		for(int i = 0; i < 10; i++)
			startYearModel.addElement(Integer.valueOf(year+i));
		startYear = new JComboBox<Integer>(startYearModel);
		
		endTerm = new JLabel("Schedule to: ");

		DefaultComboBoxModel<String> endQuarterModel2 = new DefaultComboBoxModel<String>(new String[]{"Fall", "Winter", "Spring"});//, "Summer 5 Week I", "Summer 5 Week II", "Summer 10 Week"});
		endQuarter = new JComboBox<String>(endQuarterModel2);
		
		DefaultComboBoxModel<Integer> endYearModel2 = new DefaultComboBoxModel<Integer>();
		for(int i = 0; i < 10; i++)
			endYearModel2.addElement(Integer.valueOf(year+i));
		endYear = new JComboBox<Integer>(endYearModel2);
		
		startPanel = new JPanel();
		startPanel.add(startTerm);
		startPanel.add(startQuarter);
		startPanel.add(startYear);

		endPanel = new JPanel();
		endPanel.add(endTerm);
		endPanel.add(endQuarter);
		endPanel.add(endYear);
		
		inputPanel = new JPanel();
		inputPanel.add(startPanel);
		inputPanel.add(endPanel);
		
		ok = new JButton("Ok");
		ok.addActionListener(new ScheduleGenActionListener(this, startQuarter, endQuarter, startYear, endYear));
		
		cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				ScheduleGenDialog.this.setVisible(false);
			}
		});
		
		buttonPanel = new JPanel();
		buttonPanel.add(ok);
		buttonPanel.add(cancel);
		
		this.add(inputPanel, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		this.setMinimumSize(new Dimension(375, 150));
		this.setTitle("Generate Course Schedule");
	}
}
