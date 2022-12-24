package org.csvator.main.gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import java.awt.Font;
import java.awt.Component;
import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class About extends JDialog {

	private static final long serialVersionUID = -955907605771944892L;
	private final JPanel contentPanel = new JPanel();

	/**
	 * Create the dialog.
	 */
	public About() {
		setTitle("About");
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
		{
			JLabel lblAboutCsvatorIde = new JLabel("About CSVator IDE");
			lblAboutCsvatorIde.setFont(new Font("Dialog", Font.BOLD, 18));
			lblAboutCsvatorIde.setAlignmentX(0.5f);
			contentPanel.add(lblAboutCsvatorIde);
		}
		{
			Component verticalStrut = Box.createVerticalStrut(20);
			contentPanel.add(verticalStrut);
		}
		{
			JTextPane txtpnCsvator = new JTextPane();
			txtpnCsvator.setText("CSVator IDE is an IDE for the language CSVator.\n\nCSVator is language to process CSV files. The name stands for CSV terminator.\n\nThis is mostly a toy project it is not meant to be used in production.\n\nLicense: Apache\n\nAuthor: Florêncio Natan\nE-mail: florencionatan@gmail.com");
			txtpnCsvator.setEditable(false);
			contentPanel.add(txtpnCsvator);
		}
		{
			JPanel buttonPane = new JPanel();
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						About.this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
						About.this.dispose();
					}
				});
				okButton.setAlignmentX(0.5f);
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
		}
	}

}
