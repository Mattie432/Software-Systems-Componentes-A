package exercise3;

import javax.mail.Folder;
import javax.mail.MessagingException;
import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.JScrollPane;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JProgressBar;
import javax.swing.SwingConstants;
import javax.swing.ListSelectionModel;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import java.awt.Component;

public class UI_Main {

	private UI_Main _this;
	private JFrame frame;
	private DefaultTableModel model;
	private JTable table;
	private Mail mail;
	private JProgressBar progressBar;
	private JPanel folderPannel;

	/**
	 * Create the application.
	 */
	public UI_Main(Mail mail) {
		initialize();
		this.mail = mail;
		addEmailFolders();
		this.frame.setVisible(true);
		_this = this;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings("serial")
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 960, 588);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		JPanel leftPanel = new JPanel();
		frame.getContentPane().add(leftPanel, BorderLayout.WEST);
		leftPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel_1 = new JPanel();
		leftPanel.add(panel_1, BorderLayout.NORTH);

		JButton btnNewEmail = new JButton("Create Email");
		panel_1.add(btnNewEmail);
		btnNewEmail.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				@SuppressWarnings("unused")
				UI_CreateMessage ui = new UI_CreateMessage(mail);
			}
		});
		btnNewEmail.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});
		btnNewEmail.setVerticalAlignment(SwingConstants.TOP);

		folderPannel = new JPanel();
		leftPanel.add(folderPannel, BorderLayout.SOUTH);
		folderPannel.setLayout(new BoxLayout(folderPannel, BoxLayout.Y_AXIS));

		JLabel lblFolders = new JLabel("Folders:");
		lblFolders.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblFolders.setHorizontalAlignment(SwingConstants.CENTER);
		folderPannel.add(lblFolders);

		JPanel tablePanel = new JPanel();
		frame.getContentPane().add(tablePanel, BorderLayout.CENTER);
		tablePanel.setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		tablePanel.add(scrollPane);

		table = new JTable();
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				openSelectedMsg();
			}
		});
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setModel(new DefaultTableModel(new Object[][] {}, new String[] {
				"Flags", "Date", "Sender", "Subject", "Message Preview" }) {
			@SuppressWarnings("rawtypes")
			Class[] columnTypes = new Class[] { String.class, String.class,
					String.class, String.class, Object.class };

			@SuppressWarnings({ "unchecked", "rawtypes" })
			public Class getColumnClass(int columnIndex) {
				return columnTypes[columnIndex];
			}
		});
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getColumnModel().getColumn(0).setMaxWidth(200);
		table.getColumnModel().getColumn(2).setPreferredWidth(200);
		table.getColumnModel().getColumn(2).setMaxWidth(400);
		table.getColumnModel().getColumn(3).setPreferredWidth(150);
		table.getColumnModel().getColumn(3).setMaxWidth(200);
		table.getColumnModel().getColumn(4).setPreferredWidth(300);
		scrollPane.setViewportView(table);

		JPanel mailOptionsPanel = new JPanel();
		tablePanel.add(mailOptionsPanel, BorderLayout.SOUTH);
		mailOptionsPanel.setLayout(new BorderLayout(0, 0));

		JPanel panel = new JPanel();
		mailOptionsPanel.add(panel);

		JButton buttonPrevious = new JButton("< Previous");
		panel.add(buttonPrevious);

		JButton btnNext = new JButton("Next >");
		panel.add(btnNext);

		JButton btnRefresh = new JButton("Refresh");
		btnRefresh.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// mail.retrieveMessages();
				Thread t = new Thread(new RetrieveMessages(_this, mail));
				t.start();
			}
		});
		panel.add(btnRefresh);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		mailOptionsPanel.add(progressBar, BorderLayout.EAST);
		btnNext.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (mail.changeMessageLimits(
						mail.getNumMsgStart() + mail.getMsgPageLength(),
						mail.getNumMsgEnd() + mail.getMsgPageLength())) {
					Thread t = new Thread(new RetrieveMessages(_this, mail));
					t.start();
				}
			}
		});
		buttonPrevious.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if (mail.changeMessageLimits(
						mail.getNumMsgStart() - mail.getMsgPageLength(),
						mail.getNumMsgEnd() - mail.getMsgPageLength())) {
					Thread t = new Thread(new RetrieveMessages(_this, mail));
					t.start();
				}
			}
		});
		buttonPrevious.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}
		});

		model = (DefaultTableModel) table.getModel();
	}

	/**
	 * Adds all of the available folders to the user interface. Also adds the
	 * click function to each button which will change the current folder to
	 * that one.
	 */
	private void addEmailFolders() {
		try {
			Folder[] f = mail.getAvailableFolders();
			for (Folder fd : f) {
				final JButton button = new JButton(fd.getName());

				JPanel p = new JPanel();
				p.setLayout(new BorderLayout());
				p.add(button, BorderLayout.CENTER);

				button.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						System.out.println("Changing to : " + button.getText());
						mail.changeCurrentFolder(button.getText());
						mail.retrieveMessages();

					}
				});

				folderPannel.add(p);
			}

		} catch (MessagingException e) {
			System.out.println("An error occoured adding a folder.");
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Opens the selected message
	 */
	public void openSelectedMsg() {
		System.out.println(table.getSelectedRow());
		mail.displayMsg(table.getSelectedRow());
	}

	/**
	 * Adds a message to the message table.
	 * 
	 * @param num
	 *            Int - message number
	 * @param flags
	 *            String - flags ascociated with the message
	 * @param date
	 *            String - Date of the message
	 * @param sender
	 *            String - the sender
	 * @param subject
	 *            String - the subject
	 * @param message
	 *            String - the message content
	 */
	public void addMsgToTable(String flags, String date, String sender,
			String subject, String message) {
		model.addRow(new Object[] { flags, date, sender, subject, message });
		model.fireTableDataChanged();
	}
	
	/**
	 * Adds a new message to the top of the table and removes the last one.
	 * @param num
	 *            Int - message number
	 * @param flags
	 *            String - flags ascociated with the message
	 * @param date
	 *            String - Date of the message
	 * @param sender
	 *            String - the sender
	 * @param subject
	 *            String - the subject
	 * @param message
	 *            String - the message content
	 */
	public void addMsgToTableTop(String flags, String date, String sender,
			String subject, String message) {
		model.insertRow(0,new Object[] { flags, date, sender, subject, message });
		
		model.removeRow(model.getRowCount()-1);
		
		model.fireTableDataChanged();
	}

	/**
	 * Clears the table of all entries
	 */
	public void clearTable() {

		while (model.getRowCount() > 0) {
			model.removeRow(0);
		}
		model.setNumRows(0);
		model.getDataVector().removeAllElements();
		model.fireTableDataChanged();
	}

	/**
	 * Updates the progress bar
	 * 
	 * @param currentValue
	 *            Int - the current value
	 * @param max
	 *            Int - the max value
	 */
	public void updateProgressBar(int currentValue, int max) {
		progressBar.setMaximum(max);
		progressBar.setValue(currentValue);
	}

	/**
	 * Resets the progressbar
	 */
	public void resetProgressBar() {
		progressBar.setMaximum(100);
		progressBar.setValue(0);
	}

}
