package exercise2;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JSeparator;
import javax.swing.JTable;
import javax.swing.JComboBox;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;

public class UserInterface_StudentTranscript {

	private JFrame frame;
	private String studentID;
	private Database db;
	private JTextField txtStudentID;
	private JTextField txtForname;
	private JTextField txtFamilyName;
	private JTextField txtSex;
	private JTextField txtDateOfBirth;
	private JTextField txtEmail;
	private JTextField txtPostal;
	private JTable table;
	private JTextField txtTitle;
	@SuppressWarnings("rawtypes")
	private JComboBox combYear;
	@SuppressWarnings("rawtypes")
	private JComboBox combCourse;
	private Boolean finishedInitialization = false;
	
	
	/**
	 * Create the application.
	 */
	public UserInterface_StudentTranscript(Database db, String studentID) {
		this.db = db;
		this.studentID = studentID;
		initialize();
		initializeData();
		frame.setVisible(true);
		finishedInitialization=true;
	}

	/**
	 * Initialise the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes" })
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 681, 449);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JLabel lblStudentId = new JLabel("Student ID:");
		lblStudentId.setBounds(10, 11, 90, 14);
		frame.getContentPane().add(lblStudentId);
		
		JLabel lblForname = new JLabel("Forname:");
		lblForname.setBounds(10, 61, 90, 14);
		frame.getContentPane().add(lblForname);
		
		JLabel lblFamilyName = new JLabel("Family Name:");
		lblFamilyName.setBounds(10, 86, 90, 14);
		frame.getContentPane().add(lblFamilyName);
		
		JLabel lblSex = new JLabel("Sex:");
		lblSex.setBounds(336, 14, 90, 14);
		frame.getContentPane().add(lblSex);
		
		JLabel lblDateOfBirth = new JLabel("Date of Birth:");
		lblDateOfBirth.setBounds(336, 39, 90, 14);
		frame.getContentPane().add(lblDateOfBirth);
		
		JLabel lblEmailAddress = new JLabel("Email Address:");
		lblEmailAddress.setBounds(336, 64, 90, 14);
		frame.getContentPane().add(lblEmailAddress);
		
		JLabel lblPostalAddress = new JLabel("Postal Address:");
		lblPostalAddress.setBounds(336, 89, 90, 14);
		frame.getContentPane().add(lblPostalAddress);
		
		txtStudentID = new JTextField();
		txtStudentID.setEditable(false);
		txtStudentID.setBounds(110, 8, 200, 20);
		frame.getContentPane().add(txtStudentID);
		txtStudentID.setColumns(10);
		
		txtForname = new JTextField();
		txtForname.setEditable(false);
		txtForname.setColumns(10);
		txtForname.setBounds(110, 58, 200, 20);
		frame.getContentPane().add(txtForname);
		
		txtFamilyName = new JTextField();
		txtFamilyName.setEditable(false);
		txtFamilyName.setColumns(10);
		txtFamilyName.setBounds(110, 83, 200, 20);
		frame.getContentPane().add(txtFamilyName);
		
		txtSex = new JTextField();
		txtSex.setEditable(false);
		txtSex.setColumns(10);
		txtSex.setBounds(436, 11, 200, 20);
		frame.getContentPane().add(txtSex);
		
		txtDateOfBirth = new JTextField();
		txtDateOfBirth.setEditable(false);
		txtDateOfBirth.setColumns(10);
		txtDateOfBirth.setBounds(436, 36, 200, 20);
		frame.getContentPane().add(txtDateOfBirth);
		
		txtEmail = new JTextField();
		txtEmail.setEditable(false);
		txtEmail.setColumns(10);
		txtEmail.setBounds(436, 61, 200, 20);
		frame.getContentPane().add(txtEmail);
		
		txtPostal = new JTextField();
		txtPostal.setEditable(false);
		txtPostal.setColumns(10);
		txtPostal.setBounds(436, 86, 200, 20);
		frame.getContentPane().add(txtPostal);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(24, 117, 612, 14);
		frame.getContentPane().add(separator);
		
		JLabel lblYear = new JLabel("Year:");
		lblYear.setBounds(34, 136, 46, 14);
		frame.getContentPane().add(lblYear);
		
		combCourse = new JComboBox();
		combCourse.setBounds(232, 133, 104, 20);
		frame.getContentPane().add(combCourse);
		
		JButton btnShow = new JButton("Show");
		btnShow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				showMarks();
			}
		});
		combYear = new JComboBox();
		combYear.addActionListener(new ActionListener() {
			    public void actionPerformed(ActionEvent e) {
			    	combCourse.removeAllItems();
			        initializeCourses();
			    }
			});
		combYear.setBounds(65, 133, 104, 20);
		frame.getContentPane().add(combYear);
		
		JLabel lblCourse = new JLabel("Course:");
		lblCourse.setBounds(190, 136, 46, 14);
		frame.getContentPane().add(lblCourse);
		
		btnShow.setBounds(350, 132, 76, 23);
		frame.getContentPane().add(btnShow);
		
		JLabel lblTitle = new JLabel("Title:");
		lblTitle.setBounds(10, 36, 90, 14);
		frame.getContentPane().add(lblTitle);
		
		txtTitle = new JTextField();
		txtTitle.setEditable(false);
		txtTitle.setColumns(10);
		txtTitle.setBounds(110, 33, 200, 20);
		frame.getContentPane().add(txtTitle);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 161, 655, 249);
		frame.getContentPane().add(scrollPane);
		
		table = new JTable();
		scrollPane.setViewportView(table);
	}


	/**
	 * Searches the information of the user from the UserID
	 * and populates the textbox associated with that data.
	 */
	@SuppressWarnings("unchecked")
	private void initializeData(){
		try {
			System.out.print("Searching for infomation on id:" + studentID);
			
			String sql = "SELECT " +
						 "	Student.titleID," +
						 "	Student.forname," +
						 " 	Student.familyName," +
						 "	Student.dateOfBirth," +
						 "	Student.sex," +
						 " 	StudentContact.emailAddress," +
						 " 	StudentContact.postalAddress " +
						 "FROM Student, StudentContact " +
						 "Where Student.studentID='" + studentID + "' AND StudentContact.studentID = '" + studentID + "';" ;
			PreparedStatement stmt = db.queryDatabaseStart(sql);
			ResultSet r = stmt.executeQuery();

			//SELECT ((SELECT titleString FROM Titles WHERE titleID=Student.titleID),Student.forname,Student.familyName,Student.dateOfBirth,Student.sex,StudentContact.emailAddress,StudentContact.postalAddress) FROM Student,StudentContact Where Student.studentID='2' AND StudentContact.studentID='2';
		
			r.next();
			txtStudentID.setText(studentID);
			txtTitle.setText(r.getString(1));
			txtForname.setText(r.getString(2));
			txtFamilyName.setText(r.getString(3));
			txtDateOfBirth.setText(r.getString(4));
			txtSex.setText(r.getString(5));
			txtEmail.setText(r.getString(6));
			txtPostal.setText(r.getString(7));
			
			sql = "SELECT DISTINCT year " +
					 "FROM marks " +
					 "WHERE studentID=" + studentID + 
					 "ORDER BY year ASC " +";";
			
			stmt = db.queryDatabaseStart(sql);
			r = stmt.executeQuery();
			
			while(r.next()){
				String str = r.getString(1);
				combYear.addItem(str);
			}
			combYear.setSelectedIndex(-1);
			r.close();
			stmt.close();
			
		} catch (SQLException e) {
			System.out.println("Error searching infomation on ID:" + studentID);
			System.out.println(e.getMessage());
		}finally{
			System.out.println("Search StudentID complete.");
			System.out.println();
		}
	}

