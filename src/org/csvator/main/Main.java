package org.csvator.main;

import java.io.FileReader;
import java.io.PushbackReader;

import org.csvator.core.lexer.Lexer;
import org.csvator.core.node.Start;
import org.csvator.core.parser.Parser;
import org.csvator.interpreter.Interpreter;


public class Main {

	public static void main(String[] args) {
		if (args.length > 0) {
			try {
				Lexer lexer = new Lexer(new PushbackReader(new FileReader(args[0]), 1024));
				Parser parser = new Parser(lexer);
				Start ast = parser.parse();

				Interpreter interp = new Interpreter();
				ast.apply(interp);
			} catch (Exception e) {
				System.out.println(e);
			}
		} else {
			System.err.println("No input file specified.");
			System.exit(1);
		}
	}

}
