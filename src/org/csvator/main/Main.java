package org.csvator.main;

import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackReader;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.csvator.core.lexer.Lexer;
import org.csvator.core.node.Start;
import org.csvator.core.parser.Parser;
import org.csvator.interpreter.Interpreter;


public class Main {

	private static final String FONT_COLOR_GREEN      = "\u001B[0;32m";
	private static final String FONT_COLOR_GREEN_BOLD = "\u001B[0;1;32m";
	private static final String FONT_COLOR_RED        = "\u001B[0;31m";
	private static final String FONT_COLOR_RED_BOLD   = "\u001B[0;1;31m";
	private static final String FONT_COLOR_DEFAULT    = "\u001B[0m";

	public static void main(String[] args) {
		if (args.length == 0) {
			Main.launchRepl();
			return;
		}
		if (args.length == 1 && !Main.shouldLaunchIDE(args) && !Main.shouldPrintHelp(args)) {
			Main.runsFile(args);
			return;
		}
		if (args.length == 2 && Main.shouldLaunchIDE(args) && !Main.shouldPrintHelp(args)) {
			Main.launchIDE(args);
			return;
		}
		Main.printHelp();
	}

	@SuppressWarnings("resource")
	private static void launchRepl() {
			Scanner in = new Scanner(System.in);
			Interpreter interpreter = new Interpreter();
			int i = 1;
			String inPrompt = "\nIn[%d]: ";
			String outPrompt = "Out[%d]: ";
			if (System.getProperty("os.name").equals("Linux")) {
				inPrompt = "\n" + FONT_COLOR_GREEN + "In [" + FONT_COLOR_GREEN_BOLD +"%d" + FONT_COLOR_GREEN + "]: " + FONT_COLOR_DEFAULT;
				outPrompt = FONT_COLOR_RED + "Out[" + FONT_COLOR_RED_BOLD + "%d" + FONT_COLOR_RED + "]: " + FONT_COLOR_DEFAULT;
			}
			while (true) {
				System.out.print(String.format(inPrompt, i));
				String line = in.nextLine();
				if (line.trim().equals("exit") || line.trim().equals("exit;") ||
					line.trim().equals("quit") || line.trim().equals("quit;")
				) {
					break;
				}
				InputStream streamLine = new ByteArrayInputStream(line.getBytes(StandardCharsets.UTF_8));
				InputStreamReader reader = new InputStreamReader(streamLine);
				Lexer lexer = new Lexer(new PushbackReader(reader, 1024));
				Parser parser = new Parser(lexer);
				try {
					Start ast = parser.parse();
					interpreter.setPromptString(String.format(outPrompt, i));
					ast.apply(interpreter);
				} catch (Exception e) {
					System.out.println(e);
				}
				i++;
			}
	}

	private static void runsFile(String[] args) {
		try {
			Lexer lexer = new Lexer(new PushbackReader(new FileReader(args[0]), 1024));
			Parser parser = new Parser(lexer);
			Start ast = parser.parse();

			Interpreter interpreter = new Interpreter();
			ast.apply(interpreter); // Para fazer o repl basta chamar o apply do próximo trecho a ser executado com mesmo interpreter. Desse jeito será possível continuar com o mesmo ambiente de antes.
		} catch (Exception e) {
			System.out.println(e);
		}
	}

	private static boolean shouldLaunchIDE(String[] args) {
		return args[0].equals("--ide") || args[0].equals("-i");
	}

	private static void launchIDE(String[] args) {

	}

	private static boolean shouldPrintHelp(String[] args) {
		for (String string : args) {
			if (string.equals("--help") || string.equals("-h")) {
				return true;
			}
		}
		return false;
	}
	private static void printHelp() {
		System.out.println("Use: csvator [options] [file]\n");
		System.out.println("The options include:\n");
		System.out.println("\t--ide -i \t Launch the CSVator IDE");
		System.out.println("\t--help -h \t Prints the help message\n");
		System.out.println("If no option or parameter is specified the repl will be lauched.");
	}
}
