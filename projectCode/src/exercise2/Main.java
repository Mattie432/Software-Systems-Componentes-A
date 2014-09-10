package exercise2;

public class Main {

	/**
	 * @param args
	 */
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		
		final Database db = new Database();
		
		UserInterface_Main mainUI = new UserInterface_Main(db);


		//Disconnects from the database when the program closes.
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
			System.out.println("Closing Database Connection.");
				db.disconnectFromDatabase();
			}
			});
	}

	
	
	
}
