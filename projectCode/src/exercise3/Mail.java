package exercise3;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingWorker;
import javax.mail.Multipart;
import com.sun.mail.imap.IMAPFolder;

public class Mail extends SwingWorker<Integer, String> {
	static Mail mail = null;
	private static UI_Main ui_main = null;
	// Number of msgs to display on a page
	static int msgPageLength = 10;
	int numMsgStart = 1;
	int numMsgEnd = msgPageLength;
	int numMessages = 0;
	String currentMsgFolder = "inbox";
	IMAPFolder folder = null;
	Store store = null;
	Properties props;
	Session session;
	Boolean checkNewMessages = true;
	boolean processing = false;

	// USED FOR TESTING CONVINIENCE
	String username = "--removed";
	String password = "--removed--";

	/**
	 * Change the current folder to the one specified in the parameter. (NOTE
	 * need to call retrieveMessages() to show these changes.
	 * 
	 * @param newFolder
	 *            - String - The folder to change to.
	 * @return Boolean - did the operation complete
	 */
	public boolean changeCurrentFolder(String newFolder) {
		boolean temp = false;
		try {
			if (store.getFolder(newFolder).exists()) {
				currentMsgFolder = newFolder;
				folder = (IMAPFolder) store.getFolder(currentMsgFolder);
				return temp = true;
			}
		} catch (MessagingException e) {
			System.out.println("Error trying to change foler to " + newFolder);
			// e.printStackTrace();
		}
		return temp;
	}

	/**
	 * Changes the range of messages which are shown/looked at.
	 * 
	 * @param msgStart
	 *            - Start index of the range (0 based)
	 * @param msgEnd
	 *            - End index of the range (0 based)
	 * @return Boolean - did the operation complete?
	 */
	public boolean changeMessageLimits(int msgStart, int msgEnd) {
		boolean temp = false;
		try {
			if (folder.getMessages().length >= msgEnd && msgStart > 0) {
				numMsgEnd = msgEnd;
				numMsgStart = msgStart;
				temp = true;
			}
		} catch (MessagingException e) {
			System.out.println("Error setting message limits start=" + msgStart
					+ " end=" + msgEnd);
			// e.printStackTrace();
		}
		return temp;
	}

