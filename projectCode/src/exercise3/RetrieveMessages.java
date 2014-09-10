package exercise3;

import java.io.IOException;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import com.sun.mail.imap.IMAPFolder;

/**
 * This class should be started in a new thread,
 * It will retrieve any new messages and display them 
 * in the JTAble.
 * 
 * @author Mattie432
 *
 */
public class RetrieveMessages implements Runnable {

	private UI_Main ui_Main;
	private Mail mail;
	
	
	public RetrieveMessages(UI_Main ui_main, Mail mail) {
		this.ui_Main = ui_main;
		this.mail = mail;
		
	}

	/**
	 * The main code that will run in this thread.
	 * This checks for messages and updates the table with them.
	 */
	@Override
	public void run() {
		try {
			ui_Main.clearTable();
			// Choose folder
			mail.folder = (IMAPFolder) mail.store.getFolder(mail.currentMsgFolder);

			if (!mail.folder.isOpen())
				mail.folder.open(Folder.READ_WRITE);
			// Get total number of message
			System.out.println("No of Messages : " + mail.folder.getMessageCount());
			mail.numMessages = mail.folder.getMessageCount();
			// Get total number of unread message
			System.out.println("No of Unread Messages : "
					+ mail.folder.getUnreadMessageCount());

			final Message messages[] = mail.folder.getMessages();

			ui_Main.resetProgressBar();
			
			int msgCount = 0;
			
			for (int i = messages.length - mail.numMsgStart; (i >= 0 && i > (messages.length - 1)
					- mail.numMsgEnd); i--) {

				Message msg = messages[i];

				String flags = mail.retrieveFlags(msg.getFlags());

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

					ui_Main.addMsgToTable(flags, date, sender, subject,
							msgPreview);
					
					ui_Main.updateProgressBar(++msgCount, mail.numMsgEnd - mail.numMsgStart+1);
					
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

}
