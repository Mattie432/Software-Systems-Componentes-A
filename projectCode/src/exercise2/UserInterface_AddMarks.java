package exercise2;

import javax.swing.JFrame;
//import java.awt.Window.Type;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Font;
import javax.swing.JComboBox;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;

public class UserInterface_AddMarks {

	private JFrame frmAddStudentMarks;
	private Database db;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;


	/**
	 * Create the application.
	 * @param db Database - the database in use 
	 */
	public UserInterface_AddMarks(Database database) {
		db = database;
		initialize();
		frmAddStudentMarks.setVisible(true);
	}

	/**
	 * Initialise the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		frmAddStudentMarks = new JFrame();
		frmAddStudentMarks.setTitle("Add student marks");
		//frmAddStudentMarks.setType(Type.UTILITY);
		frmAddStudentMarks.setBounds(100, 100, 543, 386);
		frmAddStudentMarks.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frmAddStudentMarks.getContentPane().setLayout(null);
		
		JLabel lblStudentId = new JLabel("Student ID:");
		lblStudentId.setBounds(10, 11, 76, 14);
		frmAddStudentMarks.getContentPane().add(lblStudentId);
		
		final JComboBox comboBox_1 = new JComboBox();
		comboBox_1.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
			}
		});
		textField = new JTextField();
		textField.getDocument().addDocumentListener(new DocumentListener() {

	        @Override
	        public void removeUpdate(DocumentEvent e) {
	        	comboBox_1.removeAllItems();
	        	
	        }

	        @Override
	        public void insertUpdate(DocumentEvent e) {
	        }

			@Override
			public void changedUpdate(DocumentEvent arg0) {
				
			}
	    });
		textField.setBounds(98, 8, 86, 20);
		frmAddStudentMarks.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblOr = new JLabel("OR");
		lblOr.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblOr.setBounds(194, 11, 46, 14);
		frmAddStudentMarks.getContentPane().add(lblOr);
		
		JLabel lblFamilyName = new JLabel("Family name");
		lblFamilyName.setBounds(233, 11, 86, 14);
		frmAddStudentMarks.getContentPane().add(lblFamilyName);
		
		textField_1 = new JTextField();
		textField_1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
			}
		});
		textField_1.setBounds(312, 11, 112, 20);
		frmAddStudentMarks.getContentPane().add(textField_1);
		textField_1.setColumns(10);
		
		
		JLabel lblForname = new JLabel("Forname");
		lblForname.setBounds(233, 39, 86, 14);
		frmAddStudentMarks.getContentPane().add(lblForname);
		
		JButton btnSearch = new JButton("Search");
		btnSearch.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				getStudentID(textField_1.getText(), textField_2.getText(), textField);
			}
		});
		btnSearch.setBounds(434, 35, 86, 23);
		frmAddStudentMarks.getContentPane().add(btnSearch);
		
		JLabel lblCourse = new JLabel("Course:");
		lblCourse.setBounds(10, 86, 46, 14);
		frmAddStudentMarks.getContentPane().add(lblCourse);
		
		comboBox_1.setBounds(98, 83, 185, 20);
		frmAddStudentMarks.getContentPane().add(comboBox_1);
		
		textField_2 = new JTextField();
		textField_2.setBounds(312, 36, 112, 20);
		frmAddStudentMarks.getContentPane().add(textField_2);
		textField_2.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("Year:");
		lblNewLabel.setBounds(10, 111, 46, 14);
		frmAddStudentMarks.getContentPane().add(lblNewLabel);
		
		final JComboBox comboBox = new JComboBox();
		comboBox.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5"}));
		comboBox.setBounds(98, 108, 46, 20);
		frmAddStudentMarks.getContentPane().add(comboBox);
		
		JLabel lblSession = new JLabel("Session:");
		lblSession.setBounds(10, 144, 76, 14);
		frmAddStudentMarks.getContentPane().add(lblSession);
		
		final JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setBounds(98, 141, 185, 20);
		frmAddStudentMarks.getContentPane().add(comboBox_2);
		JLabel lblType = new JLabel("Type:");
		lblType.setBounds(10, 175, 46, 14);
		frmAddStudentMarks.getContentPane().add(lblType);
		
		final JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setBounds(98, 172, 185, 20);
		frmAddStudentMarks.getContentPane().add(comboBox_3);
		
			JLabel lblNewLabel_1 = new JLabel("Mark:");
			lblNewLabel_1.setBounds(10, 203, 76, 14);
			frmAddStudentMarks.getContentPane().add(lblNewLabel_1);
			
			textField_3 = new JTextField();
			textField_3.setBounds(98, 200, 86, 20);
			frmAddStudentMarks.getContentPane().add(textField_3);
			textField_3.setColumns(10);
			
			JLabel lblNotes = new JLabel("Notes:");
			lblNotes.setBounds(10, 231, 46, 14);
			frmAddStudentMarks.getContentPane().add(lblNotes);
			
			final JTextArea textArea = new JTextArea();
			textArea.setLineWrap(true);
			textArea.setBounds(98, 226, 185, 111);
			frmAddStudentMarks.getContentPane().add(textArea);
			
			JButton btnSaveMarks = new JButton("Save Marks");
			btnSaveMarks.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
				
					try {
						int studentID = Integer.parseInt(textField.getText());
						String courseString = comboBox_1.getItemAt(comboBox_1.getSelectedIndex()).toString();
						int year = Integer.parseInt(comboBox.getItemAt(comboBox.getSelectedIndex()).toString());
						String sessionString = comboBox_2.getItemAt(comboBox_2.getSelectedIndex()).toString();
						String typeString = comboBox_3.getItemAt(comboBox_3.getSelectedIndex()).toString();
						int mark = Integer.parseInt(textField_3.getText());
						String notes = textArea.getText();
						
						saveMark(studentID, courseString, year , sessionString , typeString , mark , notes );
					} catch (NumberFormatException e) {
						System.out.println("Error adding mark!");
						System.out.println(e.getMessage());
					}
				}
			});
			btnSaveMarks.setBounds(431, 318, 89, 23);
			frmAddStudentMarks.getContentPane().add(btnSaveMarks);
		
			
		try {
			getSessions(comboBox_2);
			getTypes(comboBox_3);
			getClasses(comboBox_1);
			
		} catch (Exception e1) {
			System.out.println("Error setting up the marks window");
			System.out.println(e1.getMessage());
		}finally{
			
		}
	}

	/**
	 * Sets the type combobox with data from the database.
	 * @param comb	JComboBox - the combobox to fill
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void getTypes(JComboBox comb) throws SQLException {
			//System.out.print("Searching for types.");
			String sql = "SELECT typeString FROM Type;";
			PreparedStatement stmt = db.queryDatabaseStart(sql);
			ResultSet r = stmt.executeQuery();
			
			while(r.next()){
				comb.addItem(r.getString(1));
			}
	}

	/**
	 * Gets the students id from the first and last name
	 * and enters it into the relevent textbox
	 * @param lastName	String	- Students last name
	 * @param firstName	String 	- Students first name
	 * @param idTextBox	JTextBox	- the textbox to enter it into
	 */
	public void getStudentID(String lastName, String firstName, JTextField idTextBox){

			try {
				System.out.print("Searching for " + firstName + " " + lastName +"'s Student ID");
				String sql = "SELECT studentID FROM Student WHERE familyName='" + lastName + "' AND forname = '" + firstName + "';";
				PreparedStatement stmt = db.queryDatabaseStart(sql);
				ResultSet r = stmt.executeQuery();
				
				while(r.next()){
					idTextBox.setText(r.getString(1));
				}
				r.close();
				stmt.close();
			} catch (SQLException e) {
				System.out.println("Error getting student ID:");
				System.out.println(e.getMessage());
			}finally{
				System.out.println("Search StudentID complete.");
				System.out.println();
			}
	}
	