	/**
	 * Connects to the mail server after asking the user to input a password.
	 */
	public void initializeConnection() {

		// Step 1: Set all Properties
		props = System.getProperties();
		props.setProperty("mail.store.protocol", "imaps");

		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.starttls.enable", true);
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		JPasswordField pwd = new JPasswordField(10);
		int action = JOptionPane.showConfirmDialog(null, pwd, "Enter Password",
				JOptionPane.OK_CANCEL_OPTION);
		if (action < 0) {
			JOptionPane.showMessageDialog(null,
					"Cancel, X or escape key selected");
			// System.exit(0);
		} else {
			password = new String(pwd.getPassword());
		}

		// Set Property with username and password for authentication
		props.setProperty("mail.user", username);
		props.setProperty("mail.password", password);

		// Step 2: Establish a mail session (java.mail.Session)
		// session = Session.getDefaultInstance(props);
		setSession(username, password);

		try {
			// We need to get Store from mail session
			// A store needs to connect to the IMAP server
			store = session.getStore("imaps");
			store.connect("imap.googlemail.com", username, password);

		} catch (NoSuchProviderException e) {
			System.out.println("An error occoured establishing a connection!");
			System.out.println(e.getMessage());
		} catch (MessagingException e) {
			System.out.println("An error occoured establishing a connection!");
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Method to set the current sessions username and password. Used when
	 * establishing a connection.
	 * 
	 * @param usernames
	 *            String - username
	 * @param passwords
	 *            String - password
	 */
	private void setSession(final String usernames, final String passwords) {
		session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(usernames, passwords);
			}
		});
	}

	/**
	 * Retrieves all messages within the specified range and displays them to
	 * the UI_Mail instance.
	 * 
	 * This adds each message to the table to display them.
	 */
	public void retrieveMessages() {

		System.out.println("Getting messages");
		try {

			getUi_main().clearTable();

			// Choose folder
			folder = (IMAPFolder) store.getFolder(currentMsgFolder);

			if (!folder.isOpen())
				folder.open(Folder.READ_WRITE);
			// Get total number of message
			System.out.println("No of Messages : " + folder.getMessageCount());
			numMessages = folder.getMessageCount();
			// Get total number of unread message
			System.out.println("No of Unread Messages : "
					+ folder.getUnreadMessageCount());

			final Message messages[] = folder.getMessages();

			getUi_main().resetProgressBar();
			for (int i = messages.length - numMsgStart; (i >= 0 && i > (messages.length - 1)
					- numMsgEnd); i--) {

				Message msg = messages[i];

				String flags = retrieveFlags(msg.getFlags());

				String date = msg.getReceivedDate().toString();
				String sender = msg.getFrom()[0].toString();
				String subject = msg.getSubject().toString();
				String msgPreview = "";

				try {
					if (msg.getContentType().contains("TEXT/PLAIN")) {
						msgPreview = (String) (msg.getContent());
					} else {
						// How to get parts from multiple body parts of
						// MIME message
						Multipart multipart = (Multipart) msg.getContent();
						for (int x = 0; x < multipart.getCount(); x++) {
							BodyPart bodyPart = multipart.getBodyPart(x);
							// If the part is a plan text message, then
							// print it out.
							if (bodyPart.getContentType()
									.contains("TEXT/PLAIN")) {
								msgPreview = (bodyPart.getContent().toString());
							}

						}
					}

					getUi_main().addMsgToTable(flags, date, sender, subject,
							msgPreview);
				} catch (IOException e) {
					System.out
							.println("Error getting msg body for preview. Msg: "
									+ i);
				} catch (Exception e) {
					System.out.println("Exception with message " + i);
				}

			}
		} catch (MessagingException e) {
			System.out.println("An error occoured retrieving messages");
			System.out.println(e.getMessage());
		}

	}

	/**
	 * Closes all connections to the server.
	 */
	public void closeConnetions() {
		try {
			if (folder != null && folder.isOpen()) {
				folder.close(true);
			}
			if (store != null) {
				store.close();
			}
		} catch (MessagingException e) {
			System.out.println("An error occoured closing the connection!");
			System.out.println(e.getMessage());
		}
	}

	/**
	 * This retrieves the data from a specific message and displays it inside a
	 * new UI_Message.
	 * 
	 * @param msgNum
	 *            int - The message number (0 Based)
	 */
	public void displayMsg(int msgNum) {
		try {
			Message msg = folder.getMessage(folder.getMessageCount() - msgNum);

			String to = "";
			for (Address address : msg.getRecipients(Message.RecipientType.TO)) {
				to += address.toString() + ", ";
			}

			String date = msg.getReceivedDate().toString();
			String sender = msg.getFrom()[0].toString();
			String subject = msg.getSubject().toString();
			String msgPreview = "";

			try {
				if (msg.getContentType().contains("TEXT/PLAIN")) {
					msgPreview = (String) (msg.getContent());
				} else {
					// How to get parts from multiple body parts of MIME message
					Multipart multipart = (Multipart) msg.getContent();
					for (int x = 0; x < multipart.getCount(); x++) {
						BodyPart bodyPart = multipart.getBodyPart(x);
						// If the part is a plan text message, then print it
						// out.
						if (bodyPart.getContentType().contains("TEXT/PLAIN")) {
							// System.out.println(bodyPart.getContentType());
							msgPreview = (bodyPart.getContent().toString());
						}

					}
				}
			} catch (IOException e) {
				System.out.println("Error displaying msg no. " + msgNum);
			}

			@SuppressWarnings("unused")
			UI_Message ui_message = new UI_Message(to, sender, subject, date,
					msgPreview);

		} catch (MessagingException e) {
			System.out.println("An error occoured displaying a message.");
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Retrieves the names of each flag in the array.
	 * 
	 * @param flags
	 *            Flags - list of flags in a message
	 * @return String - all of the accociated flags.
	 */
	String retrieveFlags(Flags flags) {
		ArrayList<String> lst = new ArrayList<String>();

		if (flags.contains(Flags.Flag.DELETED)) {
			lst.add("Deleted");
		}
		if (flags.contains(Flags.Flag.ANSWERED)) {
			lst.add("Answered");
		}
		if (flags.contains(Flags.Flag.DRAFT)) {
			lst.add("Draft");
		}
		if (flags.contains(Flags.Flag.FLAGGED)) {
			lst.add("Marked");
		}
		if (flags.contains(Flags.Flag.RECENT)) {
			lst.add("Recent");
		}
		if (flags.contains(Flags.Flag.SEEN)) {
			lst.add("Read");
		}

		StringBuilder str = new StringBuilder();
		for (String s : lst) {
			str.append(s + ", ");
		}
		str.replace(str.lastIndexOf(", "), str.length(), "");
		return str.toString();

	}

	/**
	 * Retrieves the session object
	 * 
	 * @return Session - the session object in use
	 */
	public Session getSession() {
		return session;
	}

	/**
	 * Gets the username in use
	 * 
	 * @return String - username
	 */
	public String getUserName() {
		return username;
	}

	/**
	 * Gets all folders that are accessible to the user from the mail server.
	 * 
	 * @return Folder[] - all folders accessible
	 * @throws MessagingException
	 */
	public Folder[] getAvailableFolders() throws MessagingException {
		Folder[] f = store.getDefaultFolder().list();
		return f;
	}

	/**
	 * Gets the current starting message number
	 * 
	 * @return int - Starting index
	 */
	public int getNumMsgStart() {
		return numMsgStart;
	}

	/**
	 * Gets the currend ending message number
	 * 
	 * @return int - Ending index
	 */
	public int getNumMsgEnd() {
		return numMsgEnd;
	}

	/**
	 * Gets the current page length
	 * 
	 * @return int - page length
	 */
	public int getMsgPageLength() {
		return msgPageLength;
	}

	/**
	 * Getter method for the ui_main
	 * 
	 * @return
	 */
	public UI_Main getUi_main() {
		return ui_main;
	}

	/**
	 * Setter method for the UI_Main
	 * 
	 * @param ui_main
	 */
	public void setUi_main(UI_Main ui_main) {
		Mail.ui_main = ui_main;
	}

	/**
	 * Background process to constantly check for new messages.
	 */
	protected Integer doInBackground() throws Exception {
		while (checkNewMessages) {
			Thread.sleep(10000);
			System.out.println("Looking for new messages");
			try {

				// Choose folder
				folder = (IMAPFolder) store.getFolder(currentMsgFolder);

				if (!folder.isOpen())
					folder.open(Folder.READ_WRITE);

				if (numMessages < folder.getMessageCount()) {

					int difference = folder.getMessageCount() - numMessages;

					// getUi_mail().resetProgressBar();
					for (int i = numMessages + 1; i <= numMessages + difference; i++) {

						publish("gogogo");

					}

				}

			} catch (MessagingException e) {
				System.out.println("An error occoured retrieving new messages");
				System.out.println(e.getMessage());
			}

		}
		return null;
	}

	/**
	 * Processing for the background process. This is called when a new message
	 * is detected. Its then added into the JTable.
	 */
	@Override
	protected void process(List<String> chunks) {

		checkNewMessages = false;
		try {

			// Choose folder
			folder = (IMAPFolder) store.getFolder(currentMsgFolder);

			if (!folder.isOpen())
				folder.open(Folder.READ_WRITE);

			if (numMessages < folder.getMessageCount()) {
				System.out.println("New Messages found!");

				int difference = folder.getMessageCount() - numMessages;

				Message messages[] = folder.getMessages();

				// getUi_mail().resetProgressBar();
				for (int i = numMessages; i < numMessages + difference; i++) {

					Message msg = messages[i];

					String flags = retrieveFlags(msg.getFlags());

					String date = msg.getReceivedDate().toString();
					String sender = msg.getFrom()[0].toString();
					String subject = msg.getSubject().toString();
					String msgPreview = "";

					if (msg.getContentType().contains("TEXT/PLAIN")) {
						msgPreview = (String) (msg.getContent());
					} else {
						// How to get parts from multiple body parts of
						// MIME message
						Multipart multipart = (Multipart) msg.getContent();
						for (int x = 0; x < multipart.getCount(); x++) {
							BodyPart bodyPart = multipart.getBodyPart(x);
							// If the part is a plan text message, then
							// print it out.
							if (bodyPart.getContentType()
									.contains("TEXT/PLAIN")) {
								msgPreview = (bodyPart.getContent().toString());
							}

						}
					}

					getUi_main().addMsgToTableTop(flags, date, sender, subject,
							msgPreview);
				}

				numMessages = folder.getMessageCount();
			}
		} catch (IOException e) {
			System.out.println("Error getting msg body for preview. Msg: ");
		} catch (Exception e) {
			System.out.println("Exception with message ");
			// e.printStackTrace();
		} finally {
			checkNewMessages = true;
		}
	}

}
