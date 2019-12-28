import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JSeparator;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.SwingConstants;

public class FacultyDashboard {

	private JFrame frame;
	private JTextField faculty_id;
	private JLabel info_name;
	private JTextField student_roll_num;
	private JButton btnGo;
	private JTextField marks;
	private JLabel lblMarks;
	private JLabel student_1;
	private JLabel marks_1;
	private JLabel marks_2;
	private JLabel student_2;
	private JLabel student_3;
	private JLabel marks_3;
	private JLabel student_4;
	private JLabel marks_4;
	private JLabel student_5;
	private JLabel marks_5;
	private JButton average_marks_button;
	private JLabel subject_1;
	private JLabel sub_marks_1;
	private JLabel subject_2;
	private JLabel sub_marks_2;
	private JLabel subject_3;
	private JLabel sub_marks_3;

	public final String FACULTY_INFO = "SELECT faculty.*, departments.name\n" +
            "FROM faculty INNER JOIN departments\n" +
            "ON faculty.department_id = departments.id\n" +
            "WHERE faculty.id = FACULTY_ID";

	public final String INSERT_MARKS = "INSERT INTO marks(student_roll_num, subject_id, marks)\n" +
            "VALUES (STUDENT_ROLL_NUM, SUBJECT_ID, MARKS)";

	public final String SUBJECT_MARKS = "SELECT students.first_name, students.last_name, marks.marks FROM marks\n" +
            "JOIN students ON students.roll_num = marks.student_roll_num\n" +
            "JOIN subjects ON subjects.id = marks.subject_id\n" +
            "WHERE subjects.id = SUBJECT_ID\n" +
            "AND (subjects.start_date < datediff(SEM_1, students.admission_date)\n" +
            "AND subjects.end_date > datediff(SEM_1, students.admission_date))\n" +
            "OR (subjects.start_date < datediff(SEM_2, students.admission_date)\n" +
            "AND subjects.end_date > datediff(SEM_2, students.admission_date))\n" +
            "ORDER BY marks.marks DESC\n" + 
            "LIMIT 5";

	public final String AVERAGE_MARKS = "SELECT subjects.name, avg(marks.marks) FROM marks\n" +
            "JOIN subjects ON subjects.id = marks.subject_id\n" +
            "JOIN students ON students.roll_num = marks.student_roll_num\n" +
            "WHERE subjects.faculty_id = FACULTY_ID\n" +
            "AND (subjects.start_date < datediff(SEM_1, students.admission_date)\n" +
            "AND subjects.end_date > datediff(SEM_1, students.admission_date))\n" +
            "OR (subjects.start_date < datediff(SEM_2, students.admission_date)\n" +
            "AND subjects.end_date > datediff(SEM_2, students.admission_date))\n" +
            "GROUP BY subjects.name\n" +
            "HAVING avg(marks.marks) < 100";
	
	public final String GET_SUBJECTS = "SELECT subjects.name, subjects.id FROM subjects WHERE faculty_id = FACULTY_ID";
	
	Connection connection;
	String id;
	int[] subject_ids = new int[3];
	private JComboBox enter_marks_subject_combo;
	private JComboBox see_marks_subject_combo;
	private JButton btnNewButton;
	private JLabel info_department;
	private JLabel info_subject_1;
	private JLabel info_subject_2;
	private JLabel info_subject_3;
	private JButton btnClose;
	private JLabel lblYear;
	private JTextField batch;
	private JLabel label;
	private JTextField average_marks_year;
	private JLabel enter_marks_conf;

	public FacultyDashboard(Connection connection) {
		this.connection = connection;
		initialize();
		frame.setVisible(true);
	}
	
