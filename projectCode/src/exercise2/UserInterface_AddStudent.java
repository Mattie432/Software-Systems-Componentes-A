package exercise2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
//import java.awt.Window.Type;

public class UserInterface_AddStudent {

	private JFrame frmAddNewStudent;
	private JTextField textField;
	private Database db;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;

	/**
	 * Create the application.
	 */
	public UserInterface_AddStudent(Database database) {
		db = database;
		initialize();
		this.frmAddNewStudent.setVisible(true);
		
	}
	
	/**
	 * Fills the title box with data from the database
	 * @param comb	JComboBox - The combobox to fill
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void fillTitleCombo(JComboBox comb){
		
		try {
			String sql = "SELECT titleString FROM Titles";
			PreparedStatement stmt = db.queryDatabaseStart(sql);
			ResultSet r = stmt.executeQuery();
			
			while(r.next()){
				comb.addItem(r.getString(1));
			}
			r.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
	}

	/**
	 * Initialise the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		frmAddNewStudent = new JFrame();
		//frmAddNewStudent.setType(Type.UTILITY);
		frmAddNewStudent.setResizable(false);
		frmAddNewStudent.setTitle("Add new student");
		frmAddNewStudent.setBounds(100, 100, 404, 269);
		frmAddNewStudent.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmAddNewStudent.getContentPane().setLayout(null);
		
		JLabel lblStudentId = new JLabel("Student ID");
		lblStudentId.setBounds(20, 14, 92, 14);
		frmAddNewStudent.getContentPane().add(lblStudentId);
		
		textField = new JTextField();
		textField.setBounds(122, 11, 86, 20);
		frmAddNewStudent.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblTitle = new JLabel("Title");
		lblTitle.setBounds(20, 42, 92, 14);
		frmAddNewStudent.getContentPane().add(lblTitle);
		
		final JComboBox comboBox = new JComboBox();
		comboBox.setBounds(122, 39, 86, 20);
		frmAddNewStudent.getContentPane().add(comboBox);
		fillTitleCombo(comboBox);
		
		JLabel lblLeaveBlankTo = new JLabel("Leave blank to autoset.");
		lblLeaveBlankTo.setBounds(216, 14, 131, 14);
		frmAddNewStudent.getContentPane().add(lblLeaveBlankTo);
		
		JLabel lblForname = new JLabel("Forname");
		lblForname.setBounds(20, 73, 92, 14);
		frmAddNewStudent.getContentPane().add(lblForname);
		
		textField_1 = new JTextField();
		textField_1.setBounds(122, 70, 86, 20);
		frmAddNewStudent.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblFamilyName = new JLabel("Family Name");
		lblFamilyName.setBounds(20, 104, 92, 14);
		frmAddNewStudent.getContentPane().add(lblFamilyName);
		
		textField_2 = new JTextField();
		textField_2.setBounds(122, 101, 86, 20);
		frmAddNewStudent.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblDateOfBirth = new JLabel("Date of Birth");
		lblDateOfBirth.setBounds(20, 135, 92, 14);
		frmAddNewStudent.getContentPane().add(lblDateOfBirth);
		
		textField_3 = new JTextField();
		textField_3.setBounds(122, 132, 86, 20);
		frmAddNewStudent.getContentPane().add(textField_3);
		textField_3.setColumns(10);
		
		JLabel lblDdmmyyy = new JLabel("dd-mm-yyy");
		lblDdmmyyy.setBounds(220, 135, 86, 14);
		frmAddNewStudent.getContentPane().add(lblDdmmyyy);
		
		JLabel lblSex = new JLabel("Sex");
		lblSex.setBounds(20, 160, 92, 14);
		frmAddNewStudent.getContentPane().add(lblSex);
		
		final JComboBox comboBox_1 = new JComboBox();
		comboBox_1.setModel(new DefaultComboBoxModel(new String[] {"M", "F"}));
		comboBox_1.setMaximumRowCount(2);
		comboBox_1.setBounds(122, 160, 86, 20);
		frmAddNewStudent.getContentPane().add(comboBox_1);
		
		JButton btnSave = new JButton("Save");
		btnSave.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				//save btn clicked
				addNewStudent(textField.getText(),comboBox.getItemAt(comboBox.getSelectedIndex()).toString(),textField_1.getText(),textField_2.getText(),textField_3.getText(),comboBox_1.getItemAt(comboBox_1.getSelectedIndex()).toString());
			}
		});
		btnSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnSave.setBounds(252, 197, 89, 23);
		frmAddNewStudent.getContentPane().add(btnSave);
	}
	
	/**
	 * Add a new student to the database
	 * @param studentID	String		- the student id (unique)
	 * @param title	String 			- the title of the student	
	 * @param forname String		- the first name of the student
	 * @param familyName String		- the last name of the student
	 * @param dateOfBBirth	String 	- the dob of the student
	 * @param sex String			- the sex of te student (m/f)
	 */
	private void addNewStudent(String studentID, String title,String forname, String familyName, String dateOfBBirth, String sex){
		ArrayList<Object> values = new ArrayList<Object>();
		String sql = "INSERT INTO Student (titleID, forname, familyName, dateOfBirth, sex";
				if(studentID.length() > 0){
					sql = sql + ", studentID";
				}
				sql += ")" +
						"VALUES ((SELECT titleID::integer FROM Titles WHERE titleString=?),?,?,?::date,?";
				if(studentID.length() > 0){
					sql = sql + ",?";
				}
				sql += ");";
		values.add(title);
		values.add(forname);
		values.add(familyName);
		values.add(dateOfBBirth);
		values.add(sex);
		if(studentID.length() > 0){
			values.add(Integer.parseInt(studentID));
		}
		db.insertDatabaseStart(sql, values);
		
	}
}
