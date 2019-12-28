import java.awt.EventQueue;
import java.sql.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;

public class AdminDashboard {

	private JFrame frame;
	private JTextField add_stud_first_name;
	private JLabel lblRollNumber;
	private JTextField add_stud_roll_num;
	private JLabel label;
	private JTextField add_stud_phone;
	private JTextField add_stud_cet_marks;
	private JLabel lblCetMarks;
	private JLabel lblChangeHodOf;
	private JLabel lblNewHod;
	private JTextField change_hod_faculty_id;
	private JButton add_stud_button;
	private JButton change_hod_button;
	private JLabel lblUpdateStudentInfo;
	private JLabel lblRollNoOf;
	private JTextField delete_roll_num;
	private JButton update_sutd_details;
	
	public final String ADD_STUDENT = "INSERT INTO students(roll_num, first_name, last_name, department_id, phone, admission_date, cet_marks)\n" +
            							"VALUES (ROLL_NUMBER, 'F_NAME', 'L_NAME', D_ID, 'PHONE_NUM', curdate(), C_MARKS)";

	public final String DELETE_STUDENT = "DELETE FROM students\n" +
										"WHERE roll_num = ROLL_NUMBER";

	public final String MAKE_HOD = "UPDATE departments\n" +
									"SET hod_id = FACULTY_ID\n" +
									"WHERE id = DEPARTMENT_ID";
	
	public final String CURRENT_HOD = "SELECT faculty.first_name, faculty.last_name\n" + 
									"FROM faculty JOIN departments ON departments.hod_id = faculty.id\n" +
									"WHERE departments.id = DEPT_ID";

	Connection connection;
	private JLabel lblLastName_1;
	private JTextField add_stud_last_name;
	private JComboBox departments_combo;
	private JComboBox add_stud_department_combo;
	int[] dept_ids = new int[2];
	private JButton btnClose;
	private JButton btnGetCurrentHod;
	private JLabel current_hod;
	private JLabel add_stud_conf;
	private JLabel delete_stud_conf;

	public AdminDashboard(Connection connection) {
		this.connection = connection;
		initialize();
		frame.setVisible(true);
		dept_ids[0] = 1;
		dept_ids[1] = 3;
	}
	
	public void addStudent(int roll_num, String firstName, String lastName, String phone, int department_id, int cet_marks) {
        try {
            Statement statement = connection.createStatement();
            String query = ADD_STUDENT
                .replace("ROLL_NUMBER", String.valueOf(roll_num))
                .replace("F_NAME", firstName)
                .replace("L_NAME", lastName)
                .replace("D_ID", String.valueOf(department_id))
                .replace("PHONE_NUM", phone)
                .replace("C_MARKS", String.valueOf(cet_marks));
            statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorWindow();
        }
    }
	
	public void deleteStudent(int roll_num) {
        try {
            Statement statement = connection.createStatement();
            String query = DELETE_STUDENT.replace("ROLL_NUMBER", String.valueOf(roll_num));
            statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorWindow();
        }
    }
	
	public void getCurrentHOD(int department_id) {
		try {
            Statement statement = connection.createStatement();
            String query = CURRENT_HOD.replace("DEPT_ID", String.valueOf(department_id));
            ResultSet set = statement.executeQuery(query);

            System.out.println();
            String firstName, lastName;

            while (set.next()) {
                firstName = set.getString(1);
                lastName = set.getString(2);
                current_hod.setText(firstName + " " + lastName);
            }

        } catch (Exception e) {
            e.printStackTrace();
            new ErrorWindow();
        }
	}
	
	public void makeHOD(int department_id, int faculty_id) {
        try {
            Statement statement = connection.createStatement();
            String query = MAKE_HOD
                .replace("FACULTY_ID", String.valueOf(faculty_id))
                .replace("DEPARTMENT_ID", String.valueOf(department_id));
            statement.executeUpdate(query);
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorWindow();
        }
    }

