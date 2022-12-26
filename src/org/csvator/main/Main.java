package org.csvator.main;

import java.awt.EventQueue;
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
import org.csvator.interpreter.tablePrinterStrategy.SimpleCSVTablePrinter;
import org.csvator.main.gui.CSVatorIDE;


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
		if (Main.shouldPrintArt(args)) {
			Main.printSeaParrot();
			return;
		}
		if (args.length == 1 && !Main.shouldLaunchIDE(args) && !Main.shouldPrintHelp(args)) {
			Main.runsFile(args);
			return;
		}
		if (args.length >= 1 && Main.shouldLaunchIDE(args) && !Main.shouldPrintHelp(args)) {
			Main.launchIDE(args);
			return;
		}
		Main.printHelp();
	}

	@SuppressWarnings("resource")
	private static void launchRepl() {
			Scanner in = new Scanner(System.in);
			Interpreter interpreter = new Interpreter();
			interpreter.setTablePrinter(new SimpleCSVTablePrinter());

			String inPrompt = "\nIn[%d]: ";
			String outPrompt = "Out[%d]: ";
			if (System.getProperty("os.name").equals("Linux")) {
				inPrompt = "\n" + FONT_COLOR_GREEN + "In [" + FONT_COLOR_GREEN_BOLD +"%d" + FONT_COLOR_GREEN + "]: " + FONT_COLOR_DEFAULT;
				outPrompt = FONT_COLOR_RED + "Out[" + FONT_COLOR_RED_BOLD + "%d" + FONT_COLOR_RED + "]: " + FONT_COLOR_DEFAULT;
			}

			int i = 1;
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
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CSVatorIDE frame = new CSVatorIDE();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static boolean shouldPrintHelp(String[] args) {
		for (String string : args) {
			if (string.equals("--help") || string.equals("-h")) {
				return true;
			}
		}
		return false;
	}

	private static boolean shouldPrintArt(String[] args) {
		for (String string : args) {
			if (string.equals("--art")) {
				return true;
			}
		}
		return false;
	}

	private static void printHelp() {
		System.out.println("Use: csvator [options] [file]\n");
		System.out.println("The options include:\n");
		System.out.println("\t--ide -i \t Launch the CSVator IDE");
		System.out.println("\t--help -h \t Prints the help message");
		System.out.println("\t--art \t\t Prints the icon on terminal (Works only with ANSI escape code)\n");
		System.out.println("If no option or parameter is specified the repl will be lauched.");
	}

	private static void printSeaParrot() {
		System.out.println("[0;37;40m                                                                        [0m\n"
				+ "[0;37;40m                                                                        [0m\n"
				+ "[0;37;40m                                                                        [0m\n"
				+ "[0;37;40m                                                                        [0m\n"
				+ "[0;37;40m                                                                        [0m\n"
				+ "[0;37;40m                                                                        [0m\n"
				+ "[0;37;40m                                                                        [0m\n"
				+ "[0;37;40m                     [0;5;30;40m8[0;5;33;40m%[0;5;34;40mS[0;5;33;40mX[0;5;35;40mt[0;5;32;40mS[0;5;35;40m%[0;5;30;40m@[0;37;40m                                           [0m\n"
				+ "[0;37;40m                 [0;5;33;40m. ..[0;5;35;40m;[0;5;32;40m%[0;5;35;40m%[0;5;32;40mS[0;5;35;40mt[0;5;33;40mt[0;5;36;40mt[0;5;33;40m;[0;5;35;40m.[0;5;33;40m..:[0;5;36;40m;[0;5;31;40mX[0;1;30;40m8[0;37;40m                                    [0m\n"
				+ "[0;37;40m              [0;5;33;40m;[0;5;36;40m:[0;5;33;40m;[0;5;35;40m;[0;5;33;40m;[0;5;36;40mt[0;5;33;40mS[0;5;34;40mS[0;5;33;40mX[0;5;34;40mS[0;5;33;40m%[0;5;36;40mS[0;5;31;40mS[0;5;36;40m%[0;5;31;40m%[0;5;36;40mt[0;5;33;40mt[0;5;34;40m%[0;5;33;40mS[0;5;34;40mS[0;5;33;40m%[0;5;36;40m%[0;5;33;40mt[0;5;35;40m;[0;5;33;40m:[0;5;36;40m%[0;5;30;40m8[0;37;40m                               [0m\n"
				+ "[0;37;40m           [0;5;33;40m:[0;5;36;40m :[0;5;35;40m%[0;5;32;40mS[0;5;35;40m%[0;5;32;40mS[0;5;35;40m%[0;5;33;40mS[0;5;34;40mX[0;5;33;40mX[0;5;34;40mS[0;5;33;40mX[0;5;35;40mS[0;5;33;40mt[0;5;36;40m [0;5;37;40m@[0;1;30;47m8[0;5;37;40m8[0;1;30;47m8[0;5;37;40m8@[0;5;33;40m  :[0;5;35;40mt[0;5;32;40mS[0;5;35;40mS[0;5;33;40m@[0;5;35;40m%[0;5;36;40m;[0;5;33;40m;[0;5;30;40m@[0;37;40m                            [0m\n"
				+ "[0;37;40m         [0;5;33;40m:[0;5;36;40m.[0;5;35;40m:[0;5;33;40m%[0;5;36;40m%[0;5;31;40mX[0;5;36;40mS[0;5;31;40mX[0;5;36;40m%[0;5;33;40mS[0;5;34;40mS[0;5;33;40mS[0;5;35;40m%[0;5;33;40mt[0;5;36;40m.[0;5;37;40m8[0;1;30;47m:.::.;;;.tS[0;5;37;40m@[0;5;33;40m [0;5;36;40m:%[0;5;33;40mX[0;5;30;40mX[0;5;35;40m:[0;5;33;40m;[0;5;36;40m:[0;5;31;40m%[0;37;40m                          [0m\n"
				+ "[0;37;40m       [0;5;30;40m@[0;5;33;40m.[0;5;35;40m:[0;5;33;40m%[0;5;34;40mS[0;5;31;40mS[0;5;32;40mS[0;5;35;40mS[0;5;32;40mS[0;5;35;40m;[0;5;33;40m;[0;5;35;40mt[0;5;33;40mt[0;5;36;40m:[0;5;37;40m8[0;1;30;47m;.:;;:;t;;;;;;::X[0;5;37;40mS[0;5;35;40m.[0;5;33;40m.[0;5;36;40mt[0;5;31;40mS[0;5;36;40mS[0;5;33;40mS[0;5;35;40m;[0;5;30;40m@[0;37;40m                        [0m\n"
				+ "[0;37;40m      [0;5;33;40m.:[0;5;36;40m;[0;5;33;40m%[0;5;34;40mX[0;5;33;40m@[0;5;34;40mS[0;5;33;40m%[0;5;36;40mt[0;5;35;40m;[0;5;33;40m:[0;5;36;40m [0;1;30;47m8%.:::::..t[0;5;33;40m [0;1;30;47m@:;;:;;;::t[0;5;37;40m8[0;5;33;40m.[0;5;36;40m:[0;5;31;40mS[0;5;36;40mX[0;5;33;40m%[0;5;36;40m;[0;5;35;40mt[0;37;40m                       [0m\n"
				+ "[0;37;40m    [0;5;30;40m@[0;5;33;40m.[0;5;36;40m;[0;5;35;40mt[0;5;33;40mt[0;5;36;40mt[0;5;33;40m%[0;5;36;40mt[0;5;33;40m:[0;5;35;40m [0;5;37;40m8[0;1;30;47mX:[0;1;37;47m.[0;1;30;47m.:....:..;[0;5;37;40m8[0;5;33;40mS.[0;1;30;47mt;;;;;;:;::%[0;5;37;40mX[0;5;33;40m.[0;5;35;40m%[0;5;32;40mS[0;5;35;40m%[0;5;33;40mS[0;5;36;40mt[0;37;40m                      [0m\n"
				+ "[0;37;40m    [0;5;33;40m   [0;5;37;40mX@[0;1;30;47m8X%.[0;1;37;47m.[0;1;30;47m . ...:.....tX[0;5;37;40m8[0;5;33;40m.:[0;1;30;47m8;;;;;;;;;:::@[0;5;33;40m .t[0;5;34;40mX[0;5;33;40mS[0;5;35;40mt[0;37;40m                     [0m\n"
				+ "[0;37;40m   [0;5;37;40m8[0;1;37;47m..[0;1;30;47m . ........... .  .S[0;5;37;40m8[0;1;30;47mS88[0;5;37;40mX[0;1;30;47mX:;;;;;;;;;:...[0;5;37;40mX[0;5;33;40m:;[0;5;36;40m%[0;5;33;40m%[0;5;34;40mS[0;37;40m                    [0m\n"
				+ "[0;37;40m  [0;5;33;40m [0;1;30;47m8........:t;;::.     ;XX[0;5;37;40m8[0;5;33;40m [0;1;30;47m%[0;5;37;40m8[0;1;30;47m;..::::::::.. .:[0;5;37;40mS[0;5;33;40m:[0;5;35;40mt[0;5;33;40mt%[0;1;30;40m8[0;37;40m                   [0m\n"
				+ "[0;37;40m [0;5;33;40m;[0;5;36;40m [0;5;37;40mX[0;1;30;47m:::::::...:%SXXS%%t8[0;5;33;40m [0;5;37;40m8[0;5;33;40m [0;5;37;40m888[0;1;30;47mS:. ........  ...;[0;5;33;40m :[0;5;36;40mt[0;5;33;40m [0;1;30;47m@[0;1;33;47m@S[0;5;33;40m;[0;37;40m                [0m\n"
				+ "[0;37;40m [0;5;33;40m   [0;1;30;47m ;;:::::::..   . . :%XS[0;5;37;40m888[0;1;30;47m%    . . . ...... X[0;5;33;40m;[0;5;37;40m8[0;1;37;47m8[0;1;33;47m8[0;5;37;43m:[0;5;37;47m.[0;5;37;43m8[0;5;35;40mS[0;37;40m               [0m\n"
				+ "[0;5;30;40m@[0;5;33;40m   [0;1;30;47m :;t;t;;:::... .. . .         . .. . ..    ..[0;5;33;40m [0;1;33;47mXS[0;1;30;47m%[0;1;37;47m8[0;5;37;43mS[0;1;33;47mX[0;5;33;41m;[0;5;36;40m [0;37;40m              [0m\n"
				+ "[0;5;30;40m8[0;5;33;40m   [0;1;30;47m%;;;;tt;t;::..  .....      ... ......  .      [0;1;33;47mS[0;1;37;47m8[0;1;30;43m8[0;5;33;40m [0;1;33;47mS[0;5;1;33;47m8[0;1;33;47mX[0;5;33;41m [0;5;37;40m8[0;37;40m             [0m\n"
				+ "[0;5;30;40m8[0;5;33;40m.: [0;5;37;40m8[0;1;30;47m:;;;;:;;;;::..............  ... ...         [0;1;33;47m@[0;1;37;47m8[0;37;43m8[0;5;37;40m88[0;33;47m8[0;5;37;43mS[0;5;37;47m8[0;5;1;33;41m8[0;5;37;41m8[0;33;47m8[0;5;31;40mS[0;37;40m           [0m\n"
				+ "[0;5;30;40m@[0;5;33;40m .:.[0;1;30;47m%:;;;:;;;;;:........:.... ... ..   .       :[0;1;33;47m@@[0;5;37;40m8[0;5;33;40m [0;5;37;40m8X[0;1;33;47mS[0;5;37;43mS[0;1;37;47m [0;5;33;41m [0;5;37;41m8[0;33;47m8[0;5;35;40m:[0;37;40m          [0m\n"
				+ "[0;37;40m [0;5;33;40m.[0;5;36;40m:[0;5;33;40m:.[0;1;30;47m8.:;;:;;;;:.:.:.::::.:.......              [0;1;33;47m@[0;1;37;47m8[0;5;33;40m;[0;5;37;40m8[0;5;33;40m [0;5;37;40m8[0;5;33;40m [0;1;30;47m.[0;5;37;43mS[0;1;37;47m;[0;5;1;31;43m8[0;5;37;41m8[0;5;1;33;41mX[0;5;37;41m8[0;1;30;47m8[0;37;40m         [0m\n"
				+ "[0;37;40m [0;5;33;40m.;[0;5;35;40m:[0;5;33;40m. [0;1;30;47m ::;;;;;;::.::::::::......               [0;1;33;47mXX[0;5;37;40m88[0;5;33;40m [0;1;30;47mX[0;5;33;40m [0;5;37;40m88[0;5;37;43mS[0;5;37;47m8[0;5;1;33;41m8[0;1;31;47m%[0;5;37;41m8[0;5;1;33;41m8[0;5;37;41m8[0;33;47m8[0;5;31;40mX[0;37;40m       [0m\n"
				+ "[0;37;40m [0;5;33;40m.[0;5;36;40m;[0;5;33;40m;[0;5;36;40m.[0;5;33;40m:[0;5;37;40m@[0;1;30;47m:;;;;;;:::::::;::.:....                [0;1;33;47m8[0;1;30;47m;[0;5;33;40m [0;5;37;40m8[0;5;33;40m [0;1;30;47mX[0;5;33;40m.[0;5;37;40m8[0;5;33;40m [0;1;30;47m8[0;5;37;43m@[0;5;1;37;43m8[0;1;31;47mX[0;5;1;31;43m8[0;1;37;47m [0;5;1;33;41m8[0;1;35;47mS[0;5;1;33;41m@[0;1;30;47m8[0;5;31;40m8[0;37;40m      [0m\n"
				+ "[0;37;40m [0;5;33;40m .[0;5;35;40m.[0;5;33;40m:.:[0;5;37;40m8[0;1;30;47m;::::::;:;:::::....       .[0;33;47m@[0;1;33;47m88[0;33;47m8[0;1;33;47mS[0;1;30;47m    [0;33;47mX[0;1;33;47mX[0;1;30;47m8[0;5;33;40m.[0;5;37;40m8[0;5;33;40m [0;1;30;47mS[0;5;33;40m.[0;5;37;40m888@[0;1;37;47m8[0;5;37;43mS[0;1;33;47mX[0;5;1;35;41m@[0;5;37;43m8[0;5;1;35;41m8[0;5;37;43m8[0;5;37;41m8[0;5;1;33;41m8[0;1;30;47m8[0;37;40m      [0m\n"
				+ "[0;37;40m [0;5;33;40m:[0;5;36;40m [0;5;33;40m.[0;5;36;40m.[0;5;33;40m...[0;1;30;47mS:...:::::::::...    .  .[0;1;33;47mX[0;5;37;43m@X@[0;33;47mX[0;5;37;43mX[0;1;33;47m8[0;1;30;47m  [0;33;47mS[0;1;33;47mS[0;1;30;47m [0;5;33;40m [0;5;37;40m888[0;5;33;40m [0;5;37;40m88[0;5;33;40m [0;5;37;40m8[0;5;33;40m [0;1;30;43m8[0;5;1;37;43m8[0;5;37;47m8[0;5;1;31;43m8[0;1;33;47m@[0;5;35;41m [0;1;33;47mX[0;5;37;41m8[0;5;1;33;41m8[0;1;31;47m@[0;5;1;33;41mS[0;1;30;47m8[0;37;40m     [0m\n"
				+ "[0;37;40m  [0;5;33;40m  .... [0;1;30;47m:  . ......:..         [0;33;47m8[0;5;37;43m8[0;33;47mXX[0;1;33;47m8@X8[0;33;47mX[0;1;33;47mS[0;1;30;47m [0;5;33;40m [0;5;37;40m8[0;5;33;40m [0;5;37;40m8[0;5;33;40m [0;5;37;40m88[0;5;33;40m.[0;5;37;40m8[0;5;33;40m [0;5;37;40m88[0;1;30;47m@[0;5;37;43mX[0;5;1;37;43m8[0;1;33;47m@[0;5;1;35;41m@[0;33;47m8[0;5;33;41m [0;1;31;47m@[0;5;37;41m8[0;5;1;33;41m@[0;5;1;35;41m8[0;5;1;33;41m8[0;1;30;47m8[0;37;40m    [0m\n"
				+ "[0;37;40m  [0;5;33;40m ..[0;5;36;40m.[0;5;33;40m.  .[0;1;30;47m%      ... .          [0;33;47m8[0;1;33;47m8[0;5;37;43m@X[0;1;33;47m@[0;5;37;43m8[0;1;33;47m@[0;33;47m8[0;1;30;47m:[0;5;33;40m%S [0;5;37;40m8[0;5;33;40m [0;5;37;40m8[0;5;33;40m. [0;5;37;40m8[0;5;33;40m [0;5;37;40m8[0;5;33;40m [0;5;37;40m8[0;5;33;40m [0;33;47mX[0;5;37;47mt[0;5;37;43m@[0;5;1;31;43m8[0;1;31;47mX[0;5;33;41m%[0;1;30;47m8[0;5;1;33;41mX[0;5;37;41m8[0;5;1;33;41m8[0;1;31;47m@[0;5;37;41m8[0;33;47m@[0;5;35;40m;[0;37;40m   [0m\n"
				+ "[0;37;40m  [0;5;33;40m:[0;5;36;40m.[0;5;33;40m:.    : [0;1;30;47mt:       .       .:..;:. .X[0;5;33;40m [0;5;37;40m88[0;5;33;40m [0;5;37;40m8[0;5;33;40m [0;5;37;40m8[0;5;33;40m.[0;5;37;40m88[0;5;33;40m.[0;5;37;40m8[0;5;33;40m [0;1;30;47m@[0;5;33;40m.[0;5;37;40m8[0;5;37;43m8X[0;1;37;47m@[0;5;1;33;41m8[0;5;37;41m8[0;5;33;40mt[0;5;33;41m.[0;1;31;47m8[0;5;1;33;41m@[0;5;1;35;41m8[0;5;1;33;41m8[0;5;37;41m8[0;5;1;33;41mX[0;1;30;47m@[0;37;40m   [0m\n"
				+ "[0;37;40m   [0;5;33;40m.[0;5;36;40m.[0;5;33;40m:      ;. [0;1;30;47mtt.         .:;ttt;; [0;33;47m8[0;5;33;40m.[0;5;37;40m8[0;5;33;40m [0;5;37;40m8[0;5;33;40m [0;1;30;47m@[0;5;33;40m [0;1;30;47m@[0;5;33;40m [0;5;37;40m8[0;5;33;40m  [0;5;37;40m8[0;5;33;40m:[0;5;37;40m8[0;5;33;40m [0;1;30;47m@t[0;5;37;43mS[0;5;37;47m%[0;5;1;31;43m8[0;5;37;41m88[0;5;33;40mS[0;5;37;41m8[0;5;1;33;41m@[0;5;37;41m8[0;5;1;31;43m8[0;5;1;35;41m8[0;5;1;33;41m8[0;5;37;41m8[0;1;31;47m@[0;5;33;40m;[0;37;40m  [0m\n"
				+ "[0;37;40m   [0;5;33;40m:[0;5;35;40m:[0;5;36;40m.[0;5;33;40m.        ;;: [0;1;30;47mtt;   .::;;;;;;[0;1;33;47mXS[0;1;30;47mt[0;5;33;40m [0;5;37;40m88[0;5;33;40m [0;1;30;47mX[0;5;33;40m [0;1;30;47m@[0;5;33;40m.[0;1;30;47m8[0;5;33;40m [0;1;30;47mX[0;5;33;40m [0;5;37;40m8[0;5;33;40m [0;5;37;40m8[0;5;33;40m .[0;1;33;47m@@[0;5;37;43m8[0;5;37;41m88[0;5;33;40m@[0;5;31;41m:[0;5;37;41m88[0;5;35;41m [0;5;37;41m8[0;5;1;33;41mS[0;5;1;35;41mS[0;5;1;33;41mS[0;5;31;41m [0;1;30;47m@[0;37;40m  [0m\n"
				+ "[0;37;40m    [0;5;33;40m .    [0;5;37;40m8[0;5;33;40m:     [0;5;37;40m8[0;5;33;40m;:;;: [0;1;30;47m%St;:::;t [0;1;33;47mSS[0;5;37;43m;[0;1;30;47m;[0;5;33;40m [0;5;37;40m8[0;5;33;40m [0;5;37;40m88[0;5;33;40m [0;1;30;47m@[0;5;33;40m [0;1;30;47m@[0;5;33;40m.[0;1;30;47m@[0;5;33;40m [0;1;30;47m@[0;5;33;40m [0;1;30;47m:[0;33;47m8[0;5;37;43mt[0;1;37;47m [0;1;30;43m8[0;5;35;41m [0;5;33;40m.[0;1;31;43m8[0;5;37;41m8[0;5;31;41m [0;5;33;41m [0;1;30;43m8[0;5;31;41m [0;5;33;41m [0;5;1;33;41mX[0;5;35;41m [0;5;37;41m8[0;1;30;43m8[0;5;35;40m8[0;37;40m [0m\n"
				+ "[0;37;40m    [0;5;33;40m .       [0;5;37;40m8[0;5;33;40m.    [0;5;37;40m8[0;5;33;40m. [0;5;37;40m8[0;5;33;40mtt;;:[0;5;37;40m8[0;1;30;47mX;[0;5;33;40mS[0;37;40m [0;5;33;40m:[0;1;37;47m88[0;5;37;43m:[0;5;37;47mt[0;5;37;43m8[0;33;47m@[0;1;30;47mS[0;5;33;40m:[0;5;37;40m8[0;5;33;40m [0;1;30;47m@[0;5;33;40m [0;5;37;40m88[0;5;33;40m.[0;33;47m@8[0;1;33;47m8[0;5;37;43m:[0;5;37;47m;[0;1;33;47mX[0;1;31;47m8[0;5;37;41m8[0;5;33;40m:[0;5;31;41m8[0;5;35;41m8[0;1;30;43m8[0;5;31;41m@[0;5;33;40m [0;5;31;41m [0;5;37;41m8[0;5;1;33;41m%[0;5;1;35;41mX[0;5;33;41m   [0;5;33;40m [0;37;40m [0m\n"
				+ "[0;37;40m    [0;5;33;40m    [0;5;37;40m@[0;5;33;40m.   . [0;5;37;40m8[0;5;33;40m.      [0;5;37;40m@[0;5;33;40m [0;5;37;40m@[0;5;33;40m [0;5;37;40m8[0;5;33;40mt;[0;5;37;40m8[0;5;33;40m [0;37;40m  [0;1;30;47m.[0;1;33;47mX[0;5;37;47m%[0;5;37;43mX[0;1;37;47m8[0;5;37;43m.[0;1;37;47m8[0;5;37;43m;[0;1;37;47m8[0;5;37;43mX[0;1;33;47mSS[0;5;37;43m%[0;1;37;47m8[0;5;37;43m [0;5;37;47m:[0;5;37;43mt[0;1;37;47m8[0;5;37;43m8[0;5;33;41m;[0;33;47m8[0;5;33;41mt[0;1;30;43m8[0;5;31;41m  %[0;5;33;40mt[0;1;31;41m8[0;5;33;41mS@[0;5;35;41mt[0;5;33;41m  [0;5;1;33;41m8[0;5;37;41m8[0;5;33;41m [0;1;30;47m8[0;37;40m [0m\n"
				+ "[0;37;40m    [0;5;33;40m         [0;5;37;40m8[0;5;33;40m:.    [0;5;37;40m8[0;5;33;40m          [0;5;37;40m8[0;5;33;40m:[0;37;40m   [0;5;37;40m8[0;33;47m@[0;1;37;47m8[0;1;33;47mXS[0;1;37;47m8[0;1;33;47m8[0;1;37;47m@[0;1;33;47m8[0;1;37;47m8[0;1;33;47m8SS[0;5;37;43mS[0;1;33;47mX[0;5;1;33;41m@[0;35;47mS[0;33;47m8[0;5;1;33;41mX[0;5;37;41m8[0;5;31;41m [0;5;33;41m .% [0;5;37;41m8[0;5;33;41m [0;5;37;41m8[0;5;33;41m [0;33;47m8[0;5;31;41m%[0;5;35;41m@[0;5;33;41m  [0;33;47m@[0;5;31;40m8[0m\n"
				+ "[0;37;40m    [0;5;33;40m   [0;5;37;40m8[0;5;33;40m [0;5;37;40m8[0;5;33;40m        [0;5;37;40m8[0;5;33;40m;.    [0;5;37;40m8[0;5;33;40m. [0;5;37;40m8[0;5;33;40m      [0;5;30;40mS[0;37;40m               [0;5;33;40mSS[0;5;35;40m:[0;35;47m8[0;5;33;41mS[0;1;30;47m8[0;33;47m8[0;1;30;47m8[0;5;33;41m;[0;1;30;47m8[0;5;33;41m [0;33;47m@[0;5;33;41m [0;33;47m8[0;5;31;41m [0;33;47m8[0;5;37;41m8[0;5;37;40m8[0;1;30;43m8[0;5;35;41m [0;5;36;40mS[0m\n"
				+ "[0;37;40m   [0;5;33;40m;[0;5;36;40m [0;5;33;40m             .      :  . [0;5;37;40m8[0;5;33;40m.  [0;5;37;40mX8[0;1;30;47m8[0;37;40m                                 [0;5;31;40mS[0;37;40m [0m\n"
				+ "[0;37;40m  [0;5;33;40m.[0;5;36;40m [0;5;33;40m  [0;5;37;40m@[0;5;33;40m   [0;5;37;40m8[0;5;33;40m. [0;5;37;40m8[0;5;33;40m  [0;5;37;40m8[0;5;33;40m. [0;5;37;40m@[0;5;33;40m  [0;5;37;40m8[0;5;33;40m:        [0;5;37;40m8[0;1;30;47mX. [0;1;37;47m.[0;1;30;47m.[0;5;37;40m@[0;37;40m                                 [0m\n"
				+ "[0;5;33;40m [0;5;37;40mX[0;5;33;40m [0;5;37;40mSS[0;5;33;40m  [0;5;37;40m8[0;5;33;40m      [0;5;37;40m8[0;5;33;40m:.  :  :        [0;1;30;47m%:  [0;1;37;47m....[0;1;30;47mS[0;37;40m                                [0m\n"
				+ "[0;5;37;40mS[0;5;33;40m [0;5;37;40m8X[0;5;33;40m [0;5;37;40m8[0;5;33;40m  [0;5;37;40m8[0;5;33;40m [0;5;37;40m88[0;5;33;40m [0;5;37;40m8[0;5;33;40m:[0;5;37;40m@[0;5;33;40m [0;5;37;40m8[0;5;33;40m  [0;5;37;40m8[0;5;33;40m.    [0;5;37;40m8[0;5;33;40m.  [0;5;37;40m8[0;1;30;47m:        %[0;37;40m                               [0m");
	}
}
