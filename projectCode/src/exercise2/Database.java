package exercise2;

import java.sql.*;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class Database {

	private Connection dbConn = null;

	/**
	 * Constructor for the database, this 
	 * initialises the connection to the server.
	 */
	public Database() {
		connectToDatabase();
	}

	/**
	 * Establish a connection to the database.
	 */
	public void connectToDatabase() {

		System.out.println();
		String dbName = "jdbc:postgresql://dbteach2.cs.bham.ac.uk/mattie432";

		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException ex) {
			System.out.println("Driver not found");
		}

		System.out.println("PostgreSQL driver registered.");
		try {
			dbConn = DriverManager.getConnection(dbName, "mxf203", "password");
			System.out.println("Database accessed!");
			dbConn.setAutoCommit(false);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		if (dbConn != null) {
		} else {
			System.out.println("Failed to make connection");
		}

	}

	/**
	 * Close the connection to the database.
	 */
	public void disconnectFromDatabase() {
		System.out.println();
		try {
			dbConn.close();
			System.out.println("Database connection closed");
		} catch (SQLException e) {
			System.out.println("Error closing database \n");
			e.printStackTrace();
		}

	}

	/**
	 * Creates all of the tables in the database for testing purposes.
	 */
	public void createTables() {
		System.out.println();

		System.out.println("Starting table creation.");
		int numSqlQueries = 9;
		String[] sql = new String[numSqlQueries];
		PreparedStatement stmt = null;
		String[] tableNames = new String[9];
		tableNames[0] = "Title";
		tableNames[1] = "Student";
		tableNames[2] = "Session";
		tableNames[3] = "Type";
		tableNames[4] = "NextOfKin";
		tableNames[5] = "Lecturer";
		tableNames[6] = "Course";
		tableNames[7] = "Marks";
		tableNames[8] = "StudentContact";

		// constraints, length checks on strings
		sql[0] = "CREATE TABLE Titles (" + 
					"titleID SERIAL, "	+ 
					"titleString varchar(500) UNIQUE NOT NULL," + 
					"PRIMARY KEY (titleID)" + 
				");";

		sql[1] = "CREATE TABLE Student (" + 
					"studentID SERIAL, " + 
					"titleID int NOT NULL," + 
					"forname varchar(100)," + 
					"familyName varchar(100), " + 
					"dateOfBirth DATE NOT NULL," + 
					"sex varchar(1) NOT NULL," + 
					"PRIMARY KEY (studentID)," + 
					"FOREIGN KEY (titleID) REFERENCES Titles(titleID)" + 
				");";

		sql[2] = "CREATE TABLE Session (" + 
					"sessionID SERIAL, " + 
					"sessionString varchar(500) NOT NULL," + 
					"PRIMARY KEY (sessionID)" + 
				");";

		sql[3] = "CREATE TABLE Type (" + 
					"typeID SERIAL, " + 
					"typeString varchar(500) NOT NULL," + 
					"PRIMARY KEY (typeID)" + 
				");";

		sql[4] = "CREATE TABLE NextOfKin (" + 
					"studentID int NOT NULL, " + 
					"eMailAddress varchar(500)," + 
					"postalAddress varchar(500), " + 
					"FOREIGN KEY (studentID) REFERENCES Student(studentID)"
				+ ");";

		sql[5] = "CREATE TABLE Lecturer (" + 
					"lecturerID SERIAL, " + 
					"titleID int," + 
					"forname varchar(100)," + 
					"familyName varchar(100), " + 
					"PRIMARY KEY (LecturerID)," + 
					"FOREIGN KEY (titleID) REFERENCES Titles(titleID)" + 
				");";

		sql[6] = "CREATE TABLE Course (" + 
					"courseID SERIAL, " + 
					"courseName varchar(500) UNIQUE NOT NULL," + 
					"courseDescription varchar(500), " + 
					"lecturerID int NOT NULL," + 
					"PRIMARY KEY (courseID)," + 
					"FOREIGN KEY (lecturerID) REFERENCES Lecturer(lecturerID)" + 
				");";

		sql[7] = "CREATE TABLE Marks (" + 
					"studentID int NOT NULL, " + 
					"courseID int NOT NULL," + 
					"year int NOT NULL CHECK(year>=1 AND year<=5)," + 
					"sessionID int NOT NULL, " + 
					"typeID int NOT NULL," + 
					"mark int NOT NULL," + 
					"notes varchar(500)," + 
					"FOREIGN KEY (studentID) REFERENCES Student(studentID)," + 
					"FOREIGN KEY (courseID) REFERENCES Course(courseID)," + 
					"FOREIGN KEY (sessionID) REFERENCES Session(sessionID)," + 
					"FOREIGN KEY (typeID) REFERENCES Type(typeID)" + 
				");";

		sql[8] = "CREATE TABLE StudentContact (" + 
					"studentID int NOT NULL, " + 
					"eMailAddress varchar(100)," + 
					"postalAddress varchar(100), " + 
					"FOREIGN KEY (studentID) REFERENCES Student(studentID)" + 
				");";

		for (int i = 0; i < numSqlQueries; i++) {
			try {

				stmt = dbConn.prepareStatement(sql[i]);
				stmt.executeUpdate();
				dbConn.commit();
				System.out.println("Table created: " + tableNames[i]);

			} catch (SQLException e) {
				System.out.println("Error creating a new table");
				e.printStackTrace();

				// try rollback
				if (dbConn != null) {
					try {
						dbConn.rollback();
						System.out.println("Rollback sucsessfull");
					} catch (Exception ex) {
						System.out.println("Could not rollback changes");
					}
				}

			} finally {

				// close connection
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out
							.println("Error closing the statement connection");
					e.printStackTrace();
				}
			}
		}
		System.out.println("Finished table creation.");

	}

	/**
	 * Removes all of the tables from the database.
	 */
	public void removeTables() {
		System.out.println();
		System.out.println("Starting table removal.");
		int numTableNames = 9;
		String[] tableNames = new String[numTableNames];
		PreparedStatement stmt = null;

		tableNames[0] = "Titles";
		tableNames[1] = "Student";
		tableNames[2] = "Session";
		tableNames[3] = "Type";
		tableNames[4] = "NextOfKin";
		tableNames[5] = "Lecturer";
		tableNames[6] = "Course";
		tableNames[7] = "Marks";
		tableNames[8] = "StudentContact";

		for (int i = numTableNames - 1; i >= 0; i--) {
			try {

				String sql = "DROP TABLE " + tableNames[i] + ";";
				stmt = dbConn.prepareStatement(sql);
				stmt.executeUpdate();
				dbConn.commit();
				System.out.println("Table dropped: " + tableNames[i]);

			} catch (SQLException e) {
				System.out.println("Error dropping a table");
				e.printStackTrace();

				// try rollback
				if (dbConn != null) {
					try {
						dbConn.rollback();
						System.out.println("Rollback sucsessfull");
					} catch (Exception ex) {
						System.out.println("Could not rollback changes");
					}
				}

			} finally {

				// close connection
				try {
					stmt.close();
				} catch (SQLException e) {
					System.out
							.println("Error closing the statement connection");
					e.printStackTrace();
				}
			}
		}
		System.out.println("Table removal finished.");
	}



	/**
	 * Returns a prepared statement object ready to
	 * query the database.
	 * @param sql String - the SQL code to be rub
	 * @return	PreparedStatement - the prepared statement to execute
	 */
	public PreparedStatement queryDatabaseStart(String sql){
				PreparedStatement stmt = null;
				try {
					stmt = dbConn.prepareStatement(sql);
					return stmt;
				} catch (SQLException e) {
					
					System.out.println("Error in queryDatabaseStart");
					
				}
				return stmt;
	}
	
	/**
	 * Allows insertion of data into the database. The sql string should
	 * have '?' where the variables are to go.
	 * @param sql String	- the SQL update string
	 * @param values ArrayList<Object> - an array of objects corresponding to the variables in the sql string.
	 */
	public void insertDatabaseStart(String sql, ArrayList<Object> values){
		PreparedStatement stmt = null;
		try {
			stmt = dbConn.prepareStatement(sql);
			
			for(int i = 0; i<values.size(); i++){
				if(values.get(i).getClass().equals(Integer.class)){
					//int
					stmt.setInt(i+1,(Integer) values.get(i));
				}else if(values.get(i).getClass().equals(String.class)){
					//string
				stmt.setString(i+1, (String) values.get(i));
				}
			}
			
			stmt.executeUpdate();
			dbConn.commit();
			System.out.println("Update sucsessful!");

		} catch (SQLException e) {
			System.out.println("Error inserting record.");
			System.out.println(e.getMessage());

			// try rollback
			if (dbConn != null) {
				try {
					dbConn.rollback();
					System.out.println("Rollback sucsessfull");
				} catch (Exception ex) {
					System.out.println("Could not rollback changes");
				}
			}

		}finally{
			try {
				stmt.close();
				stmt = null;
			} catch (SQLException e) {
				System.out.println("Could not close update connection.");
				e.printStackTrace();
			}
			System.out.println();
		}
	}
	
	
	
	
	
	/**
	 * This initialises the adding of test data to the database.
	 */
	public void addTestData() {
		int numStudents = 100;
		int numTitles = 5;
		int numType = 3;
		int numNextOfKin = 50;
		int numLecturer = 15;
		int numCourse = 100;
		int numMarks = 100;
		
		System.out.println();
		
		System.out.println("Starting adding test data.");
		addTestData_Title(numTitles);
		addTestData_Student(numStudents, numTitles);
		addTestData_Session();
		addTestData_Type(numType);
		addTestData_NextOfKin(numStudents, numNextOfKin);
		addTestData_Lecturer(numLecturer, numTitles);
		addTestData_Course(numCourse, numLecturer);
		addTestData_Marks(numMarks, numCourse, numType, numStudents);
		addTestData_StudentContact(numStudents);
		System.out.println("Finished adding test data.");
		
		System.out.println();
	}

