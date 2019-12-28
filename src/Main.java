import java.sql.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.*;

class Main {
	static Connection connection;
	public static void main(String args[]){
        try {
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/college_database", "root", "");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		mainscreen();
	}

	public static void mainscreen()
	{
		final JFrame frame = new JFrame("My First GUI");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(600,600);
		frame.getContentPane().setLayout(null);
		
		JButton btnStudent = new JButton("Student");
		btnStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new StudentDashboard(connection);
			}
		});
		btnStudent.setBounds(235, 178, 114, 25);
		frame.getContentPane().add(btnStudent);
		
		JButton btnFaculty = new JButton("Faculty");
		btnFaculty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new FacultyDashboard(connection);
			}
		});
		btnFaculty.setBounds(235, 253, 114, 25);
		frame.getContentPane().add(btnFaculty);
		
		JButton btnAdmin = new JButton("Admin");
		btnAdmin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				new AdminDashboard(connection);
			}
		});
		btnAdmin.setBounds(235, 331, 114, 25);
		frame.getContentPane().add(btnAdmin);
		
		frame.setVisible(true);
	}

}
