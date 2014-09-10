package exercise3;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JLabel;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JTextArea;
import javax.swing.border.EtchedBorder;
import javax.swing.JTextField;
import javax.swing.JButton;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

public class UI_CreateMessage {

	private JFrame frame;
	private JTextArea textArea;
	private JTextField txtTo;
	private JTextField txtSubject;
	private JTextField txtCC;
	private JLabel lblAttachments;
	
	private NewMessage message;
	
	/**
	 * Create the application.
	 */
	public UI_CreateMessage(Mail mail) {
		try {
			message = new NewMessage(mail.getSession(), mail.getUserName());
		} catch (AddressException e) {
			System.out.println("An error occoured creating a new message object");
			System.out.println(e.getMessage());
		} catch (MessagingException e) {
			System.out.println("An error occoured creating a new message object");
			System.out.println(e.getMessage());
		}
		
		initialize();
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
		
		JPanel toPanel = new JPanel();
		topPannel.add(toPanel);
		toPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		toPanel.add(panel, BorderLayout.WEST);
		
		JLabel lblNewLabel = new JLabel("To: ");
		panel.add(lblNewLabel);
		
		JPanel panel_1 = new JPanel();
		toPanel.add(panel_1, BorderLayout.CENTER);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		txtTo = new JTextField();
		panel_1.add(txtTo);
		txtTo.setColumns(50);
		
		JPanel ccPannel = new JPanel();
		topPannel.add(ccPannel);
		ccPannel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		ccPannel.add(panel_2, BorderLayout.WEST);
		
		JLabel lblCc = new JLabel("CC:");
		panel_2.add(lblCc);
		
		JPanel panel_5 = new JPanel();
		ccPannel.add(panel_5, BorderLayout.CENTER);
		panel_5.setLayout(new BorderLayout(0, 0));
		
		txtCC = new JTextField();
		panel_5.add(txtCC, BorderLayout.CENTER);
		txtCC.setColumns(10);
		
		JPanel subjectPannel = new JPanel();
		topPannel.add(subjectPannel);
		subjectPannel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_4 = new JPanel();
		subjectPannel.add(panel_4, BorderLayout.WEST);
		
		JLabel lblSubject = new JLabel("Subject:");
		panel_4.add(lblSubject);
		
		JPanel panel_3 = new JPanel();
		subjectPannel.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		txtSubject = new JTextField();
		panel_3.add(txtSubject);
		txtSubject.setColumns(10);
		
		JPanel msgPannel = new JPanel();
		frame.getContentPane().add(msgPannel, BorderLayout.CENTER);
		msgPannel.setLayout(new BorderLayout(0, 0));
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		msgPannel.add(textArea, BorderLayout.CENTER);
		
		JPanel btnPannel = new JPanel();
		frame.getContentPane().add(btnPannel, BorderLayout.SOUTH);
		btnPannel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_6 = new JPanel();
		btnPannel.add(panel_6, BorderLayout.EAST);
		
		JButton btnAddAttachment = new JButton("Add Attachment");
		btnAddAttachment.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				addAttachment();
			}
		});
		panel_6.add(btnAddAttachment);
		
		JButton btnSend = new JButton("Send");
		panel_6.add(btnSend);
		
		JPanel panel_7 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_7.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		btnPannel.add(panel_7, BorderLayout.CENTER);
		
		JLabel lblAttachments2 = new JLabel("Attachments: ");
		panel_7.add(lblAttachments2);
		
		lblAttachments = new JLabel("");
		panel_7.add(lblAttachments);
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				sendMessage();
			}
		});
	}
	
	/**
	 * Ask the user to specify a file to add as an attachment
	 */
	public void addAttachment(){
		JFileChooser fileChooser = new JFileChooser();
		int returnVal = fileChooser.showOpenDialog(this.frame);
		
		if (returnVal == JFileChooser.APPROVE_OPTION) {
	        File file = fileChooser.getSelectedFile();
	        
	        System.out.println("Adding file attachment:");
	        System.out.println(file.getAbsolutePath());

	        
	        try {
				message.addAttatchment(file.getAbsolutePath(), file.getName());
			} catch (MessagingException e) {

				System.out.println("An error occoured adding an attachment");
				System.out.println(e.getMessage());
			}
	        
	        lblAttachments.setText(lblAttachments.getText() + file.getName() + ", ");
	        
	        
	    } else {
	        System.out.println("File access cancelled by user.");
	    }
		
	}
	
	/**
	 * Sends the message.
	 */
	public void sendMessage(){
		final JFrame f = this.frame;
		Thread t = new Thread() {
			@Override
			public void run() {  // override the run() for the running behaviors
		
			     f.dispose();
				try {
					message.sendMessage(txtTo.getText(), txtSubject.getText(), textArea.getText(), txtCC.getText());
					
			        
				} catch (AddressException e) {
					System.out.println("An error occoured sending the message");
					System.out.println(e.getMessage());
				} catch (MessagingException e) {
					System.out.println("An error occoured sending the message");
					System.out.println(e.getMessage());
				}
			}
		};
		t.start();
		
	}
	

}
