package org.csvator.interpreter.parsingTable.function.builtIn.ClassLoader;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.List;
import java.util.stream.Collectors;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.parsingTable.function.FunctionValueInterface;

public class BuiltInFunctionClassLoader extends ClassLoader {

	private final String PACKAGE = "org.csvator.interpreter.parsingTable.function.builtIn.";

	public void loadBuiltInFunctionIntoEnvironment(Environment env) {
		ClassLoader classLoader = this.getClass().getClassLoader();
		try {
			String directory = PACKAGE.replaceAll("[.]", "/");
			InputStream inputStream = classLoader.getResourceAsStream(directory);
			BufferedReader buffReader = new BufferedReader(new InputStreamReader(inputStream));
			List<String> classNames = buffReader.lines()
			.filter(line -> line.endsWith(".class"))
			.map(line -> line.substring(0, line.length() - 6))
			.collect(Collectors.toList());

			for (String className : classNames) {
				Class<?> functionClass = classLoader.loadClass(PACKAGE + className);
				Constructor<?> constructor = functionClass.getConstructor();
				FunctionValueInterface functionObject = (FunctionValueInterface) constructor.newInstance();
				env.putValue(functionObject.getId(), functionObject);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