	/**
	 * Fills the sessions combobox with data from the databse
	 * @param comb	JComboBox	- Combobox to fill
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getSessions(JComboBox comb) throws SQLException{
			//System.out.print("Searching for Sessions.");
			String sql = "SELECT sessionString FROM Session;";
			PreparedStatement stmt = db.queryDatabaseStart(sql);
			ResultSet r = stmt.executeQuery();
			
			while(r.next()){
				comb.addItem(r.getString(1));
			}
	}
	
	/**
	 * Fills the classes combobox with the data from the database
	 * @param comb	JComboBox	- The combobox to fill
	 * @throws SQLException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void getClasses(JComboBox comb) throws SQLException{
			//System.out.print("Searching for classes.");
			String sql = "SELECT courseName FROM Course;";
			PreparedStatement stmt = db.queryDatabaseStart(sql);
			ResultSet r = stmt.executeQuery();
			
			while(r.next()){
				comb.addItem(r.getString(1));
			}
	}

	/**
	 * Saves the mark to the database.
	 * @param studentID int		- The studentID
	 * @param courseString String	- the Coursestring
	 * @param year int				- the Year to add the mark for
	 * @param sessionString	String	- the Session to add the mark for
	 * @param typeString String		- the Type to add the mark for
	 * @param mark	int				- The mark to add
	 * @param notes	String			- notes to add
	 */
	public void saveMark(int studentID, String courseString, int year, String sessionString, String typeString, int mark, String notes){

		ArrayList<Object> values = new ArrayList<Object>();
		String sql = "INSERT INTO Marks" +
					 "	(studentID, CourseID, year, sessionID, typeID, mark, notes)" +
					 "VALUES" +
					 "	(?,(Select courseID::integer FROM Course WHERE courseName=?),?," +
					 "(Select sessionID::integer FROM Session WHERE sessionString=?)," + 
					 "(Select typeID::integer FROM Type WHERE typeString=?),?,?);" ;


		values.add(studentID);
		values.add(courseString);
		values.add(year);
		values.add(sessionString);
		values.add(typeString);
		values.add(mark);
		values.add(notes);
		
		db.insertDatabaseStart(sql, values);
		
		
	}
}
