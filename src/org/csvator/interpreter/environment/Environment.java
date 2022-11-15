package org.csvator.interpreter.environment;

import java.util.HashMap;
import java.util.Map;

import org.csvator.interpreter.parsingTable.ValueInterface;

public class Environment implements Cloneable {

	private Map<String, ValueInterface> env;
	private Environment father;

	public Environment () {
		env = new HashMap<>();
		father = null;
	}

	public void setFatherEnvironment(Environment father) {
		this.father = father;
	}

	public void putValue(String key, ValueInterface value) {
		env.put(key, value);
	}

	public boolean containsKey(String key) {
		return env.containsKey(key);
	}

	public ValueInterface getValueOf(String id) throws IndexOutOfBoundsException {
		if (!env.containsKey(id) && father != null) {
			return father.getValueOf(id);
		}

		if (!env.containsKey(id)) {
			throw new IndexOutOfBoundsException("A chave " + id + " não existe no Environment");
		}

		return env.get(id);
	}

	@Override
	public Environment clone() {
		Environment newEnv = new Environment();

		for(Map.Entry<String, ValueInterface> element : env.entrySet()) {
			String key = element.getKey();
			ValueInterface value = element.getValue();
			newEnv.putValue(key, value);
		}
		newEnv.father = this.father;

		return newEnv;
	}

}
