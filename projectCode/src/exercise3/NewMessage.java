package exercise3;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;


public class NewMessage {
	private Message message;
	private Multipart multipart = new MimeMultipart();
	
	
	/**
	 * Constructor for the class.
	 * @param session	Session - current session
	 * @param emailAddress	String - Email/UserName in use
	 * @throws AddressException	
	 * @throws MessagingException
	 */
	public NewMessage(Session session, String emailAddress) throws AddressException, MessagingException{
		
		message = new MimeMessage(session);
		message.setFrom(new InternetAddress(emailAddress));
	}

	
	/**
	 * Adds an attachment to the message.
	 * @param filePath	String - The full path to the file
	 * @param fileName	String - The file name (Including extension)
	 * @throws MessagingException
	 */
	public void addAttatchment(String filePath, String fileName) throws MessagingException{
		
		MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(filePath);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);
        multipart.addBodyPart(messageBodyPart);
	
	}

	
	/**
	 * Sends the message
	 * @param toAddress	String - the person to send the message to
	 * @param subject	String - the message subject
	 * @param msg		String - the message body text
	 * @param cc		String - email addresses to cc (separated by a comma)
	 * @throws AddressException
	 * @throws MessagingException
	 */
	public void sendMessage(String toAddress, String subject, String msg, String cc) throws AddressException, MessagingException{
	
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(toAddress));
        if(cc.length() > 0){
	        message.setRecipients(Message.RecipientType.CC,
	                InternetAddress.parse(cc));
        }
        message.setSubject(subject);
        message.setText(msg);
        
        if(multipart.getCount() > 0){
	        //add attachments
	        message.setContent(multipart);
        }

        System.out.println("Sending");
        Transport.send(message);
        System.out.println("Message sent sucsessfully");
	}
	
}