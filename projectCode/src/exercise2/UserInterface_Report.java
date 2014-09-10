package exercise2;

import javax.swing.JFrame;
import javax.swing.JTable;
import java.awt.BorderLayout;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JScrollPane;

public class UserInterface_Report {

	private JFrame frame;
	private Database db;
	private JLabel lblYear;
	@SuppressWarnings("rawtypes")
	private JComboBox combYear;
	private JSeparator separator;
	private JLabel lblNewLabel;
	@SuppressWarnings("rawtypes")
	private JComboBox combSession;
	private JButton btnShow;
	private JScrollPane scrollPane;
	private JTable table;
	
	/**
	 * Create the application.
	 * @param db 
	 */
	public UserInterface_Report(Database db) {
		this.db = db;
		initialize();
		initializeYearSession();
		initializeData();
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
		
		lblYear = new JLabel("Year:");
		panel.add(lblYear);
		
		combYear = new JComboBox();
		combYear.setModel(new DefaultComboBoxModel(new String[] {"1", "2", "3", "4", "5"}));
		panel.add(combYear);
		
		lblNewLabel = new JLabel("Session:");
		panel.add(lblNewLabel);
		
		combSession = new JComboBox();
		panel.add(combSession);
		
		btnShow = new JButton("Show");
		btnShow.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				initializeData();
			}
		});
		panel.add(btnShow);
		
		separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		panel.add(separator);
		
		scrollPane = new JScrollPane();
		frame.getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		table = new JTable();
		scrollPane.setViewportView(table);
	}

	/**
	 * Fills the year and session comboboxes with data from the
	 * database ready for the user to select from.
	 */
	@SuppressWarnings("unchecked")
	private void initializeYearSession(){
		try {
			System.out.println("Initializing the comboboxes.");

			String sql =	"SELECT sessionString FROM Session ORDER BY sessionString ASC;";
			
			PreparedStatement stmt = db.queryDatabaseStart(sql);
			ResultSet r = stmt.executeQuery();
			while(r.next()){
				combSession.addItem(r.getString(1));
			}
			r.close();
			stmt.close();
			
		} catch (SQLException e) {
			System.out.println("Error Initializing comboboxes.");
			System.out.println(e.getMessage());
		}finally{
			System.out.println("Finished Initializing comboboxes.");
			System.out.println();
		}
	}
	
	/**
	 * Loads the table with the relevant data
	 * based on which year & session you have selected.
	 */
	private void initializeData(){
		try {
			int year = Integer.parseInt(combYear.getSelectedItem().toString());
			String session = combSession.getSelectedItem().toString();
			
			
			System.out.println("Showing marks data.");

			//counts the number of students who have marks for a course and gets avg mark
//			SELECT count(studentID), avg(mark), courseID
//			FROM Marks
//			WHERE courseID='2'
//			GROUP BY courseID
			
			
			//gets the courses on the year/session
//			SELECT courseID AS CourseID 
//			FROM Marks
//			WHERE year='2' AND 
//				sessionID=(SELECT sessionID
//						FROM Session
//						WHERE sessionString='Jan/Feb')

			//Gets the coureid's along with the coursenames for courses in the marks
//			SELECT Course.courseID,Course.courseName
//			FROM Course,(SELECT courseID  
//					FROM Marks
//					WHERE year='2' AND 
//						sessionID=(SELECT sessionID
//							FROM Session
//							WHERE sessionString='Jan/Feb')) AS CourseIDs
//			WHERE Course.courseID=CourseIDs.courseID
			
			
			
			
			
//			SELECT courseID, AVG(mark), count(studentID)
//			FROM Marks
////			GROUP BY courseID
//			
			//String sql ="SELECT temp.courseID, Course.courseName, temp.AverageMark, temp.NumStudents FROM Course, (SELECT courseID, AVG(mark) AS AverageMark, count(studentID) AS NumStudents FROM Marks WHERE Marks.year='" + year + "' AND Marks.sessionID=(SELECT Session.sessionID FROM Session WHERE Session.sessionString='" + session + "') GROUP BY courseID) AS temp WHERE Course.courseID=temp.courseID;" ;
			String sql ="SELECT temp.courseID, Course.courseName, temp.AverageMark, temp.NumStudents " + 
						"FROM Course, (" + 
							"SELECT courseID, AVG(mark)::integer AS AverageMark, count(studentID) AS NumStudents " + 
							"FROM Marks " + 
							"WHERE Marks.year='" + year + "' AND " + 
								  "Marks.sessionID=(" + 
								  					"SELECT Session.sessionID " + 
								  					"FROM Session " +
								  					"WHERE Session.sessionString='" + session + "') " +
								  					"GROUP BY courseID) AS temp " + 
						"WHERE Course.courseID=temp.courseID;" ;

			
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
