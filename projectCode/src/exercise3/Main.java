package exercise3;

import javax.swing.SwingUtilities;

public class Main {

	/**
	 * Main method for the class.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {

		final Mail mail = new Mail();
		mail.initializeConnection();

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				UI_Main ui_mail = new UI_Main(mail);
				mail.setUi_main(ui_mail);
				Thread t = new Thread(new RetrieveMessages(ui_mail, mail));
				t.start();
				mail.execute();

			}
		});
		// close connection when the program closes.
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				System.out.println("Closing Connection.");
				mail.closeConnetions();
			}
		});

	}

}
