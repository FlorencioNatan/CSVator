package org.csvator.main.gui;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PushbackReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.csvator.core.lexer.Lexer;
import org.csvator.core.lexer.LexerException;
import org.csvator.core.node.Start;
import org.csvator.core.parser.Parser;
import org.csvator.core.parser.ParserException;
import org.csvator.interpreter.Interpreter;

import javax.swing.SwingConstants;
import java.awt.Color;

public class CSVatorIDE extends JFrame {

	private static final long serialVersionUID = 209033612627417086L;
	private JPanel contentPane;
	private JTable table;
	private JTextPane textPane;
	private JTextPane textPaneOutput;
	private String filePath = "";

	private Interpreter interpreter;

	/**
	 * Create the frame.
	 */
	public CSVatorIDE() {
		setTitle("CSVator IDE");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		this.setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

		interpreter = new Interpreter();

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
				JMenu mnFile = new JMenu("File");
				mnFile.setMnemonic(KeyEvent.VK_F);
				menuBar.add(mnFile);

						JMenuItem mntmOpen = new JMenuItem("Open", KeyEvent.VK_O);
						mntmOpen.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								JFileChooser fileChooser = new JFileChooser();
								int option = fileChooser.showOpenDialog(mntmOpen);
								if (option == JFileChooser.APPROVE_OPTION) {
									File file = fileChooser.getSelectedFile();
									filePath = file.getAbsolutePath();
									Path path = Paths.get(filePath);
									StringBuilder fileContent = new StringBuilder();
									try {
										BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"));

										String line = null;
										while((line = reader.readLine()) != null){
											fileContent.append(line);
											fileContent.append("\n");
										}
									} catch (IOException e1) {
										e1.printStackTrace();
									}

									textPane.setText(fileContent.toString());
								}
							}
						});
						mnFile.add(mntmOpen);
						
						JMenuItem mntmSave = new JMenuItem("Save", KeyEvent.VK_S);
						mntmSave.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								if (filePath == null || filePath.equals("")) {
									saveAsTextFile(mntmSave);
								} else {
									saveTextFile();
								}
							}
						});
						mnFile.add(mntmSave);
						
						JMenuItem mntmSaveAs = new JMenuItem("Save As ...", KeyEvent.VK_A);
						mntmSaveAs.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								saveAsTextFile(mntmSaveAs);
							}
						});
						mnFile.add(mntmSaveAs);
						
						JMenuItem mntmExit = new JMenuItem("Exit", KeyEvent.VK_X);
						mntmExit.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								CSVatorIDE.this.setDefaultCloseOperation(CSVatorIDE.EXIT_ON_CLOSE);
								CSVatorIDE.this.dispose();
							}
						});
						mnFile.add(mntmExit);
						
						JMenu mnRun = new JMenu("Run");
						mnRun.setMnemonic(KeyEvent.VK_R);
						menuBar.add(mnRun);
						
						JMenuItem mntmRunFile = new JMenuItem("Run file", KeyEvent.VK_F);
						mntmRunFile.setHorizontalAlignment(SwingConstants.LEFT);
						mntmRunFile.addActionListener(mnRunFile());
						mnRun.add(mntmRunFile);
						
						JMenuItem mntmRunSelection = new JMenuItem("Run selection", KeyEvent.VK_S);
						mntmRunSelection.addActionListener(mnRunSelection());
						mnRun.add(mntmRunSelection);
						
						JMenuItem mntmResetEnvironment = new JMenuItem("Reset Environment", KeyEvent.VK_E);
						mntmResetEnvironment.addActionListener(mnResetEnvironment());
						mnRun.add(mntmResetEnvironment);
						
						JMenu mnHelp = new JMenu("Help");
						mnHelp.setMnemonic(KeyEvent.VK_H);
						menuBar.add(mnHelp);
						
						JMenuItem mntmAbout = new JMenuItem("About", KeyEvent.VK_A);
						mntmAbout.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								About dialog = new About();
								dialog.setModal(true);
								dialog.setLocationRelativeTo(CSVatorIDE.this);
								dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
								dialog.setVisible(true);
							}
						});
						mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));

		JToolBar toolBar = new JToolBar();
		toolBar.setAlignmentX(0.0f);
		contentPane.add(toolBar);

		JButton btnRunFile = new JButton("Run file");
		btnRunFile.addActionListener(mnRunFile());
		toolBar.add(btnRunFile);

		JButton btnRunSelection = new JButton("Run selection");
		btnRunSelection.addActionListener(mnRunSelection());
		toolBar.add(btnRunSelection);
		
		JButton btnResetEnvironment = new JButton("Reset Environment");
		btnResetEnvironment.addActionListener(mnResetEnvironment());
		toolBar.add(btnResetEnvironment);

		Component horizontalGlue = Box.createHorizontalGlue();
		toolBar.add(horizontalGlue);

		JSplitPane splitPane = new JSplitPane();
		contentPane.add(splitPane);
		splitPane.setOneTouchExpandable(true);

		JSplitPane splitPaneOutput = new JSplitPane();
		splitPaneOutput.setOrientation(JSplitPane.VERTICAL_SPLIT);
		splitPane.setRightComponent(splitPaneOutput);
		splitPaneOutput.setOneTouchExpandable(true);

		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				splitPane.setDividerLocation(0.5);
				splitPane.setResizeWeight(0.5);

				splitPaneOutput.setDividerLocation(0.7);
				splitPaneOutput.setResizeWeight(0.7);
			}
		});

		textPane = new JTextPane();
		textPane.setFont(new Font("Fira Code Retina", Font.PLAIN, 14));
		JScrollPane scrollPaneTextPane = new JScrollPane(textPane);
		splitPane.setLeftComponent(scrollPaneTextPane);

		table = new JTable();
		JScrollPane scrollPaneTable = new JScrollPane(table);
		splitPaneOutput.setLeftComponent(scrollPaneTable);

		textPaneOutput = new JTextPane();
		textPaneOutput.setEditable(false);
		textPaneOutput.setForeground(new Color(222, 221, 218));
		textPaneOutput.setBackground(new Color(0, 0, 0));
		JScrollPane scrollPaneOutput = new JScrollPane(textPaneOutput);
		splitPaneOutput.setRightComponent(scrollPaneOutput);

		TextPaneOutputStream textPaneOutputStream = new TextPaneOutputStream(textPaneOutput);
		System.setOut(new PrintStream(textPaneOutputStream));
		System.setErr(new PrintStream(textPaneOutputStream));
	}

	private ActionListener mnRunFile() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String script = textPane.getText();
				InputStream streamLine = new ByteArrayInputStream(script.getBytes(StandardCharsets.UTF_8));
				InputStreamReader reader = new InputStreamReader(streamLine);
				Lexer lexer = new Lexer(new PushbackReader(reader, 1024));
				Parser parser = new Parser(lexer);
				Start ast;
				try {
					ast = parser.parse();
					ast.apply(interpreter);
				} catch (ParserException | LexerException | IOException e1) {
					e1.printStackTrace();
				}
			}
		};
	}

	private ActionListener mnRunSelection() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String selection = textPane.getSelectedText();
				InputStream streamLine = new ByteArrayInputStream(selection.getBytes(StandardCharsets.UTF_8));
				InputStreamReader reader = new InputStreamReader(streamLine);
				Lexer lexer = new Lexer(new PushbackReader(reader, 1024));
				Parser parser = new Parser(lexer);
				Start ast;
				try {
					ast = parser.parse();
					ast.apply(interpreter);
				} catch (ParserException | LexerException | IOException e1) {
					e1.printStackTrace();
				}
			}
		};
	}

	private ActionListener mnResetEnvironment() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				interpreter = new Interpreter();
			}
		};
	}

	private void saveAsTextFile(Component parent) {
		JFileChooser fileChooser = new JFileChooser();
		int option = fileChooser.showSaveDialog(parent);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			filePath = file.getAbsolutePath();
			Path path = Paths.get(filePath);
			try {
				BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"), StandardOpenOption.CREATE);

				writer.write(textPane.getText());
				writer.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void saveTextFile() {
		Path path = Paths.get(filePath);
		try {
			BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"), StandardOpenOption.CREATE);

			writer.write(textPane.getText());
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

}

class TextPaneOutputStream extends OutputStream {

	JTextPane outputPane;
	StringBuilder content;
	List<Byte>  bytes;

	public TextPaneOutputStream(JTextPane outputPane) {
		this.outputPane = outputPane;
		this.bytes = new LinkedList<Byte>();
	}

	@Override
	public void write(int b) throws IOException {
		this.bytes.add(Byte.valueOf((byte) b));
		if ((byte) b == 10) { // The byte readed is line break;
			String cnt = new String(buildByteArray());
			this.bytes = new LinkedList<Byte>();
			try {
				Document document = (Document) outputPane.getDocument();
				document.insertString(document.getLength(), cnt, null);
			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		}
	}

	private byte[] buildByteArray() {
		byte[] byteArray = new byte[bytes.size()];
		int i = 0;
		for (byte c : bytes) {
			byteArray[i] = c;
			i++;
		}
		return byteArray;
	}
}
