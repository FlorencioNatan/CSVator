package org.csvator.main.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.undo.UndoManager;

import org.csvator.core.lexer.Lexer;
import org.csvator.core.lexer.LexerException;
import org.csvator.core.node.Start;
import org.csvator.core.parser.Parser;
import org.csvator.core.parser.ParserException;
import org.csvator.interpreter.Interpreter;
import org.csvator.interpreter.tablePrinterStrategy.JTableTablePrinter;
import org.csvator.main.gui.CSVatorCodeEditor.CodeEditor;
import javax.swing.SpringLayout;
import javax.swing.JTextField;
import javax.swing.JLabel;
import java.awt.event.KeyAdapter;

public class CSVatorIDE extends JFrame {

	private static final long serialVersionUID = 209033612627417086L;
	private JPanel contentPane;
	private JTable table;
	private CodeEditor codeEditor;
	private UndoManager undoManager = new UndoManager();
	private JTextPane textPaneOutput;
	private String filePath = "";
	private JTextField tFFind;
	private JTextField tFReplace;
	private JPanel panelEditor;
	private JPanel panelSearchReplace;
	private JPanel panelReplace;
	private Matcher matcher;
	private LinkedList<FindMatch> findMacthes;
	private ListIterator<FindMatch> findPos;
	private boolean findDirectionNext = true;

	private Interpreter interpreter;

	/**
	 * Create the frame.
	 */
	public CSVatorIDE() {
		setTitle("CSVator IDE");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 800, 600);
		ImageIcon icon = new ImageIcon("resources/images/icon.png");
		setIconImage(icon.getImage());
		this.setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
				JMenu mnFile = new JMenu("File");
				mnFile.setMnemonic(KeyEvent.VK_F);
				menuBar.add(mnFile);

						JMenuItem mntmOpen = new JMenuItem("Open", KeyEvent.VK_O);
						mntmOpen.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								JFileChooser fileChooser = new JFileChooser();
								fileChooser.setAcceptAllFileFilterUsed(false);
								fileChooser.setFileFilter(CSVatorIDE.this.getFileFiler());
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