	/**
	 * Adds the list of courses that the student studies
	 * to the course selection combobox.
	 */
	@SuppressWarnings("unchecked")
	private void initializeCourses(){
		if(finishedInitialization){
			try {
				System.out.println("Searching for infomation on courses:");
	
				String year = combYear.getSelectedItem().toString();
				String sql = "SELECT courseName " +
							 "FROM Course " +
							 "WHERE courseID=(" +
							 "	SELECT DISTINCT courseID" +
							 "	FROM marks" +
							 "	WHERE studentID='" + this.studentID + "' AND year='" + year + "');" ;
				
				PreparedStatement stmt = db.queryDatabaseStart(sql);
				ResultSet r = stmt.executeQuery();
				
				while(r.next()){
					String str = r.getString(1);
					combCourse.addItem(str);
				}
				combCourse.setSelectedIndex(-1);
				r.close();
				stmt.close();
				
			} catch (SQLException e) {
				System.out.println("Error searching infomation on Courses");
				System.out.println(e.getMessage());
			}finally{
				System.out.println("Search StudentID complete.");
				System.out.println();
			}
		}
	}

	/**
	 * Populates the table with the relevant data from the database.
	 * This is based off of the selected course and year.
	 */
	private void showMarks(){
		int year = Integer.parseInt(combYear.getSelectedItem().toString());
		String course = combCourse.getSelectedItem().toString();
		try {
			System.out.println("Showing marks data.");

			String sql =	"SELECT" +
							 "	courseID," +
							 "	(SELECT courseName FROM Course WHERE courseID=marks.courseID) AS courseName," +
							 "	mark," +
							 "	(SELECT sessionString FROM Session WHERE sessionID=marks.sessionID) AS session," +
							 "	(SELECT typeString FROM Type WHERE typeID=marks.typeID) AS type " +
							 "FROM Marks " +
							 "WHERE " +
							 "	CourseID=(SELECT courseID FROM Course WHERE courseName='" + course + "') AND " +
							 "	year='" + year + "';" ;
			
			PreparedStatement stmt = db.queryDatabaseStart(sql);
			ResultSet r = stmt.executeQuery();
			
			
			table.setModel(db.buildTableModel(r));
			r.close();
			stmt.close();
			
		} catch (SQLException e) {
			System.out.println("Error showing marks data.");
			System.out.println(e.getMessage());
		}finally{
			System.out.println("Finished showing marks data.");
			System.out.println();
		}
	}
	
	
}