/**
 * Adds test data to the Titles table.
 * @param numTitles	int - Number of titles to add
 */
	private void addTestData_Title(int numTitles) {

		String[] titles = new String[numTitles];
		titles[0] = "Mr";
		titles[1] = "Mrs";
		titles[2] = "Miss";
		titles[3] = "Dr";
		titles[4] = "Ms";

		PreparedStatement stmt = null;

		try {
			for (int i = 0; i < numTitles; i++) {
				try {
					String sql = "INSERT INTO Titles(titleString) VALUES(" +
							"'" + titles[i] + "');";
					stmt = dbConn.prepareStatement(sql);
					stmt.executeUpdate();

					dbConn.commit();

				} catch (SQLException e) {
					System.out.println("Error inserting :" + titles[i]);
					e.printStackTrace();
					try {
						dbConn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
			
			System.out.println("Test data inserted: Titles");

		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out
						.println("Could not close PreparedStatement connection");
				e.printStackTrace();
			}
		}

	}
	
	/**
	 * Adds test data to the Student table. 
	 * @param numStudents int - number of students to add
	 * @param numTitles	int - number of titles already added
	 */
	private void addTestData_Student(int numStudents, int numTitles) {

		PreparedStatement stmt = null;

		try {
			for (int i = 0; i < numStudents; i++) {
				try {
					String sql = "INSERT INTO " + 
									"Student(titleID, forname, familyName, dateOfBirth, sex) " + 
									"VALUES(" +
										"'" + randomNumberBetween(1, numTitles) + "', " +
										"'" + "forname" + (i+1) + "', " +
										"'" + "familyName" + (i+1) + "', " +
										"'" + randomDateOfBirth() + "', " +
										"'" + randomSex() + "'" +
								");";
					stmt = dbConn.prepareStatement(sql);
					stmt.executeUpdate();

					dbConn.commit();

				} catch (SQLException e) {
					System.out.println("Error inserting : Student " + i);
					e.printStackTrace();
					try {
						dbConn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
			
			System.out.println("Test data inserted: Students");

		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out
						.println("Could not close PreparedStatement connection");
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * Adds test data to the Session table. 
	 */
	@SuppressWarnings("resource")
	private void addTestData_Session() {

		PreparedStatement stmt = null;

		try {
				try {
					String sql = "INSERT INTO " + 
									"Session(sessionString) " + 
									"VALUES(" + 
										"'May/Aug'" +
								");";
					stmt = dbConn.prepareStatement(sql);
					stmt.executeUpdate();
					dbConn.commit();

					sql = "INSERT INTO " + 
									"Session(sessionString) " + 
									"VALUES(" +
										"'Jan/Feb'" +
								");";
					stmt = dbConn.prepareStatement(sql);
					stmt.executeUpdate();
					dbConn.commit();
					

				} catch (SQLException e) {
					System.out.println("Error inserting : Sessions");
					e.printStackTrace();
					try {
						dbConn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				
			System.out.println("Test data inserted: Sessions");

		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out
						.println("Could not close PreparedStatement connection");
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * Adds test data to the Type table. 
	 * @param numType int	- Number of types to add
	 */
	@SuppressWarnings("resource")
	private void addTestData_Type(int numType){
		PreparedStatement stmt = null;

		try {
				try {
					String sql = "INSERT INTO " + 
									"Type(typeString) " + 
									"VALUES(" + 
										"'normal'" +
								");";
					stmt = dbConn.prepareStatement(sql);
					stmt.executeUpdate();
					dbConn.commit();

					sql = "INSERT INTO " + 
							"Type(typeString) " + 
							"VALUES(" + 
								"'resit'" +
						");";
					stmt = dbConn.prepareStatement(sql);
					stmt.executeUpdate();
					dbConn.commit();
					
					sql = "INSERT INTO " + 
							"Type(typeString) " +
							"VALUES(" + 
								"'repeat'" +
						");";
					stmt = dbConn.prepareStatement(sql);
					stmt.executeUpdate();
					dbConn.commit();

				} catch (SQLException e) {
					System.out.println("Error inserting : Type");
					e.printStackTrace();
					try {
						dbConn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
				
			System.out.println("Test data inserted: Type");

		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out
						.println("Could not close PreparedStatement connection");
				e.printStackTrace();
			}
		}		
	}
	
	/**
	 * Adds test data to the Next of kin table. 
	 * @param numStudents int	- Number of students added
	 * @param numNextOfKin int 	- Number of nextOfKin added
	 */
	@SuppressWarnings("null")
	private void addTestData_NextOfKin(int numStudents, int numNextOfKin){
		PreparedStatement stmt = null;

		try {
			int[] usedStudentIDs = new int[numNextOfKin];
			for(int i = 0; i < numNextOfKin; i++){
				usedStudentIDs[i] = getUniqueRandomNumberBetween(1, numStudents, usedStudentIDs);
				
				try {
					String sql = "INSERT INTO " + 
									"NextOfKin(studentID,eMailAddress,postalAddress) " + 
									"VALUES(" + 
										"'" + usedStudentIDs[i] + "', " +
										"'" + "emailAddress" + (i+1) +"@testing.com', " +
										"'" + "PostalAddress" + (i+1) + "'" +
									");";
					stmt = dbConn.prepareStatement(sql);
					stmt.executeUpdate();
					dbConn.commit();


				} catch (SQLException e) {
					System.out.println("Error inserting : NextOfKin" + i);
					usedStudentIDs[i] = (Integer) null;
					try {
						dbConn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					e.printStackTrace();
				}
			}
				
			System.out.println("Test data inserted: NextOfKin");

		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out
						.println("Could not close PreparedStatement connection");
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Adds test data to the Lecturer table. 
	 * @param numLecturer int 	- Number of lecturers to add
	 * @param numTitles	int 	- Number of titles added
	 */
	private void addTestData_Lecturer(int numLecturer, int numTitles){
		PreparedStatement stmt = null;

		try {
			for(int i = 0; i < numLecturer; i++){
				
				try {
					String sql = "INSERT INTO " + 
									"Lecturer(titleID,forname,familyName) " + 
									"VALUES(" +
										"'" + randomNumberBetween(1, numTitles) + "', " +
										"'" + "forNameLecturer" + (i+1) + "', " +
										"'" + "familyNameLecturer" + (i+1) + "'" +
									");";
					stmt = dbConn.prepareStatement(sql);
					stmt.executeUpdate();
					dbConn.commit();


				} catch (SQLException e) {
					System.out.println("Error inserting : Lecturer");
					e.printStackTrace();
					try {
						dbConn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
				
			System.out.println("Test data inserted: Lecturer");

		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out
						.println("Could not close PreparedStatement connection");
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Adds test data to the Course table. 
	 * @param numCourse	int		- number of courses to add
	 * @param numLecturer int	- Number of lecurers added
	 */
	private void addTestData_Course(int numCourse, int numLecturer){
		PreparedStatement stmt = null;

		try {
			for(int i = 0; i < numCourse; i++){
				
				try {
					String sql = "INSERT INTO " + 
									"Course(courseName, courseDescription, lecturerID) " + 
									"VALUES(" +
										"'" + "courseName" + (i+1) + "', " +
										"'" + "courseDescription" + (i+1) + "', " +
										"'" + randomNumberBetween(1, numLecturer) + "'" +
									");";
					stmt = dbConn.prepareStatement(sql);
					stmt.executeUpdate();
					dbConn.commit();


				} catch (SQLException e) {
					System.out.println("Error inserting : Course");
					e.printStackTrace();
					try {
						dbConn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
				
			System.out.println("Test data inserted: Course");

		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out
						.println("Could not close PreparedStatement connection");
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Adds test data to the Marks table. 
	 * @param numMarks int 		- Number of marks to add
	 * @param numCourse	int 	- number of courses added
	 * @param numType int 		- number of types added
	 * @param numStudentsint 	- number of students added
	 */
	private void addTestData_Marks(int numMarks, int numCourse, int numType, int numStudents){
		PreparedStatement stmt = null;

		try {
			for(int i = 0; i < numMarks; i++){
				
				
				
				try {
					int studentID = randomNumberBetween(1, numStudents);
					int courseID = randomNumberBetween(1, numCourse);
					int year = randomNumberBetween(1, 5);
					int sessionID = randomNumberBetween(1, 2);
					int typeID = randomNumberBetween(1, numType);
					int mark = randomNumberBetween(0, 100);
					String notes = "Notes notes note. Note note note notes.";
					
					
					String sql = "INSERT INTO " + 
									"Marks(studentID, CourseID, year, sessionID, typeID, mark, notes) " + 
									"VALUES(" +
										"'" + studentID + "', " +
										"'" + courseID + "', " +
										"'" + year + "', " +
										"'" + sessionID + "', " +
										"'" + typeID + "', " +
										"'" + mark + "', " +
										"'" + notes + "'" +
									");";
					stmt = dbConn.prepareStatement(sql);
					stmt.executeUpdate();
					dbConn.commit();


				} catch (SQLException e) {
					System.out.println("Error inserting : Marks");
					e.printStackTrace();
					try {
						dbConn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
				
			System.out.println("Test data inserted: Marks");

		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out
						.println("Could not close PreparedStatement connection");
				e.printStackTrace();
			}
		}	
	}
	
	/**
	 * Adds test data to the StudentContact table. 
	 * @param numStudents int	- NUmber of students added
	 */
	@SuppressWarnings("null")
	private void addTestData_StudentContact(int numStudents){
		PreparedStatement stmt = null;

		try {
			int[] listOfUsedIDs = new int[numStudents];
			for(int i = 0; i < numStudents; i++){
				
				try {
					listOfUsedIDs[i] = getUniqueRandomNumberBetween(1, numStudents, listOfUsedIDs);
					
					String sql = "INSERT INTO " + 
									"StudentContact(studentID, eMailAddress, postalAddress) " + 
									"VALUES(" +
										"'" + listOfUsedIDs[i] + "', " +
										"'" + "eMailAddress" + (i+1) + "@test.com', " +
										"'" + "postalAddress" + (i+1) + "'" +
									");";
					stmt = dbConn.prepareStatement(sql);
					stmt.executeUpdate();
					dbConn.commit();


				} catch (SQLException e) {
					System.out.println("Error inserting : StudentContact");
					e.printStackTrace();
					listOfUsedIDs[i] = (Integer) null;
					try {
						dbConn.rollback();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
				}
			}
				
			System.out.println("Test data inserted: StudentContact");

		} finally {
			try {
				stmt.close();
			} catch (SQLException e) {
				System.out
						.println("Could not close PreparedStatement connection");
				e.printStackTrace();
			}
		}	
	}
	
	
	
	/**
	 * Creats a table from the data in the result set provided.
	 * @param rs ResultSet			- The result set of the query
	 * @return	DefaultTableModel	- The completed table model
	 * @throws SQLException
	 */
	public DefaultTableModel buildTableModel(ResultSet rs)
	        throws SQLException {

	    ResultSetMetaData metaData = rs.getMetaData();

	    // names of columns
	    Vector<String> columnNames = new Vector<String>();
	    int columnCount = metaData.getColumnCount();
	    for (int column = 1; column <= columnCount; column++) {
	        columnNames.add(metaData.getColumnName(column));
	    }

	    // data of the table
	    Vector<Vector<Object>> data = new Vector<Vector<Object>>();
	    while (rs.next()) {
	        Vector<Object> vector = new Vector<Object>();
	        for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
	            vector.add(rs.getObject(columnIndex));
	        }
	        data.add(vector);
	    }

	    return new DefaultTableModel(data, columnNames);

	}
	
	/**
	 * Generates a random number and checks it against the list of previously used ones.
	 * @param min int				- Min value
	 * @param max int				- Max value
	 * @param listOfUsedIDs int[]	- Int array of used values
	 * @return int					- Unique random number.
	 */
	private int getUniqueRandomNumberBetween(int min, int max, int[] listOfUsedIDs){
		int r = randomNumberBetween(min, max);
		for(int i = 0; i < listOfUsedIDs.length; i++){
			if(listOfUsedIDs[i] == r){
				r = getUniqueRandomNumberBetween(min, max, listOfUsedIDs);
			}
		}
		return r;
	}
	
	/**
	 * Generates a random sex.
	 * @return String	- m/f
	 */
	private String randomSex(){
		int i = randomNumberBetween(0,1);
		if(i == 0){
			return "m";
		}else
			return "f";
	}
	
	/**
	 * Generates a random date of birth in the format
	 * dd-mm-yyy
	 * @return	String - Date
	 */
	private String randomDateOfBirth(){

		int year = randomNumberBetween(1950, 2010);
		int month = randomNumberBetween(1, 12);
		int day = randomNumberBetween(1, 28);
		
		return day + "-" + month + "-" + year;

	}
	private int randomNumberBetween(int min, int max){
        return min + (int)Math.round(Math.random() * (max - min));

	}
}