									codeEditor.setText(fileContent.toString());
								}
							}
						});
						
						Icon iconCsvatorFile = new ImageIcon("resources/images/csvator_file_24.png");
						mntmOpen.setIcon(iconCsvatorFile);
						mntmOpen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK));
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
						mntmSave.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK));
						mnFile.add(mntmSave);
						
						JMenuItem mntmSaveAs = new JMenuItem("Save As ...", KeyEvent.VK_A);
						mntmSaveAs.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								saveAsTextFile(mntmSaveAs);
							}
						});
						mntmSaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
						mnFile.add(mntmSaveAs);
						
						JMenuItem mntmExit = new JMenuItem("Exit", KeyEvent.VK_X);
						mntmExit.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								CSVatorIDE.this.setDefaultCloseOperation(CSVatorIDE.EXIT_ON_CLOSE);
								CSVatorIDE.this.dispose();
							}
						});
						mntmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, KeyEvent.CTRL_DOWN_MASK));
						mnFile.add(mntmExit);
						
						JMenu mnEdit = new JMenu("Edit");
						mnEdit.setMnemonic(KeyEvent.VK_E);
						menuBar.add(mnEdit);
						
						JMenuItem mntmUndo = new JMenuItem("Undo");
						JMenuItem mntmRedo = new JMenuItem("Redo");
						mntmUndo.addActionListener(this.mnUndo(mntmUndo, mntmRedo));
						mntmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK));
						mntmUndo.setMnemonic(KeyEvent.VK_U);
						mntmUndo.setEnabled(false);
						mnEdit.add(mntmUndo);
						
						mntmRedo.addActionListener(this.mnRedo(mntmUndo, mntmRedo));
						mntmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z, KeyEvent.CTRL_DOWN_MASK | KeyEvent.SHIFT_DOWN_MASK));
						mntmRedo.setMnemonic(KeyEvent.VK_R);
						mntmRedo.setEnabled(false);
						mnEdit.add(mntmRedo);
						
						JSeparator separator = new JSeparator();
						mnEdit.add(separator);
						
						JMenuItem mntmSelectAll = new JMenuItem("Select All");
						mntmSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
						mntmSelectAll.setMnemonic(KeyEvent.VK_A);
						mntmSelectAll.addActionListener(this.mnSelectAll());
						mnEdit.add(mntmSelectAll);
						
						JMenuItem mntmCut = new JMenuItem("Cut");
						mntmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
						mntmCut.setMnemonic(KeyEvent.VK_C);
						mntmCut.addActionListener(this.mnCut());
						mnEdit.add(mntmCut);
						
						JMenuItem mntmCopy = new JMenuItem("Copy");
						mntmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
						mntmCopy.setMnemonic(KeyEvent.VK_Y);
						mntmCopy.addActionListener(this.mnCopy());
						mnEdit.add(mntmCopy);
						
						JMenuItem mntmPaste = new JMenuItem("Paste");
						mntmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
						mntmPaste.setMnemonic(KeyEvent.VK_P);
						mntmPaste.addActionListener(this.mnPaste());
						mnEdit.add(mntmPaste);
						
						JSeparator separator_1 = new JSeparator();
						mnEdit.add(separator_1);
						
						JMenuItem mntmComment = new JMenuItem("Toggle Comment");
						mntmComment.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, KeyEvent.CTRL_DOWN_MASK));
						mntmComment.setMnemonic(KeyEvent.VK_M);
						mntmComment.addActionListener(this.mnToggleComment());
						mnEdit.add(mntmComment);
						
						JMenu mnFind = new JMenu("Find");
						mnFind.setMnemonic(KeyEvent.VK_I);
						menuBar.add(mnFind);
						
						JMenuItem mntmFind = new JMenuItem("Find");
						mntmFind.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								panelSearchReplace.setVisible(false);
								SpringLayout sl_panelEditor = (SpringLayout) panelEditor.getLayout();
								sl_panelEditor.putConstraint(SpringLayout.NORTH, panelSearchReplace, -25, SpringLayout.SOUTH, panelEditor);
								panelReplace.setVisible(false);
								panelSearchReplace.setVisible(true);
								tFFind.requestFocus();
							}
						});
						mntmFind.setMnemonic(KeyEvent.VK_F);
						mntmFind.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, KeyEvent.CTRL_DOWN_MASK));
						mnFind.add(mntmFind);
						
						JMenuItem mntmReplace = new JMenuItem("Replace");
						mntmReplace.addActionListener(new ActionListener() {
							public void actionPerformed(ActionEvent e) {
								panelSearchReplace.setVisible(false);
								panelSearchReplace.setVisible(true);
								SpringLayout sl_panelEditor = (SpringLayout) panelEditor.getLayout();
								sl_panelEditor.putConstraint(SpringLayout.NORTH, panelSearchReplace, -50, SpringLayout.SOUTH, panelEditor);
								panelReplace.setVisible(true);
								panelSearchReplace.setVisible(true);
								tFFind.requestFocus();
							}
						});
						mntmReplace.setMnemonic(KeyEvent.VK_R);
						mntmReplace.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK));
						mnFind.add(mntmReplace);
						
						JMenu mnRun = new JMenu("Run");
						mnRun.setMnemonic(KeyEvent.VK_R);
						menuBar.add(mnRun);
						
						JMenuItem mntmRunFile = new JMenuItem("Run file", KeyEvent.VK_F);
						mntmRunFile.setHorizontalAlignment(SwingConstants.LEFT);
						mntmRunFile.addActionListener(mnRunFile());
						mntmRunFile.setAccelerator(KeyStroke.getKeyStroke("F9"));
						Icon iconRunFile24 = new ImageIcon("resources/images/run_24.png");
						mntmRunFile.setIcon(iconRunFile24);
						mnRun.add(mntmRunFile);
						
						JMenuItem mntmRunSelection = new JMenuItem("Run selection", KeyEvent.VK_S);
						mntmRunSelection.addActionListener(mnRunSelection());
						mntmRunSelection.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.CTRL_DOWN_MASK));
						Icon iconRunSelection24 = new ImageIcon("resources/images/run_selection_24.png");
						mntmRunSelection.setIcon(iconRunSelection24);
						mnRun.add(mntmRunSelection);
						
						JMenuItem mntmResetEnvironment = new JMenuItem("Reset Environment", KeyEvent.VK_E);
						mntmResetEnvironment.addActionListener(mnResetEnvironment());
						mntmResetEnvironment.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, KeyEvent.CTRL_DOWN_MASK));
						Icon iconResetEnvironment24 = new ImageIcon("resources/images/reset_environment_24.png");
						mntmResetEnvironment.setIcon(iconResetEnvironment24);
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
						mntmAbout.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK));
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
		Icon iconRunFile32 = new ImageIcon("resources/images/run_32.png");
		btnRunFile.setIcon(iconRunFile32);
		toolBar.add(btnRunFile);

		JButton btnRunSelection = new JButton("Run selection");
		btnRunSelection.addActionListener(mnRunSelection());
		Icon iconRunSelection32 = new ImageIcon("resources/images/run_selection_32.png");
		btnRunSelection.setIcon(iconRunSelection32);
		toolBar.add(btnRunSelection);
		
		JButton btnResetEnvironment = new JButton("Reset Environment");
		btnResetEnvironment.addActionListener(mnResetEnvironment());
		Icon iconResetEnvironment32 = new ImageIcon("resources/images/reset_environment_32.png");
		btnResetEnvironment.setIcon(iconResetEnvironment32);
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

		codeEditor = new CodeEditor();
		JScrollPane scrollPaneTextPane = new JScrollPane(codeEditor);
		panelEditor = new JPanel();
		panelSearchReplace = new JPanel();
		panelSearchReplace.setVisible(false);
		SpringLayout sl_panelEditor = new SpringLayout();
		sl_panelEditor.putConstraint(SpringLayout.SOUTH, scrollPaneTextPane, 0, SpringLayout.NORTH, panelSearchReplace);
		sl_panelEditor.putConstraint(SpringLayout.NORTH, scrollPaneTextPane, 0, SpringLayout.NORTH, panelEditor);
		sl_panelEditor.putConstraint(SpringLayout.WEST, panelSearchReplace, 0, SpringLayout.WEST, panelEditor);
		sl_panelEditor.putConstraint(SpringLayout.SOUTH, panelSearchReplace, 0, SpringLayout.SOUTH, panelEditor);
		sl_panelEditor.putConstraint(SpringLayout.EAST, panelSearchReplace, 0, SpringLayout.EAST, panelEditor);
		sl_panelEditor.putConstraint(SpringLayout.WEST, scrollPaneTextPane, 0, SpringLayout.WEST, panelEditor);
		sl_panelEditor.putConstraint(SpringLayout.EAST, scrollPaneTextPane, 0, SpringLayout.EAST, panelEditor);
		panelEditor.setLayout(sl_panelEditor);
		panelEditor.add(scrollPaneTextPane);
		panelEditor.add(panelSearchReplace);
		panelSearchReplace.setLayout(new BoxLayout(panelSearchReplace, BoxLayout.Y_AXIS));
		
		JPanel panelSearch = new JPanel();
		panelSearchReplace.add(panelSearch);
		SpringLayout sl_panelSearch = new SpringLayout();
		panelSearch.setLayout(sl_panelSearch);
		
		JLabel lblFind = new JLabel("Find:");
		sl_panelSearch.putConstraint(SpringLayout.NORTH, lblFind, 0, SpringLayout.NORTH, panelSearch);
		sl_panelSearch.putConstraint(SpringLayout.WEST, lblFind, 0, SpringLayout.WEST, panelSearch);
		sl_panelSearch.putConstraint(SpringLayout.SOUTH, lblFind, 0, SpringLayout.SOUTH, panelSearch);
		panelSearch.add(lblFind);
		
		tFFind = new JTextField();
		tFFind.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 27){
					panelSearchReplace.setVisible(false);
					sl_panelEditor.putConstraint(SpringLayout.NORTH, panelSearchReplace, 0, SpringLayout.SOUTH, panelEditor);
				}
			}
		});
		sl_panelSearch.putConstraint(SpringLayout.NORTH, tFFind, 0, SpringLayout.NORTH, panelSearch);
		sl_panelSearch.putConstraint(SpringLayout.WEST, tFFind, 64, SpringLayout.WEST, panelSearch);
		sl_panelSearch.putConstraint(SpringLayout.SOUTH, tFFind, 0, SpringLayout.SOUTH, panelSearch);
		panelSearch.add(tFFind);
		tFFind.setColumns(10);
		
		JButton btnFind = new JButton("Find");
		btnFind.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (matcher != null && matcher.pattern().toString().equals(tFFind.getText())) {
					nextMatch();
					return;
				}

				Pattern regex = Pattern.compile(tFFind.getText());
				matcher = regex.matcher(codeEditor.getText());
				findMacthes = new LinkedList<>();
				while(matcher.find()) {
					int start = matcher.start();
					int end = matcher.end();
					findMacthes.add(new FindMatch(start, end));
				}

				findPos = findMacthes.listIterator();
				nextMatch();
			}
		});
		sl_panelSearch.putConstraint(SpringLayout.WEST, btnFind, -197, SpringLayout.EAST, panelSearch);
		sl_panelSearch.putConstraint(SpringLayout.EAST, tFFind, 0, SpringLayout.WEST, btnFind);
		sl_panelSearch.putConstraint(SpringLayout.NORTH, btnFind, 0, SpringLayout.NORTH, panelSearch);
		sl_panelSearch.putConstraint(SpringLayout.SOUTH, btnFind, 0, SpringLayout.SOUTH, panelSearch);
		panelSearch.add(btnFind);

		JButton btnFindPrev = new JButton("Find Prev");
		btnFindPrev.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (matcher != null && matcher.pattern().toString().equals(tFFind.getText())) {
					prevMatch();
					return;
				}

				Pattern regex = Pattern.compile(tFFind.getText());
				matcher = regex.matcher(codeEditor.getText());
				findMacthes = new LinkedList<>();
				while(matcher.find()) {
					int start = matcher.start();
					int end = matcher.end();
					findMacthes.add(new FindMatch(start, end));
				}

				findPos = findMacthes.listIterator(findMacthes.size());
				prevMatch();
			}
		});
		sl_panelSearch.putConstraint(SpringLayout.WEST, btnFindPrev, -107, SpringLayout.EAST, panelSearch);
		sl_panelSearch.putConstraint(SpringLayout.EAST, btnFindPrev, 0, SpringLayout.EAST, panelSearch);
		sl_panelSearch.putConstraint(SpringLayout.EAST, btnFind, 0, SpringLayout.WEST, btnFindPrev);
		sl_panelSearch.putConstraint(SpringLayout.NORTH, btnFindPrev, 0, SpringLayout.NORTH, panelSearch);
		sl_panelSearch.putConstraint(SpringLayout.SOUTH, btnFindPrev, 0, SpringLayout.SOUTH, panelSearch);
		panelSearch.add(btnFindPrev);
		
		panelReplace = new JPanel();
		panelSearchReplace.add(panelReplace);
		SpringLayout sl_panelReplace = new SpringLayout();
		panelReplace.setLayout(sl_panelReplace);
		
		JLabel lblReplace = new JLabel("Replace:");
		sl_panelReplace.putConstraint(SpringLayout.NORTH, lblReplace, 0, SpringLayout.NORTH, panelReplace);
		sl_panelReplace.putConstraint(SpringLayout.WEST, lblReplace, 0, SpringLayout.WEST, panelReplace);
		sl_panelReplace.putConstraint(SpringLayout.SOUTH, lblReplace, 0, SpringLayout.SOUTH, panelReplace);
		panelReplace.add(lblReplace);
		
		tFReplace = new JTextField();
		tFReplace.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 27){
					panelSearchReplace.setVisible(false);
					sl_panelEditor.putConstraint(SpringLayout.NORTH, panelSearchReplace, 0, SpringLayout.SOUTH, panelEditor);
				}
			}
		});
		sl_panelReplace.putConstraint(SpringLayout.NORTH, tFReplace, 0, SpringLayout.NORTH, panelReplace);
		sl_panelReplace.putConstraint(SpringLayout.WEST, tFReplace, 64, SpringLayout.WEST, panelReplace);
		sl_panelReplace.putConstraint(SpringLayout.SOUTH, tFReplace, 0, SpringLayout.SOUTH, panelReplace);
		panelReplace.add(tFReplace);
		tFReplace.setColumns(10);
		
		JButton btnReplace = new JButton("Replace");
		btnReplace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = codeEditor.getText().replaceFirst(tFFind.getText(), tFReplace.getText());
				codeEditor.setText(text);
			}
		});
		sl_panelReplace.putConstraint(SpringLayout.NORTH, btnReplace, 0, SpringLayout.NORTH, panelReplace);
		sl_panelReplace.putConstraint(SpringLayout.SOUTH, btnReplace, 0, SpringLayout.SOUTH, panelReplace);
		sl_panelReplace.putConstraint(SpringLayout.EAST, tFReplace, 0, SpringLayout.WEST, btnReplace);
		panelReplace.add(btnReplace);
		
		JButton btnReplaceall = new JButton("ReplaceAll");
		btnReplaceall.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = codeEditor.getText().replaceAll(tFFind.getText(), tFReplace.getText());
				codeEditor.setText(text);
			}
		});
		sl_panelReplace.putConstraint(SpringLayout.NORTH, btnReplaceall, 0, SpringLayout.NORTH, panelReplace);
		sl_panelReplace.putConstraint(SpringLayout.SOUTH, btnReplaceall, 0, SpringLayout.SOUTH, panelReplace);
		sl_panelReplace.putConstraint(SpringLayout.EAST, btnReplaceall, 0, SpringLayout.EAST, panelReplace);
		sl_panelReplace.putConstraint(SpringLayout.EAST, btnReplace, 0, SpringLayout.WEST, btnReplaceall);
		panelReplace.add(btnReplaceall);

		splitPane.setLeftComponent(panelEditor);

		JPopupMenu codeEditorPopupMenu = new JPopupMenu();
		
		JMenuItem mntmPopUpSelectAll = new JMenuItem("Select All");
		mntmPopUpSelectAll.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, KeyEvent.CTRL_DOWN_MASK));
		mntmPopUpSelectAll.addActionListener(this.mnSelectAll());
		codeEditorPopupMenu.add(mntmPopUpSelectAll);
		
		JMenuItem mntmPopUpCut = new JMenuItem("Cut");
		mntmPopUpCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, KeyEvent.CTRL_DOWN_MASK));
		mntmPopUpCut.addActionListener(this.mnCut());
		codeEditorPopupMenu.add(mntmPopUpCut);
		
		JMenuItem mntmPopUpCopy = new JMenuItem("Copy");
		mntmPopUpCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK));
		mntmPopUpCopy.addActionListener(this.mnCopy());
		codeEditorPopupMenu.add(mntmPopUpCopy);
		
		JMenuItem mntmPopUpPaste = new JMenuItem("Paste");
		mntmPopUpPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, KeyEvent.CTRL_DOWN_MASK));
		mntmPopUpPaste.addActionListener(this.mnPaste());
		codeEditorPopupMenu.add(mntmPopUpPaste);
		
		JSeparator popUpSeparator = new JSeparator();
		codeEditorPopupMenu.add(popUpSeparator);
		
		JMenuItem mntmPopUpComment = new JMenuItem("Toggle Comment");
		mntmPopUpComment.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, KeyEvent.CTRL_DOWN_MASK));
		mntmPopUpComment.addActionListener(this.mnToggleComment());
		codeEditorPopupMenu.add(mntmPopUpComment);

		addPopup(codeEditor, codeEditorPopupMenu);

		codeEditor.getDocument().addUndoableEditListener(new UndoListener(mntmUndo, mntmRedo));

		table = new JTable();
		JScrollPane scrollPaneTable = new JScrollPane(table);
		splitPaneOutput.setLeftComponent(scrollPaneTable);

		textPaneOutput = new JTextPane();
		textPaneOutput.setEditable(false);
		textPaneOutput.setForeground(new Color(222, 221, 218));
		textPaneOutput.setBackground(new Color(0, 0, 0));
		JScrollPane scrollPaneOutput = new JScrollPane(textPaneOutput);
		splitPaneOutput.setRightComponent(scrollPaneOutput);

		interpreter = new Interpreter();
		interpreter.setTablePrinter(new JTableTablePrinter(table));

		TextPaneOutputStream textPaneOutputStream = new TextPaneOutputStream(textPaneOutput);
		
		System.setOut(new PrintStream(textPaneOutputStream));
		System.setErr(new PrintStream(textPaneOutputStream));
	}

	private ActionListener mnRunFile() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String script = codeEditor.getText();
				InputStream streamLine = new ByteArrayInputStream(script.getBytes(StandardCharsets.UTF_8));
				InputStreamReader reader = new InputStreamReader(streamLine);
				Lexer lexer = new Lexer(new PushbackReader(reader, 1024));
				Parser parser = new Parser(lexer);
				Start ast;

				textPaneOutput.setText("");

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
				String selection = codeEditor.getSelectedText();
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
				interpreter.setTablePrinter(new JTableTablePrinter(table));
				textPaneOutput.setText("");
				table.setModel(new DefaultTableModel());
			}
		};
	}

	private void saveAsTextFile(Component parent) {
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setSelectedFile(new File("code.csvtr"));
		fileChooser.setAcceptAllFileFilterUsed(false);
		fileChooser.setFileFilter(getFileFiler());
		int option = fileChooser.showSaveDialog(parent);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = fileChooser.getSelectedFile();
			filePath = file.getAbsolutePath();
			Path path = Paths.get(filePath);
			try {
				BufferedWriter writer = Files.newBufferedWriter(path, Charset.forName("UTF-8"), StandardOpenOption.CREATE);

				writer.write(codeEditor.getText());
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

			writer.write(codeEditor.getText());
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	private FileFilter getFileFiler() {
		return new FileNameExtensionFilter("CSVator source file", "csvtr");
	}

	private ActionListener mnUndo(JMenuItem mntmUndo, JMenuItem mntmRedo) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (undoManager.canUndo()) {
					undoManager.undo();
					mntmUndo.setEnabled(undoManager.canUndo());
					mntmRedo.setEnabled(undoManager.canRedo());
				}
			}
		};
	}

	private ActionListener mnRedo(JMenuItem mntmUndo, JMenuItem mntmRedo) {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (undoManager.canRedo()) {
					undoManager.redo();
					mntmUndo.setEnabled(undoManager.canUndo());
					mntmRedo.setEnabled(undoManager.canRedo());
				}
			}
		};
	}

	private ActionListener mnSelectAll() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				codeEditor.selectAll();
			}
		};
	}

	private ActionListener mnCut() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				codeEditor.cut();
			}
		};
	}

	private ActionListener mnCopy() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				codeEditor.copy();
			}
		};
	}

	private ActionListener mnPaste() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				codeEditor.paste();
			}
		};
	}

	private ActionListener mnToggleComment() {
		return new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String text = codeEditor.getSelectedText();
				if (text.substring(0, 3).equals("// ")) {
					text = text.substring(3);
					text = text.replaceAll("\\n// ", "\n");
					codeEditor.replaceSelection(text);
					return;
				}
				text = "// " + text;
				text = text.replaceAll("\\n", "\n// ");
				codeEditor.replaceSelection(text);
			}
		};
	}

	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}

	private void nextMatch() {
		if(!findDirectionNext) {
			findPos.next();
			findDirectionNext = true;
		}

		if (!findPos.hasNext()) {
			findPos = findMacthes.listIterator();
		}

		if (findPos.hasNext()) {
			FindMatch currentPos = findPos.next();
			codeEditor.select(currentPos.start, currentPos.end);
			codeEditor.getCaret().setSelectionVisible(true);
			codeEditor.requestFocus();
		}
	}

	private void prevMatch() {
		if(findDirectionNext) {
			findPos.previous();
			findDirectionNext = false;
		}

		if (!findPos.hasPrevious()) {
			findPos = findMacthes.listIterator(findMacthes.size());
		}

		if (findPos.hasPrevious()) {
			FindMatch currentPos = findPos.previous();
			codeEditor.select(currentPos.start, currentPos.end);
			codeEditor.getCaret().setSelectionVisible(true);
			codeEditor.requestFocus();
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

	class UndoListener implements UndoableEditListener {
		private JMenuItem mntmUndo;
		private JMenuItem mntmRedo;

		public UndoListener(JMenuItem mntmUndo, JMenuItem mntmRedo) {
			this.mntmUndo = mntmUndo;
			this.mntmRedo = mntmRedo;
		}

		public void undoableEditHappened(UndoableEditEvent e) {
			undoManager.addEdit(e.getEdit());
			this.mntmUndo.setEnabled(undoManager.canUndo());
			this.mntmRedo.setEnabled(undoManager.canRedo());
		}
	}

	class FindMatch {
		public int start;
		public int end;

		public FindMatch(int start, int end) {
			this.start = start;
			this.end = end;
		}
	}
}

