package edu.uci.ics.mattg;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;

public class ScheduleGenActionListener implements ActionListener {
	private Component parent;
	private JComboBox<String> startQuarter, endQuarter;
	private JComboBox<Integer> startYear, endYear;
	
	public ScheduleGenActionListener(Component parent, JComboBox<String> startQuarter, JComboBox<String> endQuarter, JComboBox<Integer> startYear, JComboBox<Integer> endYear) {
		this.parent = parent;
		this.startQuarter = startQuarter;
		this.endQuarter = endQuarter;
		this.startYear = startYear;
		this.endYear = endYear;
	}
	
	public void actionPerformed (ActionEvent e) {
		String major = null;
		String specialization = null;
		for(DegreeItem d : ProgramState.degreeMap.values())
			if(d.isSelected()) {
				major = d.getMajor();
				specialization = d.getSpecialization();
			}

		if(major == null) {
			JOptionPane.showMessageDialog(parent, "Must select a degree to schedule for", "No degree selected", JOptionPane.ERROR_MESSAGE);
			((JDialog)parent).setVisible(false);
		}
		else {
			CourseSchedulerGRBModeler csm = new CourseSchedulerGRBModeler(major, specialization, (String)startQuarter.getSelectedItem(), (String)endQuarter.getSelectedItem(), (int)startYear.getSelectedItem(), (int)endYear.getSelectedItem());
			System.out.println((String)startQuarter.getSelectedItem() + " " + (String)endQuarter.getSelectedItem() + " " + (int)startYear.getSelectedItem() + " " + (int)endYear.getSelectedItem());
			csm.model();
			if(csm.optimize()) {
				System.out.println("\tSchedule:");
				for(String s : ProgramState.courseSchedule.elements()) 
					System.out.println(s);
				new CourseScheduleDialog((JDialog)parent, (String)startQuarter.getSelectedItem(),(String)endQuarter.getSelectedItem(), (int)startYear.getSelectedItem(), (int)endYear.getSelectedItem()).setVisible(true);
			} else {
				JOptionPane.showMessageDialog(parent, "Planner could not find a valid schedule within the provided quarters.", "No feasible schedule", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}
