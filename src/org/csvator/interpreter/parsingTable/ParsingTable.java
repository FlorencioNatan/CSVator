package org.csvator.interpreter.parsingTable;

import java.util.HashMap;
import java.util.Map;

import org.csvator.core.node.Node;

public class ParsingTable {

	private Map<Node, ValueInterface> env;
	private ParsingTable father;

	public ParsingTable () {
		env = new HashMap<>();
		father = null;
	}

	public void setFatherEnvironment(ParsingTable father) {
		this.father = father;
	}

	public void putValue(Node key, ValueInterface value) {
		env.put(key, value);
	}

	public boolean containsKey(Node key) {
		return env.containsKey(key);
	}

	public ValueInterface getValueOf(Node id) throws IndexOutOfBoundsException {
		if (!env.containsKey(id) && father != null) {
			return father.getValueOf(id);
		}

		if (!env.containsKey(id)) {
			throw new IndexOutOfBoundsException("A chave " + id + " não existe na ParsingTable");
		}

		return env.get(id);
	}

}