	private void initialize() {
		frame = new JFrame("Admin Dashboard");
		frame.setBounds(100, 100, 811, 513);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblAddNewStudent = new JLabel("New student");
		lblAddNewStudent.setBounds(101, 31, 112, 14);
		frame.getContentPane().add(lblAddNewStudent);
		
		JLabel lblName = new JLabel("First Name:");
		lblName.setHorizontalAlignment(SwingConstants.RIGHT);
		lblName.setBounds(22, 77, 126, 14);
		frame.getContentPane().add(lblName);
		
		add_stud_first_name = new JTextField();
		add_stud_first_name.setBounds(159, 75, 153, 20);
		frame.getContentPane().add(add_stud_first_name);
		add_stud_first_name.setColumns(10);
		
		lblRollNumber = new JLabel("Roll number:");
		lblRollNumber.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRollNumber.setBounds(22, 132, 126, 14);
		frame.getContentPane().add(lblRollNumber);
		
		add_stud_roll_num = new JTextField();
		add_stud_roll_num.setBounds(159, 129, 153, 20);
		frame.getContentPane().add(add_stud_roll_num);
		add_stud_roll_num.setColumns(10);
		
		label = new JLabel("Phone:");
		label.setHorizontalAlignment(SwingConstants.RIGHT);
		label.setBounds(22, 158, 126, 14);
		frame.getContentPane().add(label);
		
		add_stud_phone = new JTextField();
		add_stud_phone.setColumns(10);
		add_stud_phone.setBounds(159, 154, 153, 20);
		frame.getContentPane().add(add_stud_phone);
		
		add_stud_cet_marks = new JTextField();
		add_stud_cet_marks.setBounds(159, 181, 153, 20);
		frame.getContentPane().add(add_stud_cet_marks);
		add_stud_cet_marks.setColumns(10);
		
		lblCetMarks = new JLabel("CET marks:");
		lblCetMarks.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCetMarks.setBounds(22, 184, 126, 14);
		frame.getContentPane().add(lblCetMarks);
		
		lblChangeHodOf = new JLabel("Change HoD of a Department");
		lblChangeHodOf.setBounds(412, 31, 220, 14);
		frame.getContentPane().add(lblChangeHodOf);
		
		lblNewHod = new JLabel("Faculty ID:");
		lblNewHod.setBounds(412, 178, 107, 14);
		frame.getContentPane().add(lblNewHod);
		
		change_hod_faculty_id = new JTextField();
		change_hod_faculty_id.setBounds(522, 176, 86, 20);
		frame.getContentPane().add(change_hod_faculty_id);
		change_hod_faculty_id.setColumns(10);
		
		add_stud_button = new JButton("Add Student");
		add_stud_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				addStudent(Integer.valueOf(add_stud_roll_num.getText()), add_stud_first_name.getText(), add_stud_last_name.getText(), add_stud_phone.getText(), dept_ids[add_stud_department_combo.getSelectedIndex()], Integer.valueOf(add_stud_cet_marks.getText()));
				add_stud_conf.setText("Student added!");
				add_stud_roll_num.setText("");
				add_stud_first_name.setText("");
				add_stud_last_name.setText("");
				add_stud_phone.setText("");
				add_stud_cet_marks.setText("");
			}
		});
		add_stud_button.setBounds(68, 242, 150, 23);
		frame.getContentPane().add(add_stud_button);
		
		change_hod_button = new JButton("Change HOD");
		change_hod_button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				makeHOD(dept_ids[departments_combo.getSelectedIndex()], Integer.valueOf(change_hod_faculty_id.getText()));
				getCurrentHOD(dept_ids[departments_combo.getSelectedIndex()]);
				change_hod_faculty_id.setText("");
			}
		});
		change_hod_button.setBounds(437, 207, 129, 23);
		frame.getContentPane().add(change_hod_button);
		
		lblUpdateStudentInfo = new JLabel("Delete Student");
		lblUpdateStudentInfo.setBounds(79, 340, 136, 14);
		frame.getContentPane().add(lblUpdateStudentInfo);
		
		lblRollNoOf = new JLabel("Roll no.:");
		lblRollNoOf.setBounds(24, 371, 95, 14);
		frame.getContentPane().add(lblRollNoOf);
		
		delete_roll_num = new JTextField();
		delete_roll_num.setBounds(129, 368, 86, 20);
		frame.getContentPane().add(delete_roll_num);
		delete_roll_num.setColumns(10);
		
		update_sutd_details = new JButton("Delete");
		update_sutd_details.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				deleteStudent(Integer.valueOf(delete_roll_num.getText()));
				delete_stud_conf.setText("Student deleted");
				delete_roll_num.setText("");
			}
		});
		update_sutd_details.setBounds(106, 400, 112, 23);
		frame.getContentPane().add(update_sutd_details);
		
		lblLastName_1 = new JLabel("Last Name:");
		lblLastName_1.setHorizontalAlignment(SwingConstants.RIGHT);
		lblLastName_1.setBounds(22, 103, 126, 14);
		frame.getContentPane().add(lblLastName_1);
		
		add_stud_last_name = new JTextField();
		add_stud_last_name.setColumns(10);
		add_stud_last_name.setBounds(159, 101, 153, 20);
		frame.getContentPane().add(add_stud_last_name);
		
		departments_combo = new JComboBox();
		departments_combo.addItem("Computer Department");
		departments_combo.addItem("IT Department");
		departments_combo.setBounds(412, 72, 196, 24);
		frame.getContentPane().add(departments_combo);
		
		add_stud_department_combo = new JComboBox();
		add_stud_department_combo.addItem("Computer Department");
		add_stud_department_combo.addItem("IT Department");
		add_stud_department_combo.setBounds(116, 206, 196, 24);
		frame.getContentPane().add(add_stud_department_combo);
		
		btnClose = new JButton("Close");
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				frame.dispose();
			}
		});
		btnClose.setBounds(334, 446, 114, 25);
		frame.getContentPane().add(btnClose);
		
		btnGetCurrentHod = new JButton("Current HOD");
		btnGetCurrentHod.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				getCurrentHOD(dept_ids[departments_combo.getSelectedIndex()]);
			}
		});
		btnGetCurrentHod.setBounds(437, 103, 129, 25);
		frame.getContentPane().add(btnGetCurrentHod);
		
		current_hod = new JLabel("");
		current_hod.setHorizontalAlignment(SwingConstants.CENTER);
		current_hod.setBounds(412, 140, 196, 15);
		frame.getContentPane().add(current_hod);
		
		add_stud_conf = new JLabel("");
		add_stud_conf.setBounds(68, 287, 152, 15);
		frame.getContentPane().add(add_stud_conf);
		
		delete_stud_conf = new JLabel("");
		delete_stud_conf.setBounds(53, 449, 136, 15);
		frame.getContentPane().add(delete_stud_conf);
	}
}
