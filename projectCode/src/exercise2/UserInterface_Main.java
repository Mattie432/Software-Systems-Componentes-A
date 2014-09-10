package exercise2;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import javax.swing.JLabel;

public class UserInterface_Main {

	private JFrame frame;
	private Database db;
	private JTextField txtStudentID;


	/**
	 * Create the application.
	 */
	public UserInterface_Main(Database dataBase) {
		db = dataBase;
		initialize();
		this.frame.setVisible(true);
	}

	/**
	 * Initialise the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 589, 213);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnAddNewStudent = new JButton("Add New Student");
		btnAddNewStudent.setBounds(10, 11, 133, 21);
		frame.getContentPane().add(btnAddNewStudent);
		
		JButton btnAddStudentMarks = new JButton("Add Student Marks");
		btnAddStudentMarks.setBounds(10, 43, 143, 21);
		frame.getContentPane().add(btnAddStudentMarks);
		
		JButton btnClearDatabase = new JButton("Clear Database");
		btnClearDatabase.setBounds(449, 11, 114, 21);
		frame.getContentPane().add(btnClearDatabase);
		btnClearDatabase.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				db.removeTables();
			}
		});
		btnClearDatabase.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JButton btnAddTestData = new JButton("Add test data");
		btnAddTestData.setBounds(449, 75, 114, 21);
		frame.getContentPane().add(btnAddTestData);
		
		JButton btnAddTables = new JButton("Add tables");
		btnAddTables.setBounds(449, 43, 117, 21);
		frame.getContentPane().add(btnAddTables);
		
		txtStudentID = new JTextField();
		txtStudentID.setBounds(83, 126, 86, 20);
		frame.getContentPane().add(txtStudentID);
		txtStudentID.setColumns(10);
		
		JLabel lblStudentId = new JLabel("Student ID:");
		lblStudentId.setBounds(10, 129, 86, 14);
		frame.getContentPane().add(lblStudentId);
		
		JButton btnCreateTranscript = new JButton("Create transcript");
		btnCreateTranscript.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("unused")
			@Override
			public void mouseClicked(MouseEvent arg0) {
				UserInterface_StudentTranscript ui = new UserInterface_StudentTranscript(db, txtStudentID.getText());
			}
		});
		btnCreateTranscript.setBounds(179, 125, 143, 23);
		frame.getContentPane().add(btnCreateTranscript);
		
		JButton btnProduceReport = new JButton("Produce Report");
		btnProduceReport.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("unused")
			@Override
			public void mouseClicked(MouseEvent arg0) {
				UserInterface_Report ui = new UserInterface_Report(db);
			}
		});
		btnProduceReport.setBounds(10, 75, 133, 23);
		frame.getContentPane().add(btnProduceReport);
		btnAddTables.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				db.createTables();
			}
		});
		btnAddTestData.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				db.addTestData();
			}
		});
		btnAddStudentMarks.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("unused")
			@Override
			public void mouseClicked(MouseEvent arg0) {
				UserInterface_AddMarks ui_addMarks = new UserInterface_AddMarks(db);
			}
		});
		btnAddNewStudent.addMouseListener(new MouseAdapter() {
			@SuppressWarnings("unused")
			@Override
			public void mouseClicked(MouseEvent arg0) {
				UserInterface_AddStudent ui_addStudent = new UserInterface_AddStudent(db);
			}
		});
		btnAddNewStudent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
	}
}
