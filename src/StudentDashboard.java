import java.awt.EventQueue;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class StudentDashboard {

	private JFrame frame;
	private JTextField stud_roll_num;

	Connection connection;
	String roll_num;
	
	private JTextField student_info_name;
	private JTextField student_info_phone;
	private JTextField student_info_dept;
	private JTextField student_info_admission_date;
	private JTextField student_info_cet_marks;
	
	public final String GET_STUDENT_INFO = "SELECT students.*, departments.name FROM students\n" +
            "JOIN departments\n" +
            "ON students.department_id = departments.id\n" +
            "WHERE roll_num = ROLL_NUMBER";

	public final String GET_STUDENT_MARKS = "SELECT subjects.name, marks.marks FROM marks\n" +
			"JOIN subjects ON marks.subject_id = subjects.id\n" +
            "WHERE marks.student_roll_num = ROLL_NUMBER\n" +
            "AND marks.subject_id IN (SELECT subjects.id FROM subjects, students\n" +
            "WHERE subjects.department_id = students.department_id\n" +
            "AND subjects.start_date < DATE\n" +
            "AND subjects.end_date > DATE\n" +
            "AND students.roll_num = ROLL_NUMBER)";
	
	private JTextField gpa;
	private JTextField subject_5;
	private JTextField marks_5;
	private JTextField marks_4;
	private JTextField subject_4;
	private JTextField subject_3;
	private JTextField marks_3;
	private JTextField marks_2;
	private JTextField subject_2;
	private JTextField subject_1;
	private JTextField semester;
	private JTextField marks_1;
	private JButton btnClose;

	public StudentDashboard(Connection connection) {
		this.connection = connection;
		initialize();
		frame.setVisible(true);
	}
	
	public void getInfo() {
        try {
            Statement statement = connection.createStatement();
            String query = GET_STUDENT_INFO.replace("ROLL_NUMBER", String.valueOf(roll_num));
            ResultSet set = statement.executeQuery(query);

            student_info_name.setText("");
            student_info_phone.setText("");
            student_info_dept.setText("");
            student_info_admission_date.setText("");
            student_info_cet_marks.setText("");
            
            System.out.println();
            int rollNum;
            String firstName, lastName, admissionDate;

            while (set.next()) {
                rollNum = set.getInt(1);
                firstName = set.getString(2);
                lastName = set.getString(3);
                admissionDate = set.getString(7);
                student_info_name.setText(firstName + " " + lastName);
                student_info_phone.setText(set.getString(5));
                student_info_dept.setText(set.getString(8));
                student_info_admission_date.setText(set.getString(6));
                student_info_cet_marks.setText(set.getString(7));
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorWindow();
        }
    }
	
	public void getSubjects(int semester) {
        try {
            Statement statement = connection.createStatement();
            String query = GET_STUDENT_MARKS;
			int date = 5 + 180*(semester - 1);

            query = query.replace("DATE", String.valueOf(date));
            query = query.replace("ROLL_NUMBER", String.valueOf(roll_num));
            ResultSet set = statement.executeQuery(query);

            System.out.println();
            
            subject_1.setText("");
			marks_1.setText("");
			subject_2.setText("");
			marks_2.setText("");
			subject_3.setText("");
			marks_3.setText("");
			subject_4.setText("");
			marks_4.setText("");
			subject_5.setText("");
			marks_5.setText("");

            int counter = 1;
            while (set.next()) {
            	switch(counter) {
            		case 1:
            			subject_1.setText(set.getString(1));
            			marks_1.setText(set.getString(2));
            			break;
            			
            		case 2:
            			subject_2.setText(set.getString(1));
            			marks_2.setText(set.getString(2));
            			break;
            			
            		case 3:
            			subject_3.setText(set.getString(1));
            			marks_3.setText(set.getString(2));
            			break;
            		
            		case 4:
            			subject_4.setText(set.getString(1));
            			marks_4.setText(set.getString(2));
            			break;
            		
            		case 5:
            			subject_5.setText(set.getString(1));
            			marks_5.setText(set.getString(2));
            			break;
            	}
                                
                counter++;
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorWindow();
        }
    }
	
	public void getPointer(int semester) {
		try {
			int date = 5 + 180*(semester - 1);
			CallableStatement callableStatement = connection.prepareCall("{call calculate_pointer(?,?,?)}");
            callableStatement.setInt(1, Integer.valueOf(roll_num));
            callableStatement.setInt(2, date);
            callableStatement.registerOutParameter(3, Types.DOUBLE);
            callableStatement.execute();
            
            gpa.setText(String.format("%.2f", callableStatement.getDouble(3) / 10));
            
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorWindow();
        }
	}

	private void initialize() {
		frame = new JFrame("Student Dashboard");
		frame.setBounds(100, 100, 803, 465);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblEnterRollNumber = new JLabel("Roll no.:");
		lblEnterRollNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblEnterRollNumber.setBounds(68, 48, 77, 14);
		frame.getContentPane().add(lblEnterRollNumber);
		
		stud_roll_num = new JTextField();
		stud_roll_num.setBounds(155, 45, 86, 20);
		frame.getContentPane().add(stud_roll_num);
		stud_roll_num.setColumns(10);
		
		JButton student_info_go_button = new JButton("Show info");
		student_info_go_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				roll_num = stud_roll_num.getText();
				getInfo();
			}
		});
		student_info_go_button.setBounds(155, 74, 133, 23);
		frame.getContentPane().add(student_info_go_button);
		
		JLabel label = new JLabel("Name:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(99, 107, 46, 14);
		frame.getContentPane().add(label);
		
		student_info_name = new JTextField();
		student_info_name.setEditable(false);
		student_info_name.setColumns(10);
		student_info_name.setBounds(155, 104, 133, 20);
		frame.getContentPane().add(student_info_name);
		
		student_info_phone = new JTextField();
		student_info_phone.setEditable(false);
		student_info_phone.setColumns(10);
		student_info_phone.setBounds(155, 129, 133, 20);
		frame.getContentPane().add(student_info_phone);
		
		JLabel label_1 = new JLabel("Phone:");
		label_1.setHorizontalAlignment(SwingConstants.RIGHT);
		label_1.setBounds(68, 132, 77, 14);
		frame.getContentPane().add(label_1);
		
		JLabel label_2 = new JLabel("Department:");
		label_2.setHorizontalAlignment(SwingConstants.RIGHT);
		label_2.setBounds(57, 158, 88, 13);
		frame.getContentPane().add(label_2);
		
		student_info_dept = new JTextField();
		student_info_dept.setEditable(false);
		student_info_dept.setColumns(10);
		student_info_dept.setBounds(155, 154, 133, 20);
		frame.getContentPane().add(student_info_dept);
		
		student_info_admission_date = new JTextField();
		student_info_admission_date.setEditable(false);
		student_info_admission_date.setColumns(10);
		student_info_admission_date.setBounds(155, 179, 133, 20);
		frame.getContentPane().add(student_info_admission_date);
		
		JLabel label_3 = new JLabel("Admission Date:");
		label_3.setHorizontalAlignment(SwingConstants.RIGHT);
		label_3.setBounds(34, 182, 111, 14);
		frame.getContentPane().add(label_3);
		
		JLabel label_4 = new JLabel("CET marks:");
		label_4.setHorizontalAlignment(SwingConstants.RIGHT);
		label_4.setBounds(68, 207, 77, 14);
		frame.getContentPane().add(label_4);
		
		student_info_cet_marks = new JTextField();
		student_info_cet_marks.setEditable(false);
		student_info_cet_marks.setColumns(10);
		student_info_cet_marks.setBounds(155, 204, 133, 20);
		frame.getContentPane().add(student_info_cet_marks);
		
		JButton gpa_button = new JButton("Caculate GPA");
		gpa_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getPointer(Integer.valueOf(semester.getText()));
			}
		});
		gpa_button.setBounds(515, 203, 141, 23);
		frame.getContentPane().add(gpa_button);
		
		gpa = new JTextField();
		gpa.setEditable(false);
		gpa.setColumns(10);
		gpa.setBounds(668, 205, 36, 20);
		frame.getContentPane().add(gpa);
		
		subject_5 = new JTextField();
		subject_5.setHorizontalAlignment(SwingConstants.RIGHT);
		subject_5.setEditable(false);
		subject_5.setColumns(10);
		subject_5.setBounds(393, 167, 263, 20);
		frame.getContentPane().add(subject_5);
		
		marks_5 = new JTextField();
		marks_5.setEditable(false);
		marks_5.setColumns(10);
		marks_5.setBounds(668, 167, 36, 20);
		frame.getContentPane().add(marks_5);
		
		marks_4 = new JTextField();
		marks_4.setEditable(false);
		marks_4.setColumns(10);
		marks_4.setBounds(668, 142, 36, 20);
		frame.getContentPane().add(marks_4);
		
		subject_4 = new JTextField();
		subject_4.setHorizontalAlignment(SwingConstants.RIGHT);
		subject_4.setEditable(false);
		subject_4.setColumns(10);
		subject_4.setBounds(393, 142, 263, 20);
		frame.getContentPane().add(subject_4);
		
		subject_3 = new JTextField();
		subject_3.setHorizontalAlignment(SwingConstants.RIGHT);
		subject_3.setEditable(false);
		subject_3.setColumns(10);
		subject_3.setBounds(393, 117, 263, 20);
		frame.getContentPane().add(subject_3);
		
		marks_3 = new JTextField();
		marks_3.setEditable(false);
		marks_3.setColumns(10);
		marks_3.setBounds(668, 117, 36, 20);
		frame.getContentPane().add(marks_3);
		
		marks_2 = new JTextField();
		marks_2.setEditable(false);
		marks_2.setColumns(10);
		marks_2.setBounds(668, 92, 36, 20);
		frame.getContentPane().add(marks_2);
		
		subject_2 = new JTextField();
		subject_2.setHorizontalAlignment(SwingConstants.RIGHT);
		subject_2.setEditable(false);
		subject_2.setColumns(10);
		subject_2.setBounds(393, 92, 263, 20);
		frame.getContentPane().add(subject_2);
		
		subject_1 = new JTextField();
		subject_1.setHorizontalAlignment(SwingConstants.RIGHT);
		subject_1.setEditable(false);
		subject_1.setColumns(10);
		subject_1.setBounds(393, 67, 263, 20);
		frame.getContentPane().add(subject_1);
		
		JLabel label_5 = new JLabel("Semester");
		label_5.setBounds(465, 45, 86, 14);
		frame.getContentPane().add(label_5);
		
		semester = new JTextField();
		semester.setColumns(10);
		semester.setBounds(551, 42, 46, 20);
		frame.getContentPane().add(semester);
		
		marks_1 = new JTextField();
		marks_1.setEditable(false);
		marks_1.setColumns(10);
		marks_1.setBounds(668, 67, 36, 20);
		frame.getContentPane().add(marks_1);
		
		JButton show_button = new JButton("Show");
		show_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getSubjects(Integer.valueOf(semester.getText()));
			}
		});
		show_button.setBounds(607, 41, 97, 23);
		frame.getContentPane().add(show_button);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		btnClose.setBounds(329, 398, 114, 25);
		frame.getContentPane().add(btnClose);
	}
}
