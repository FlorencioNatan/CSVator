package org.csvator.main.gui;

import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextPane;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JScrollPane;
import javax.swing.SpringLayout;

public class About extends JDialog {

	private static final long serialVersionUID = -955907605771944892L;
	private final JPanel contentPanel = new JPanel();
	private JLabel lblImage;

	/**
	 * Create the dialog.
	 */
	public About() {
		setTitle("About");
		setBounds(100, 100, 500, 460);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		SpringLayout sl_contentPanel = new SpringLayout();
		contentPanel.setLayout(sl_contentPanel);
		{
			JLabel lblAboutCsvatorIde = new JLabel("About CSVator IDE");
			sl_contentPanel.putConstraint(SpringLayout.NORTH, lblAboutCsvatorIde, 0, SpringLayout.NORTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.WEST, lblAboutCsvatorIde, 0, SpringLayout.WEST, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.EAST, lblAboutCsvatorIde, 0, SpringLayout.EAST, contentPanel);
			lblAboutCsvatorIde.setHorizontalAlignment(SwingConstants.CENTER);
			lblAboutCsvatorIde.setFont(new Font("Dialog", Font.BOLD, 18));
			contentPanel.add(lblAboutCsvatorIde);
		}
		{
			lblImage = new JLabel("");
			sl_contentPanel.putConstraint(SpringLayout.NORTH, lblImage, 0, SpringLayout.NORTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.WEST, lblImage, 0, SpringLayout.WEST, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.EAST, lblImage, 0, SpringLayout.EAST, contentPanel);
			lblImage.setHorizontalAlignment(SwingConstants.CENTER);
			Icon image = new ImageIcon("resources/images/icon_196.png");
			lblImage.setIcon(image);
			contentPanel.add(lblImage);
		}
		{
			JTextPane txtpnCsvator = new JTextPane();
			txtpnCsvator.setContentType("text/html");
			txtpnCsvator.setText("<html>\n<body>\n<p>CSVator IDE is an IDE for the language CSVator.</p>\n\n<p>CSVator is language to process CSV files. The name stands for CSV terminator.</p>\n\n<p>This is mostly a <u>toy project</u> it <b>is not meant to be used in production</b>.</p>\n\n<p>The Atlantic puffin used in the icons were hand drawn and colored using GIMP. The were drawn using as reference the image <a href=\"https://en.wikipedia.org/wiki/File:Puffin_(Fratercula_arctica).jpg\">https://en.wikipedia.org/wiki/File:Puffin_(Fratercula_arctica).jpg</a>. And therefore are shared in the same license.</p>\n\n<p>The broom is in public domain, you can acces at the link: <a href=\"https://commons.wikimedia.org/wiki/File:Edit-clear.svg\">https://commons.wikimedia.org/wiki/File:Edit-clear.svg</a>.</p>\n\n<p>License: Apache</p>\n\n<p>Author: Florêncio Natan<br/>\nE-mail: florencionatan@gmail.com<p>\n</body>\n</html>");
			txtpnCsvator.setEditable(false);

			JScrollPane scrollPane = new JScrollPane(txtpnCsvator);
			sl_contentPanel.putConstraint(SpringLayout.NORTH, scrollPane, 0, SpringLayout.SOUTH, lblImage);
			sl_contentPanel.putConstraint(SpringLayout.WEST, scrollPane, 0, SpringLayout.WEST, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.SOUTH, scrollPane, 0, SpringLayout.SOUTH, contentPanel);
			sl_contentPanel.putConstraint(SpringLayout.EAST, scrollPane, 0, SpringLayout.EAST, contentPanel);
			contentPanel.add(scrollPane);
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					scrollPane.getVerticalScrollBar().setValue(0);
				}
			});
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
