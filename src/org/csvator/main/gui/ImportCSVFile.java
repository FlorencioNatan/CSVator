package org.csvator.main.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BoxLayout;
import javax.swing.SpringLayout;
import javax.swing.JLabel;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JButton;

public class ImportCSVFile extends JFrame {

	private static final long serialVersionUID = -600224018393394497L;
	private JPanel contentPane;
	private JTextField outroSeparador;
	private JTextField outroDelimitador;
	private JTextField tfLines;
	private JTextField tfFile;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImportCSVFile frame = new ImportCSVFile();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ImportCSVFile() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 430);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.setResizable(false);
		this.setTitle("Import CSV File");

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		JPanel panelFile = new JPanel();
		contentPane.add(panelFile);
		SpringLayout sl_panelFile = new SpringLayout();
		panelFile.setLayout(sl_panelFile);
		
		JLabel lblFile = new JLabel("File:");
		sl_panelFile.putConstraint(SpringLayout.NORTH, lblFile, 10, SpringLayout.NORTH, panelFile);
		sl_panelFile.putConstraint(SpringLayout.WEST, lblFile, 0, SpringLayout.WEST, panelFile);
		panelFile.add(lblFile);
		
		tfFile = new JTextField();
		sl_panelFile.putConstraint(SpringLayout.NORTH, tfFile, 8, SpringLayout.NORTH, panelFile);
		sl_panelFile.putConstraint(SpringLayout.WEST, tfFile, 6, SpringLayout.EAST, lblFile);
		panelFile.add(tfFile);
		tfFile.setColumns(10);
		
		JButton btOpenFile = new JButton("...");
		sl_panelFile.putConstraint(SpringLayout.WEST, btOpenFile, 381, SpringLayout.WEST, panelFile);
		sl_panelFile.putConstraint(SpringLayout.EAST, tfFile, -6, SpringLayout.WEST, btOpenFile);
		sl_panelFile.putConstraint(SpringLayout.NORTH, btOpenFile, -5, SpringLayout.NORTH, lblFile);
		panelFile.add(btOpenFile);
		
		JPanel panelCharset = new JPanel();
		contentPane.add(panelCharset);
		SpringLayout sl_panelCharset = new SpringLayout();
		panelCharset.setLayout(sl_panelCharset);
		
		JLabel lblCharset = new JLabel("Charset:");
		sl_panelCharset.putConstraint(SpringLayout.NORTH, lblCharset, 8, SpringLayout.NORTH, panelCharset);
		sl_panelCharset.putConstraint(SpringLayout.WEST, lblCharset, 0, SpringLayout.WEST, panelCharset);
		panelCharset.add(lblCharset);
		
		JComboBox<String> cbCharset = new JComboBox<String>();
		sl_panelCharset.putConstraint(SpringLayout.NORTH, cbCharset, -5, SpringLayout.NORTH, lblCharset);
		sl_panelCharset.putConstraint(SpringLayout.WEST, cbCharset, 6, SpringLayout.EAST, lblCharset);
		sl_panelCharset.putConstraint(SpringLayout.EAST, cbCharset, -10, SpringLayout.EAST, panelCharset);
		cbCharset.setModel(new DefaultComboBoxModel<String>(new String[] {"US-ASCII", "ISO-8859-1", "UTF-8", "UTF-16BE", "UTF-16LE", "UTF-16"}));
		cbCharset.setSelectedIndex(2);
		panelCharset.add(cbCharset);
		
		JPanel panelSeparator = new JPanel();
		contentPane.add(panelSeparator);
		SpringLayout sl_panelSeparator = new SpringLayout();
		panelSeparator.setLayout(sl_panelSeparator);
		
		JLabel lblSeparator = new JLabel("Separator:");
		sl_panelSeparator.putConstraint(SpringLayout.NORTH, lblSeparator, 5, SpringLayout.NORTH, panelSeparator);
		sl_panelSeparator.putConstraint(SpringLayout.WEST, lblSeparator, 0, SpringLayout.WEST, panelSeparator);
		panelSeparator.add(lblSeparator);
		
		JRadioButton rdbtnTab = new JRadioButton("Tab");
		sl_panelSeparator.putConstraint(SpringLayout.NORTH, rdbtnTab, 5, SpringLayout.NORTH, panelSeparator);
		sl_panelSeparator.putConstraint(SpringLayout.WEST, rdbtnTab, 14, SpringLayout.EAST, lblSeparator);
		panelSeparator.add(rdbtnTab);
		
		JRadioButton rdbtnComma = new JRadioButton("Comma");
		sl_panelSeparator.putConstraint(SpringLayout.NORTH, rdbtnComma, 5, SpringLayout.NORTH, panelSeparator);
		sl_panelSeparator.putConstraint(SpringLayout.WEST, rdbtnComma, 11, SpringLayout.EAST, rdbtnTab);
		panelSeparator.add(rdbtnComma);
		
		JRadioButton rdbtnSemicolon = new JRadioButton("Semicolon");
		sl_panelSeparator.putConstraint(SpringLayout.NORTH, rdbtnSemicolon, 5, SpringLayout.NORTH, panelSeparator);
		sl_panelSeparator.putConstraint(SpringLayout.WEST, rdbtnSemicolon, 6, SpringLayout.EAST, rdbtnComma);
		panelSeparator.add(rdbtnSemicolon);
		
		JRadioButton rdbtnSpace = new JRadioButton("Space");
		sl_panelSeparator.putConstraint(SpringLayout.NORTH, rdbtnSpace, 5, SpringLayout.NORTH, panelSeparator);
		sl_panelSeparator.putConstraint(SpringLayout.WEST, rdbtnSpace, 8, SpringLayout.EAST, rdbtnSemicolon);
		panelSeparator.add(rdbtnSpace);
		
		JRadioButton rdbtnOther = new JRadioButton("Other");
		sl_panelSeparator.putConstraint(SpringLayout.NORTH, rdbtnOther, 25, SpringLayout.NORTH, panelSeparator);
		sl_panelSeparator.putConstraint(SpringLayout.WEST, rdbtnOther, 92, SpringLayout.WEST, panelSeparator);
		panelSeparator.add(rdbtnOther);
		
		outroSeparador = new JTextField();
		sl_panelSeparator.putConstraint(SpringLayout.NORTH, outroSeparador, 30, SpringLayout.NORTH, panelSeparator);
		sl_panelSeparator.putConstraint(SpringLayout.WEST, outroSeparador, 170, SpringLayout.WEST, panelSeparator);
		panelSeparator.add(outroSeparador);
		outroSeparador.setColumns(10);
		
		JPanel panelDelimiter = new JPanel();
		contentPane.add(panelDelimiter);
		SpringLayout sl_panelDelimiter = new SpringLayout();
		panelDelimiter.setLayout(sl_panelDelimiter);
		
		JLabel lblDelimiter = new JLabel("Delimiter:");
		sl_panelDelimiter.putConstraint(SpringLayout.NORTH, lblDelimiter, 5, SpringLayout.NORTH, panelDelimiter);
		sl_panelDelimiter.putConstraint(SpringLayout.WEST, lblDelimiter, 0, SpringLayout.WEST, panelDelimiter);
		panelDelimiter.add(lblDelimiter);
		
		JRadioButton rdbtnDoubleQuotes = new JRadioButton("\"");
		sl_panelDelimiter.putConstraint(SpringLayout.NORTH, rdbtnDoubleQuotes, 5, SpringLayout.NORTH, panelDelimiter);
		sl_panelDelimiter.putConstraint(SpringLayout.WEST, rdbtnDoubleQuotes, 14, SpringLayout.EAST, lblDelimiter);
		panelDelimiter.add(rdbtnDoubleQuotes);
		
		JRadioButton rdbtnSingleQuote = new JRadioButton("'");
		sl_panelDelimiter.putConstraint(SpringLayout.NORTH, rdbtnSingleQuote, 5, SpringLayout.NORTH, panelDelimiter);
		sl_panelDelimiter.putConstraint(SpringLayout.WEST, rdbtnSingleQuote, 11, SpringLayout.EAST, rdbtnDoubleQuotes);
		panelDelimiter.add(rdbtnSingleQuote);
		
		JRadioButton rdbtnOtherDel = new JRadioButton("Other");
		sl_panelDelimiter.putConstraint(SpringLayout.NORTH, rdbtnOtherDel, 5, SpringLayout.NORTH, panelDelimiter);
		sl_panelDelimiter.putConstraint(SpringLayout.WEST, rdbtnOtherDel, 10, SpringLayout.EAST, rdbtnSingleQuote);
		panelDelimiter.add(rdbtnOtherDel);

		outroDelimitador = new JTextField();
		sl_panelDelimiter.putConstraint(SpringLayout.NORTH, outroDelimitador, 5, SpringLayout.NORTH, panelDelimiter);
		sl_panelDelimiter.putConstraint(SpringLayout.WEST, outroDelimitador, 10, SpringLayout.EAST, rdbtnOtherDel);
		panelDelimiter.add(outroDelimitador);
		outroDelimitador.setColumns(10);
		
		JPanel panelHeaderInference = new JPanel();
		contentPane.add(panelHeaderInference);
		SpringLayout sl_panelHeaderInference = new SpringLayout();
		panelHeaderInference.setLayout(sl_panelHeaderInference);
		
		JLabel lblHeaderInferenceSettings = new JLabel("Header inference Settings");
		sl_panelHeaderInference.putConstraint(SpringLayout.NORTH, lblHeaderInferenceSettings, 5, SpringLayout.NORTH, panelHeaderInference);
		sl_panelHeaderInference.putConstraint(SpringLayout.WEST, lblHeaderInferenceSettings, 0, SpringLayout.WEST, panelHeaderInference);
		panelHeaderInference.add(lblHeaderInferenceSettings);
		
		JLabel lblIgnoreFirstLine = new JLabel("Ignore first line:");
		sl_panelHeaderInference.putConstraint(SpringLayout.NORTH, lblIgnoreFirstLine, 6, SpringLayout.SOUTH, lblHeaderInferenceSettings);
		sl_panelHeaderInference.putConstraint(SpringLayout.WEST, lblIgnoreFirstLine, 0, SpringLayout.WEST, panelHeaderInference);
		panelHeaderInference.add(lblIgnoreFirstLine);
		
		JComboBox<String> cBIgnoreFirstLine = new JComboBox<>();
		sl_panelHeaderInference.putConstraint(SpringLayout.NORTH, cBIgnoreFirstLine, -2, SpringLayout.NORTH, lblIgnoreFirstLine);
		sl_panelHeaderInference.putConstraint(SpringLayout.WEST, cBIgnoreFirstLine, 10, SpringLayout.EAST, lblIgnoreFirstLine);
		cBIgnoreFirstLine.setModel(new DefaultComboBoxModel<String>(new String[] {"Yes", "No"}));
		panelHeaderInference.add(cBIgnoreFirstLine);
		
		JRadioButton rdbtnUseWholeFile = new JRadioButton("Use whole file");
		sl_panelHeaderInference.putConstraint(SpringLayout.NORTH, rdbtnUseWholeFile, 4, SpringLayout.SOUTH, lblHeaderInferenceSettings);
		sl_panelHeaderInference.putConstraint(SpringLayout.WEST, rdbtnUseWholeFile, 25, SpringLayout.EAST, cBIgnoreFirstLine);
		panelHeaderInference.add(rdbtnUseWholeFile);
		
		JRadioButton rdbtnUseFirstNLines = new JRadioButton("use first n lines");
		sl_panelHeaderInference.putConstraint(SpringLayout.NORTH, rdbtnUseFirstNLines, 0, SpringLayout.SOUTH, rdbtnUseWholeFile);
		sl_panelHeaderInference.putConstraint(SpringLayout.WEST, rdbtnUseFirstNLines, 0, SpringLayout.WEST, rdbtnUseWholeFile);
		panelHeaderInference.add(rdbtnUseFirstNLines);
		
		tfLines = new JTextField();
		sl_panelHeaderInference.putConstraint(SpringLayout.WEST, tfLines, 6, SpringLayout.EAST, rdbtnUseFirstNLines);
		sl_panelHeaderInference.putConstraint(SpringLayout.SOUTH, tfLines, 0, SpringLayout.SOUTH, panelHeaderInference);
		sl_panelHeaderInference.putConstraint(SpringLayout.EAST, tfLines, 93, SpringLayout.EAST, rdbtnUseFirstNLines);
		panelHeaderInference.add(tfLines);
		tfLines.setColumns(10);
		
		JPanel panelImport = new JPanel();
		contentPane.add(panelImport);
		SpringLayout sl_panelImport = new SpringLayout();
		panelImport.setLayout(sl_panelImport);
		
		JButton btnImport = new JButton("Import");
		panelImport.add(btnImport);
		
		JButton btnCancel = new JButton("Cancel");
		sl_panelImport.putConstraint(SpringLayout.SOUTH, btnCancel, -10, SpringLayout.SOUTH, panelImport);
		sl_panelImport.putConstraint(SpringLayout.NORTH, btnImport, 0, SpringLayout.NORTH, btnCancel);
		sl_panelImport.putConstraint(SpringLayout.EAST, btnImport, -19, SpringLayout.WEST, btnCancel);
		sl_panelImport.putConstraint(SpringLayout.EAST, btnCancel, -10, SpringLayout.EAST, panelImport);
		panelImport.add(btnCancel);
	}
}