	public void getInfo() {
        try {
            Statement statement = connection.createStatement();
            String query = FACULTY_INFO.replace("FACULTY_ID", String.valueOf(id));
            ResultSet set = statement.executeQuery(query);

            System.out.println();
            int rollNum;
            String firstName, lastName, admissionDate;

            while (set.next()) {
                rollNum = set.getInt(1);
                firstName = set.getString(2);
                lastName = set.getString(3);
                String dept = set.getString(6);
                info_name.setText(firstName + " " + lastName);
                info_department.setText(dept);
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorWindow();
        }
	}
	
	public void getSubjects() {
		try {
            Statement statement = connection.createStatement();
            String query = GET_SUBJECTS.replace("FACULTY_ID", String.valueOf(id));
            ResultSet set = statement.executeQuery(query);

            System.out.println();

			info_subject_1.setText("");
			info_subject_2.setText("");
			info_subject_3.setText("");

            int counter = 1;
            while (set.next()) {
        		subject_ids[counter -1] = set.getInt(2);
            	switch (counter) {
            		case 1:
            			info_subject_1.setText(set.getString(1));
            			enter_marks_subject_combo.addItem(set.getString(1));
            			see_marks_subject_combo.addItem(set.getString(1));
            			break;
            			
            		case 2:
            			info_subject_2.setText(set.getString(1));
            			enter_marks_subject_combo.addItem(set.getString(1));
            			see_marks_subject_combo.addItem(set.getString(1));
            			break;
            			
            		case 3:
            			info_subject_3.setText(set.getString(1));
            			enter_marks_subject_combo.addItem(set.getString(1));
            			see_marks_subject_combo.addItem(set.getString(1));
            			break;
            	}
            	counter++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorWindow();
        }
	}
	
	public void enterMarks(int roll_num, int subject_id, int marks) {
        try {
            Statement statement = connection.createStatement();
            String query = INSERT_MARKS.replace("STUDENT_ROLL_NUM", String.valueOf(roll_num));
            query = query.replace("SUBJECT_ID", String.valueOf(subject_id));
            query = query.replace("MARKS", String.valueOf(marks));
            statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorWindow();
        }
    }
	
	public void enterMarksViaProcedure(int roll_num, int subject_id, int marks) {
		try {
			CallableStatement callableStatement = connection.prepareCall("{call insert_marks_1(?,?,?)}");
            callableStatement.setInt(1, subject_id);
            callableStatement.setInt(2, roll_num);
            callableStatement.setInt(3, marks);
            callableStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorWindow();
        }
	}
	
	public void getSubjectMarksList(int subject_id, int year) {
        try {
            Statement statement = connection.createStatement();
            String query = SUBJECT_MARKS
            		.replace("SUBJECT_ID", String.valueOf(subject_id))
            		.replace("SEM_1", "'" + String.valueOf(year) + "-09-10'")
    				.replace("SEM_2", "'" + String.valueOf(year + 1) + "-03-10'");

            
            ResultSet set = statement.executeQuery(query);

            student_1.setText("");
            marks_1.setText("");
            student_2.setText("");
            marks_2.setText("");
            student_3.setText("");
            marks_3.setText("");
            student_4.setText("");
            marks_4.setText("");
            student_5.setText("");
            marks_5.setText("");
            
            int counter = 1;
            while (set.next()) {
            	
            	switch (counter) {
            		case 1:
            			student_1.setText(set.getString(1) + " " + set.getString(2));
            			marks_1.setText(set.getString(3));
            			break;
            		
            		case 2:
            			student_2.setText(set.getString(1) + " " + set.getString(2));
            			marks_2.setText(set.getString(3));
            			break;
            			
            		case 3:
            			student_3.setText(set.getString(1) + " " + set.getString(2));
            			marks_3.setText(set.getString(3));
            			break;
            			
            		case 4:
            			student_4.setText(set.getString(1) + " " + set.getString(2));
            			marks_4.setText(set.getString(3));
            			break;
            			
            		case 5:
            			student_5.setText(set.getString(1) + " " + set.getString(2));
            			marks_5.setText(set.getString(3));
            			break;
            	}
            	
            	counter++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorWindow();
        }
    }
	
	public void getSubjectsAverageMarks(int year) {
        try {
            Statement statement = connection.createStatement();
            String query = AVERAGE_MARKS
            		.replace("FACULTY_ID", String.valueOf(id))
            		.replace("SEM_1", "'" + String.valueOf(year) + "-09-10'")
    				.replace("SEM_2", "'" + String.valueOf(year + 1) + "-03-10'");

            ResultSet set = statement.executeQuery(query);

            subject_1.setText("");
			sub_marks_1.setText("");
			subject_2.setText("");
			sub_marks_2.setText("");
			subject_3.setText("");
			sub_marks_3.setText("");
            
            int counter = 1;
            while (set.next()) {
            	switch (counter) {
            		case 1:
            			subject_1.setText(set.getString(1));
            			sub_marks_1.setText(set.getString(2));
            			break;
            			
            		case 2:
            			subject_2.setText(set.getString(1));
            			sub_marks_2.setText(set.getString(2));
            			break;
            		
            		case 3:
            			subject_3.setText(set.getString(1));
            			sub_marks_3.setText(set.getString(2));
            			break;
            	
            	}
            	counter++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorWindow();
        }
    }


	private void initialize() {
		frame = new JFrame("Faculty Dashboard");
		frame.setBounds(100, 100, 876, 586);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblEnterFacultyId = new JLabel("Faculty ID:");
		lblEnterFacultyId.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEnterFacultyId.setBounds(56, 33, 86, 14);
		frame.getContentPane().add(lblEnterFacultyId);
		
		faculty_id = new JTextField();
		faculty_id.setBounds(146, 31, 86, 20);
		frame.getContentPane().add(faculty_id);
		faculty_id.setColumns(10);
		
		JButton show_info_button = new JButton("Show info");
		show_info_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				id = faculty_id.getText();
				see_marks_subject_combo.removeAllItems();
				enter_marks_subject_combo.removeAllItems();
				getInfo();
				getSubjects();
			}
		});
		show_info_button.setBounds(244, 29, 115, 23);
		frame.getContentPane().add(show_info_button);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setBounds(96, 65, 46, 14);
		frame.getContentPane().add(lblName);
		
		info_name = new JLabel();
		info_name.setBounds(146, 63, 218, 20);
		frame.getContentPane().add(info_name);
		
		JLabel lblDeptName = new JLabel("Department:");
		lblDeptName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblDeptName.setBounds(35, 93, 107, 14);
		frame.getContentPane().add(lblDeptName);
		
		JLabel lblListOfSubjects = new JLabel("Subjects:");
		lblListOfSubjects.setHorizontalAlignment(SwingConstants.RIGHT);
		lblListOfSubjects.setBounds(58, 118, 84, 14);
		frame.getContentPane().add(lblListOfSubjects);
		
		JLabel lblstudent_roll_num = new JLabel("Student Roll No:");
		lblstudent_roll_num.setBounds(533, 36, 155, 14);
		frame.getContentPane().add(lblstudent_roll_num);
		
		student_roll_num = new JTextField();
		student_roll_num.setBounds(677, 33, 86, 20);
		frame.getContentPane().add(student_roll_num);
		student_roll_num.setColumns(10);
		
		btnGo = new JButton("Enter Marks");
		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int sub_id = subject_ids[enter_marks_subject_combo.getSelectedIndex()];
				enterMarksViaProcedure(Integer.valueOf(student_roll_num.getText()), sub_id, Integer.valueOf(marks.getText()));
				student_roll_num.setText("");
				marks.setText("");
				enter_marks_conf.setText("Marks entered!");
			}
		});
		btnGo.setBounds(588, 136, 137, 23);
		frame.getContentPane().add(btnGo);
		
		marks = new JTextField();
		marks.setBounds(677, 98, 86, 20);
		frame.getContentPane().add(marks);
		marks.setColumns(10);
		
		lblMarks = new JLabel("Marks:");
		lblMarks.setBounds(533, 100, 46, 14);
		frame.getContentPane().add(lblMarks);
		
		student_1 = new JLabel("");
		student_1.setBounds(35, 374, 141, 14);
		frame.getContentPane().add(student_1);
		
		marks_1 = new JLabel("");
		marks_1.setBounds(186, 374, 46, 14);
		frame.getContentPane().add(marks_1);
		
		marks_2 = new JLabel("");
		marks_2.setBounds(186, 403, 46, 14);
		frame.getContentPane().add(marks_2);
		
		student_2 = new JLabel("");
		student_2.setBounds(35, 399, 140, 14);
		frame.getContentPane().add(student_2);
		
		student_3 = new JLabel("");
		student_3.setBounds(35, 428, 141, 14);
		frame.getContentPane().add(student_3);
		
		marks_3 = new JLabel("");
		marks_3.setBounds(186, 428, 46, 14);
		frame.getContentPane().add(marks_3);
		
		student_4 = new JLabel("");
		student_4.setBounds(35, 457, 133, 14);
		frame.getContentPane().add(student_4);
		
		marks_4 = new JLabel("");
		marks_4.setBounds(186, 457, 46, 14);
		frame.getContentPane().add(marks_4);
		
		student_5 = new JLabel("");
		student_5.setBounds(35, 482, 133, 14);
		frame.getContentPane().add(student_5);
		
		marks_5 = new JLabel("");
		marks_5.setBounds(186, 482, 46, 14);
		frame.getContentPane().add(marks_5);
		
		average_marks_button = new JButton("Get Average Marks");
		average_marks_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int year = Integer.valueOf(average_marks_year.getText());
				getSubjectsAverageMarks(year);
			}
		});
		average_marks_button.setBounds(456, 295, 193, 23);
		frame.getContentPane().add(average_marks_button);
		
		subject_1 = new JLabel("");
		subject_1.setBounds(393, 330, 257, 14);
		frame.getContentPane().add(subject_1);
		
		sub_marks_1 = new JLabel("");
		sub_marks_1.setBounds(655, 330, 70, 14);
		frame.getContentPane().add(sub_marks_1);
		
		subject_2 = new JLabel("");
		subject_2.setBounds(393, 355, 257, 14);
		frame.getContentPane().add(subject_2);
		
		sub_marks_2 = new JLabel("");
		sub_marks_2.setBounds(655, 359, 70, 14);
		frame.getContentPane().add(sub_marks_2);
		
		subject_3 = new JLabel("");
		subject_3.setBounds(393, 384, 257, 14);
		frame.getContentPane().add(subject_3);
		
		sub_marks_3 = new JLabel("");
		sub_marks_3.setBounds(655, 384, 70, 14);
		frame.getContentPane().add(sub_marks_3);
		
		enter_marks_subject_combo = new JComboBox();
		enter_marks_subject_combo.setBounds(533, 61, 230, 24);
		frame.getContentPane().add(enter_marks_subject_combo);
		
		see_marks_subject_combo = new JComboBox();
		see_marks_subject_combo.setBounds(35, 265, 220, 24);
		frame.getContentPane().add(see_marks_subject_combo);
		
		btnNewButton = new JButton("Get marks");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				int sub_id = subject_ids[see_marks_subject_combo.getSelectedIndex()];
				int year = Integer.valueOf(batch.getText());
				getSubjectMarksList(sub_id, year);
			}
		});
		btnNewButton.setBounds(92, 337, 114, 25);
		frame.getContentPane().add(btnNewButton);
		
		info_department = new JLabel("");
		info_department.setBounds(146, 91, 213, 16);
		frame.getContentPane().add(info_department);
		
		info_subject_1 = new JLabel("");
		info_subject_1.setBounds(146, 119, 300, 15);
		frame.getContentPane().add(info_subject_1);
		
		info_subject_2 = new JLabel("");
		info_subject_2.setBounds(146, 144, 300, 15);
		frame.getContentPane().add(info_subject_2);
		
		info_subject_3 = new JLabel("");
		info_subject_3.setBounds(146, 170, 300, 15);
		frame.getContentPane().add(info_subject_3);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		btnClose.setBounds(379, 519, 114, 25);
		frame.getContentPane().add(btnClose);
		
		lblYear = new JLabel("Academic year:");
		lblYear.setBounds(35, 299, 115, 15);
		frame.getContentPane().add(lblYear);
		
		batch = new JTextField();
		batch.setBounds(159, 297, 96, 19);
		frame.getContentPane().add(batch);
		batch.setColumns(10);
		
		label = new JLabel("Academic year:");
		label.setBounds(445, 266, 115, 15);
		frame.getContentPane().add(label);
		
		average_marks_year = new JTextField();
		average_marks_year.setColumns(10);
		average_marks_year.setBounds(569, 264, 96, 19);
		frame.getContentPane().add(average_marks_year);
		
		enter_marks_conf = new JLabel("");
		enter_marks_conf.setHorizontalAlignment(SwingConstants.CENTER);
		enter_marks_conf.setBounds(588, 170, 137, 15);
		frame.getContentPane().add(enter_marks_conf);
	}
}
