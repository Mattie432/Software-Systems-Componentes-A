package exercise3;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;

public class UI_Message {

	private JFrame frame;
	private JLabel lblFrom;
	private JLabel lblTo;
	private JLabel lblSubject;
	private JLabel lblDate;
	private JTextArea textArea;

	/**
	 * Create the application.
	 */
	public UI_Message(String to, String from, String subject, String date, String message) {
		initialize();
		showMsg(to, from, subject, date, message);
		frame.setVisible(true);
	}

	
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.setBounds(100, 100, 616, 575);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel topPannel = new JPanel();
		topPannel.setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		frame.getContentPane().add(topPannel, BorderLayout.NORTH);
		topPannel.setLayout(new BoxLayout(topPannel, BoxLayout.Y_AXIS));
		
		JPanel fromPannel = new JPanel();
		topPannel.add(fromPannel);
		fromPannel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblNewLabel_1 = new JLabel("From: ");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
		fromPannel.add(lblNewLabel_1);
		
		lblFrom = new JLabel("");
		fromPannel.add(lblFrom);
		
		JPanel toPanel = new JPanel();
		topPannel.add(toPanel);
		toPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblNewLabel = new JLabel("To: ");
		toPanel.add(lblNewLabel);
		
		lblTo = new JLabel("");
		toPanel.add(lblTo);
		
		JPanel subjectPannel = new JPanel();
		topPannel.add(subjectPannel);
		subjectPannel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblNewLabel_2 = new JLabel("Subject: ");
		subjectPannel.add(lblNewLabel_2);
		
		lblSubject = new JLabel("");
		subjectPannel.add(lblSubject);
		
		JPanel datePannel = new JPanel();
		topPannel.add(datePannel);
		datePannel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		
		JLabel lblNewLabel_3 = new JLabel("Date: ");
		datePannel.add(lblNewLabel_3);
		
		lblDate = new JLabel("");
		datePannel.add(lblDate);
		
		JPanel msgPannel = new JPanel();
		frame.getContentPane().add(msgPannel, BorderLayout.CENTER);
		msgPannel.setLayout(new BorderLayout(0, 0));
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		msgPannel.add(textArea, BorderLayout.CENTER);
	}
	
	
	/**
	 * Show the current message
	 * @param to		String - to address
	 * @param from		String - from address
	 * @param subject	String - subject
	 * @param date		String - message sent date
	 * @param message	String - the message
	 */
	private void showMsg(String to, String from, String subject, String date, String message){
		lblDate.setText(date);
		lblFrom.setText(from);
		lblSubject.setText(subject);
		lblTo.setText(to);
		textArea.setText(message);
		
		
	}
	

}
