package org.csvator.interpreter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Vector;

import org.csvator.interpreter.environment.Environment;
import org.csvator.interpreter.environment.operators.OperatorInterface;
import org.csvator.interpreter.environment.operators.Boolean.And;
import org.csvator.interpreter.environment.operators.Boolean.Implies;
import org.csvator.interpreter.environment.operators.Boolean.Not;
import org.csvator.interpreter.environment.operators.Boolean.Or;
import org.csvator.interpreter.environment.operators.Boolean.Xor;
import org.csvator.interpreter.environment.operators.arithmetic.Div;
import org.csvator.interpreter.environment.operators.arithmetic.Mult;
import org.csvator.interpreter.environment.operators.arithmetic.Negative;
import org.csvator.interpreter.environment.operators.arithmetic.Sub;
import org.csvator.interpreter.environment.operators.arithmetic.Sum;
import org.csvator.interpreter.environment.operators.collection.Concat;
import org.csvator.interpreter.environment.operators.collection.Contains;
import org.csvator.interpreter.environment.operators.collection.Head;
import org.csvator.interpreter.environment.operators.collection.Index;
import org.csvator.interpreter.environment.operators.collection.Remove;
import org.csvator.interpreter.environment.operators.collection.Size;
import org.csvator.interpreter.environment.operators.collection.Tail;
import org.csvator.interpreter.environment.operators.comparsion.Different;
import org.csvator.interpreter.environment.operators.comparsion.Equal;
import org.csvator.interpreter.environment.operators.comparsion.GreaterEqual;
import org.csvator.interpreter.environment.operators.comparsion.GreaterThan;
import org.csvator.interpreter.environment.operators.comparsion.LessEqual;
import org.csvator.interpreter.environment.operators.comparsion.LessThan;
import org.csvator.interpreter.parsingTable.AnonymousFunctionExpressionValue;
import org.csvator.interpreter.parsingTable.ArgumentValue;
import org.csvator.interpreter.parsingTable.BinaryExpressionValue;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.DictValue;
import org.csvator.interpreter.parsingTable.DoubleValue;
import org.csvator.interpreter.parsingTable.FieldValue;
import org.csvator.interpreter.parsingTable.FunctionCallExpressionValue;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.InvariantsDefinition;
import org.csvator.interpreter.parsingTable.KeyValueExpressionValue;
import org.csvator.interpreter.parsingTable.ListValue;
import org.csvator.interpreter.parsingTable.NullValue;
import org.csvator.interpreter.parsingTable.NullaryExpressionValue;
import org.csvator.interpreter.parsingTable.ParsingTable;
import org.csvator.interpreter.parsingTable.SetValue;
import org.csvator.interpreter.parsingTable.StringValue;
import org.csvator.interpreter.parsingTable.UnaryExpressionValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.VariableValue;
import org.csvator.interpreter.parsingTable.VectorValue;
import org.csvator.interpreter.parsingTable.function.AnonymousFunctionBody;
import org.csvator.interpreter.parsingTable.function.AnonymousFunctionBodyGuard;
import org.csvator.interpreter.parsingTable.function.UserDefinedFunctionValue;
import org.csvator.interpreter.parsingTable.function.builtIn.PrintTable;
import org.csvator.interpreter.parsingTable.function.builtIn.ClassLoader.BuiltInFunctionClassLoader;
import org.csvator.interpreter.parsingTable.typeValues.AnyTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.BoolTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.DictTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.DoubleTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.FunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.IntTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.ListTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.RecordTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.SetTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.StringTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;
import org.csvator.interpreter.parsingTable.typeValues.VectorTypeValue;
import org.csvator.interpreter.tablePrinterStrategy.TablePrinterStrategy;
import org.csvator.core.analysis.DepthFirstAdapter;
import org.csvator.core.node.AAndExpression;
import org.csvator.core.node.AAnonymousFunctionBodyGuard;
import org.csvator.core.node.AAnonymousFunctionWithArgumentExpression;
import org.csvator.core.node.AAnonymousFunctionWithoutArgumentExpression;
import org.csvator.core.node.AAnyTypeSpecifier;
import org.csvator.core.node.AArgument;
import org.csvator.core.node.ABodyFunctionDefinition;
import org.csvator.core.node.ABodyGuardAnonymousFunctionBody;
import org.csvator.core.node.ABodyGuardFunctionDefinition;
import org.csvator.core.node.ABoolTypeSpecifier;
import org.csvator.core.node.AConcatExpressionExpression;
import org.csvator.core.node.AContainsExpressionExpression;
import org.csvator.core.node.ADeclarationFunctionDefinition;
import org.csvator.core.node.ADeclarationWithArgumentFunctionDefinition;
import org.csvator.core.node.ADictExpression;
import org.csvator.core.node.ADictTypeSpecifier;
import org.csvator.core.node.ADifferentExpression;
import org.csvator.core.node.ADivExpression;
import org.csvator.core.node.ADoubleExpression;
import org.csvator.core.node.ADoubleTypeSpecifier;
import org.csvator.core.node.AEmptyDictExpression;
import org.csvator.core.node.AEmptyListExpression;
import org.csvator.core.node.AEmptySetExpression;
import org.csvator.core.node.AEmptyVectorExpression;
import org.csvator.core.node.AEqualExpression;
import org.csvator.core.node.AExpressionLineLine;
import org.csvator.core.node.AFalseExpression;
import org.csvator.core.node.AField;
import org.csvator.core.node.AFunctionApplicationWithArgumentExpression;
import org.csvator.core.node.AFunctionApplicationWithoutArgumentExpression;
import org.csvator.core.node.AFunctionTypeSpecifierNoParametersTypeSpecifier;
import org.csvator.core.node.AFunctionTypeSpecifierWithParametersTypeSpecifier;
import org.csvator.core.node.AGreaterEqualExpression;
import org.csvator.core.node.AGreaterExpression;
import org.csvator.core.node.AHeadExpression;
import org.csvator.core.node.AImpliesExpression;
import org.csvator.core.node.AIndexExpressionExpression;
import org.csvator.core.node.AIntExpression;
import org.csvator.core.node.AIntTypeSpecifier;
import org.csvator.core.node.AInvariantsDefinition;
import org.csvator.core.node.AKeyValueExpression;
import org.csvator.core.node.ALessEqualExpression;
import org.csvator.core.node.ALessExpression;
import org.csvator.core.node.AListExpression;
import org.csvator.core.node.AListTypeSpecifier;
import org.csvator.core.node.AMultExpression;
import org.csvator.core.node.ANegativeExpression;
import org.csvator.core.node.ANotExpression;
import org.csvator.core.node.ANullExpression;
import org.csvator.core.node.AOrExpression;
import org.csvator.core.node.AParenExpression;
import org.csvator.core.node.ARecordType;
import org.csvator.core.node.ARemoveExpressionExpression;
import org.csvator.core.node.ASetExpression;
import org.csvator.core.node.ASetTypeSpecifier;
import org.csvator.core.node.ASingleExpressionAnonymousFunctionBody;
import org.csvator.core.node.ASizeExpression;
import org.csvator.core.node.AStringLiteralExpression;
import org.csvator.core.node.AStringTypeSpecifier;
import org.csvator.core.node.ASubExpression;
import org.csvator.core.node.ASumExpression;
import org.csvator.core.node.ATailExpression;
import org.csvator.core.node.ATrueExpression;
import org.csvator.core.node.ATypeDefinition;
import org.csvator.core.node.AVarExpression;
import org.csvator.core.node.AVariableDefinition;
import org.csvator.core.node.AVectorExpression;
import org.csvator.core.node.AVectorTypeSpecifier;
import org.csvator.core.node.AXorExpression;
import org.csvator.core.node.Node;
import org.csvator.core.node.PAnonymousFunctionBodyGuard;
import org.csvator.core.node.PArgument;
import org.csvator.core.node.PExpression;
import org.csvator.core.node.PField;
import org.csvator.core.node.PTypeSpecifier;

public class Interpreter extends DepthFirstAdapter {

	private ParsingTable parsingTable;
	private Environment global;
	private String prompt = "";

	public Interpreter() {
		parsingTable = new ParsingTable();
		global = new Environment();
		BuiltInFunctionClassLoader builtInFunctionClassLoader = new BuiltInFunctionClassLoader();
		try {
			builtInFunctionClassLoader.loadBuiltInFunctionIntoEnvironment(global);
		} catch (Exception e) {
			// do nothing
		}
	}

	public void setPromptString(String prompt) {
		this.prompt = prompt;
	}

	public void setTablePrinter(TablePrinterStrategy printer) {
		PrintTable printTable = (PrintTable) this.global.getValueOf("printTable");
		printTable.setTablePrinter(printer);
	}

	@Override
	public void outAIntExpression(AIntExpression node) {
		super.outAIntExpression(node);

		String textInt = node.getInteger().getText().trim();
		IntegerValue intVal = new IntegerValue(node.toString(), textInt);
		NullaryExpressionValue expression = new NullaryExpressionValue(node.toString(), intVal);
		parsingTable.putValue(node, expression);
	}

	@Override
	public void outADoubleExpression(ADoubleExpression node) {
		super.outADoubleExpression(node);

		String textDouble = node.getDouble().getText().trim();
		DoubleValue doubleVal = new DoubleValue(node.toString(), textDouble);
		NullaryExpressionValue expression = new NullaryExpressionValue(node.toString(), doubleVal);
		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAStringLiteralExpression(AStringLiteralExpression node) {
		super.outAStringLiteralExpression(node);

		String value = node.getString().getText();
		value = value.substring(1, value.length() - 1);
		StringValue strVal = new StringValue(node.toString(), value);
		NullaryExpressionValue expression = new NullaryExpressionValue(node.toString(), strVal);
		parsingTable.putValue(node, expression);
	}

	@Override
	public void outANullExpression(ANullExpression node) {
		super.outANullExpression(node);

		NullValue nullVal = NullValue.getInstace();
		parsingTable.putValue(node, nullVal);
	}

	@Override
	public void outAKeyValueExpression(AKeyValueExpression node) {
		super.outAKeyValueExpression(node);

		ValueInterface key = parsingTable.getValueOf(node.getKey());
		ValueInterface value = parsingTable.getValueOf(node.getValue());
		KeyValueExpressionValue keyValue = new KeyValueExpressionValue(node.toString(), key, value);
		parsingTable.putValue(node, keyValue);
	}

	@Override
	public void outAEmptyVectorExpression(AEmptyVectorExpression node) {
		super.outAEmptyVectorExpression(node);

		VectorValue vector = new VectorValue(node.toString());
		parsingTable.putValue(node, vector);
	}

	@Override
	public void outAVectorExpression(AVectorExpression node) {
		super.outAVectorExpression(node);

		Vector<ValueInterface> vectorValues = new Vector<ValueInterface>();

		ValueInterface value = parsingTable.getValueOf(node.getFirst()).evaluate(global);
		vectorValues.add(value);
		for (PExpression nodeExpression : node.getRest()) {
			value = parsingTable.getValueOf(nodeExpression).evaluate(global);
			vectorValues.add(value);
		}

		VectorValue vector = new VectorValue(node.toString(), vectorValues);
		parsingTable.putValue(node, vector);
	}

	@Override
	public void outAEmptyListExpression(AEmptyListExpression node) {
		super.outAEmptyListExpression(node);

		ListValue list = new ListValue(node.toString());
		parsingTable.putValue(node, list);
	}

	@Override
	public void outAListExpression(AListExpression node) {
		super.outAListExpression(node);

		LinkedList<ValueInterface> listValues = new LinkedList<ValueInterface>();

		ValueInterface value = parsingTable.getValueOf(node.getFirst()).evaluate(global);
		listValues.add(value);
		for (PExpression nodeExpression : node.getRest()) {
			value = parsingTable.getValueOf(nodeExpression).evaluate(global);
			listValues.add(value);
		}

		ListValue list = new ListValue(node.toString(), listValues);
		parsingTable.putValue(node, list);
	}

	@Override
	public void outAEmptyDictExpression(AEmptyDictExpression node) {
		super.outAEmptyDictExpression(node);

		DictValue list = new DictValue(node.toString());
		parsingTable.putValue(node, list);
	}

	@Override
	public void outADictExpression(ADictExpression node) {
		super.outADictExpression(node);

		HashMap<ValueInterface, ValueInterface> dictValues = new HashMap<ValueInterface, ValueInterface>();

		KeyValueExpressionValue value = (KeyValueExpressionValue) parsingTable.getValueOf(node.getFirst()).evaluate(global);
		dictValues.put(value.getKey(), value.getValue());
		for (PExpression nodeExpression : node.getRest()) {
			value = (KeyValueExpressionValue) parsingTable.getValueOf(nodeExpression).evaluate(global);
			dictValues.put(value.getKey(), value.getValue());
		}

		DictValue list = new DictValue(node.toString(), dictValues);
		parsingTable.putValue(node, list);
	}

	@Override
	public void outAEmptySetExpression(AEmptySetExpression node) {
		super.outAEmptySetExpression(node);

		SetValue list = new SetValue(node.toString());
		parsingTable.putValue(node, list);
	}

	@Override
	public void outASetExpression(ASetExpression node) {
		super.outASetExpression(node);

		HashSet<ValueInterface> setValues = new HashSet<ValueInterface>();

		ValueInterface value = parsingTable.getValueOf(node.getFirst()).evaluate(global);
		setValues.add(value);
		for (PExpression nodeExpression : node.getRest()) {
			value = parsingTable.getValueOf(nodeExpression).evaluate(global);
			setValues.add(value);
		}

		SetValue list = new SetValue(node.toString(), setValues);
		parsingTable.putValue(node, list);
	}

	@Override
	public void outAVarExpression(AVarExpression node) {
		super.outAVarExpression(node);

		String identifier = node.getIdentifier().getText();
		VariableValue value = new VariableValue(identifier);
		NullaryExpressionValue expression = new NullaryExpressionValue(identifier, value);
		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAParenExpression(AParenExpression node) {
		super.outAParenExpression(node);

		ValueInterface value = parsingTable.getValueOf(node.getExpression());
		parsingTable.putValue(node, value);
	}

	@Override
	public void outASumExpression(ASumExpression node) {
		super.outASumExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new Sum());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outASubExpression(ASubExpression node) {
		super.outASubExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new Sub());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAMultExpression(AMultExpression node) {
		super.outAMultExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(),
				new Mult());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outADivExpression(ADivExpression node) {
		super.outADivExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new Div());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outANegativeExpression(ANegativeExpression node) {
		super.outANegativeExpression(node);

		UnaryExpressionValue expression = buildExpression(node.toString(), node.getExpression(), new Negative());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outATrueExpression(ATrueExpression node) {
		super.outATrueExpression(node);

		String textTrue = node.getKwTrue().getText().trim();
		BooleanValue boolVal = new BooleanValue(node.toString(), textTrue);
		parsingTable.putValue(node, boolVal);
	}

	@Override
	public void outAFalseExpression(AFalseExpression node) {
		super.outAFalseExpression(node);

		String textFalse = node.getKwFalse().getText().trim();
		BooleanValue boolVal = new BooleanValue(node.toString(), textFalse);
		parsingTable.putValue(node, boolVal);
	}

	@Override
	public void outANotExpression(ANotExpression node) {
		super.outANotExpression(node);

		UnaryExpressionValue expression = buildExpression(node.toString(), node.getExpression(), new Not());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAAndExpression(AAndExpression node) {
		super.outAAndExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new And());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAOrExpression(AOrExpression node) {
		super.outAOrExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new Or());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAXorExpression(AXorExpression node) {
		super.outAXorExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new Xor());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAImpliesExpression(AImpliesExpression node) {
		super.outAImpliesExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(),
				new Implies());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAGreaterExpression(AGreaterExpression node) {
		super.outAGreaterExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(),
				new GreaterThan());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outALessExpression(ALessExpression node) {
		super.outALessExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(),
				new LessThan());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAGreaterEqualExpression(AGreaterEqualExpression node) {
		super.outAGreaterEqualExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(),
				new GreaterEqual());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outALessEqualExpression(ALessEqualExpression node) {
		super.outALessEqualExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(),
				new LessEqual());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAEqualExpression(AEqualExpression node) {
		super.outAEqualExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(),
				new Equal());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outADifferentExpression(ADifferentExpression node) {
		super.outADifferentExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(),
				new Different());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAConcatExpressionExpression(AConcatExpressionExpression node) {
		super.outAConcatExpressionExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new Concat());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outARemoveExpressionExpression(ARemoveExpressionExpression node) {
		super.outARemoveExpressionExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new Remove());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAIndexExpressionExpression(AIndexExpressionExpression node) {
		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new Index());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAContainsExpressionExpression(AContainsExpressionExpression node) {
		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new Contains());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAHeadExpression(AHeadExpression node) {
		UnaryExpressionValue expression = buildExpression(node.toString(), node.getExpression(), new Head());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outATailExpression(ATailExpression node) {
		UnaryExpressionValue expression = buildExpression(node.toString(), node.getExpression(), new Tail());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outASizeExpression(ASizeExpression node) {
		UnaryExpressionValue expression = buildExpression(node.toString(), node.getExpression(), new Size());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAVariableDefinition(AVariableDefinition node) {
		super.outAVariableDefinition(node);

		ValueInterface value = parsingTable.getValueOf(node.getExpression());
		VariableValue variableValue;
		if (value instanceof FunctionCallExpressionValue) {
			value = value.evaluate(global);
			variableValue = new VariableValue(node.getVariable().getText(), value);
		} else {
			variableValue = new VariableValue(node.getVariable().getText(), value);
		}
		parsingTable.putValue(node.getVariable(), variableValue);
		global.putValue(node.getVariable().getText(), value);
	}

	@Override
	public void outATypeDefinition(ATypeDefinition node) {
		super.outATypeDefinition(node);
		String typeName = node.getName().getText();
		RecordTypeValue record = (RecordTypeValue) parsingTable.getValueOf(node.getRecordType());
		record.setId(typeName);
		global.putValue(typeName, record);
	}

	@Override
	public void outARecordType(ARecordType node) {
		super.outARecordType(node);
		LinkedList<FieldValue> fieldList = new LinkedList<FieldValue>();
		FieldValue value;
		for (PField nodeField : node.getFields()) {
			value = (FieldValue) parsingTable.getValueOf(nodeField);
			fieldList.add(value);
		}

		RecordTypeValue record = new RecordTypeValue(fieldList);
		if (node.getInvariantsDefinition() != null) {
			InvariantsDefinition invariants = (InvariantsDefinition) parsingTable.getValueOf(node.getInvariantsDefinition());
			record.setInvariants(invariants.getInvariants());
		}

		parsingTable.putValue(node, record);
	}

	@Override
	public void outAInvariantsDefinition(AInvariantsDefinition node) {
		super.outAInvariantsDefinition(node);
		LinkedList<ValueInterface> invariantList = new LinkedList<ValueInterface>();

		ValueInterface value;
		for (PExpression nodeExpression : node.getExpressions()) {
			value = parsingTable.getValueOf(nodeExpression);
			invariantList.add(value);
		}

		InvariantsDefinition invariants = new InvariantsDefinition(invariantList);
		parsingTable.putValue(node, invariants);
	}

	@Override
	public void outAField(AField node) {
		super.outAField(node);

		String fieldName = node.getName().getText();
		TypeValueInterface type = (TypeValueInterface) parsingTable.getValueOf(node.getType());
		FieldValue field = new FieldValue(fieldName, fieldName, type);

		parsingTable.putValue(node, field);
	}

	@Override
	public void outAIntTypeSpecifier(AIntTypeSpecifier node) {
		super.outAIntTypeSpecifier(node);

		IntTypeValue type = IntTypeValue.getInstance();
		parsingTable.putValue(node, type);
	}

	@Override
	public void outADoubleTypeSpecifier(ADoubleTypeSpecifier node) {
		super.outADoubleTypeSpecifier(node);

		DoubleTypeValue type = DoubleTypeValue.getInstance();
		parsingTable.putValue(node, type);
	}

	@Override
	public void outABoolTypeSpecifier(ABoolTypeSpecifier node) {
		super.outABoolTypeSpecifier(node);

		BoolTypeValue type = BoolTypeValue.getInstance();
		parsingTable.putValue(node, type);
	}

	@Override
	public void outAStringTypeSpecifier(AStringTypeSpecifier node) {
		super.outAStringTypeSpecifier(node);

		StringTypeValue type = StringTypeValue.getInstance();
		parsingTable.putValue(node, type);
	}

	@Override
	public void outAAnyTypeSpecifier(AAnyTypeSpecifier node) {
		super.outAAnyTypeSpecifier(node);

		AnyTypeValue type = AnyTypeValue.getInstance();
		parsingTable.putValue(node, type);
	}

	@Override
	public void outAVectorTypeSpecifier(AVectorTypeSpecifier node) {
		super.outAVectorTypeSpecifier(node);

		VectorTypeValue type = VectorTypeValue.getInstance();
		parsingTable.putValue(node, type);
	}

	@Override
	public void outADictTypeSpecifier(ADictTypeSpecifier node) {
		super.outADictTypeSpecifier(node);

		DictTypeValue type = DictTypeValue.getInstance();
		parsingTable.putValue(node, type);
	}

	@Override
	public void outASetTypeSpecifier(ASetTypeSpecifier node) {
		super.outASetTypeSpecifier(node);

		SetTypeValue type = SetTypeValue.getInstance();
		parsingTable.putValue(node, type);
	}

	@Override
	public void outAListTypeSpecifier(AListTypeSpecifier node) {
		super.outAListTypeSpecifier(node);

		ListTypeValue type = ListTypeValue.getInstance();
		parsingTable.putValue(node, type);
	}

	@Override
	public void outAFunctionTypeSpecifierNoParametersTypeSpecifier(
			AFunctionTypeSpecifierNoParametersTypeSpecifier node) {
		super.outAFunctionTypeSpecifierNoParametersTypeSpecifier(node);

		TypeValueInterface result = (TypeValueInterface) parsingTable.getValueOf(node.getResult());
		FunctionTypeValue type = new FunctionTypeValue(result);
		parsingTable.putValue(node, type);
	}

	@Override
	public void outAFunctionTypeSpecifierWithParametersTypeSpecifier(
			AFunctionTypeSpecifierWithParametersTypeSpecifier node) {
		super.outAFunctionTypeSpecifierWithParametersTypeSpecifier(node);

		LinkedList<TypeValueInterface> parametersTypes = new LinkedList<>();
		TypeValueInterface result = (TypeValueInterface) parsingTable.getValueOf(node.getResult());
		TypeValueInterface pType = (TypeValueInterface) parsingTable.getValueOf(node.getFirst());
		parametersTypes.add(pType);
		for (PTypeSpecifier specifier : node.getRest()) {
			pType = (TypeValueInterface) parsingTable.getValueOf(specifier);
			parametersTypes.add(pType);
		}
		FunctionTypeValue type = new FunctionTypeValue(parametersTypes, result);
		parsingTable.putValue(node, type);
	}

	@Override
	public void outAArgument(AArgument node) {
		super.outAArgument(node);

		TypeValueInterface type = (TypeValueInterface) parsingTable.getValueOf(node.getTypeSpecifier());
		ArgumentValue identifier = new ArgumentValue(node.toString(), node.getIdentifier().getText(), type);
		parsingTable.putValue(node, identifier);
	}

	@Override
	public void outADeclarationFunctionDefinition(ADeclarationFunctionDefinition node) {
		super.outADeclarationFunctionDefinition(node);

		String functionIdentifier = node.getFunctionIdentifier().getText();
		TypeValueInterface returnType = (TypeValueInterface) parsingTable.getValueOf(node.getReturnType());

		UserDefinedFunctionValue function = new UserDefinedFunctionValue(functionIdentifier, returnType);
		global.putValue(functionIdentifier, function);
	}

	@Override
	public void outADeclarationWithArgumentFunctionDefinition(ADeclarationWithArgumentFunctionDefinition node) {
		super.outADeclarationWithArgumentFunctionDefinition(node);

		String functionIdentifier = node.getFunctionIdentifier().getText();
		TypeValueInterface returnType = (TypeValueInterface) parsingTable.getValueOf(node.getReturnType());
		LinkedList<ArgumentValue> arguments = new LinkedList<>();

		ArgumentValue value = (ArgumentValue) parsingTable.getValueOf(node.getFirst());
		arguments.add(value);
		for (PArgument nodeArgument : node.getRest()) {
			value = (ArgumentValue) parsingTable.getValueOf(nodeArgument);
			arguments.add(value);
		}

		UserDefinedFunctionValue function = new UserDefinedFunctionValue(functionIdentifier, returnType, arguments);
		global.putValue(functionIdentifier, function);
	}

	@Override
	public void outABodyGuardFunctionDefinition(ABodyGuardFunctionDefinition node) {
		super.outABodyGuardFunctionDefinition(node);

		String functionIdentifier = node.getFunctionIdentifier().getText();
		UserDefinedFunctionValue function = (UserDefinedFunctionValue) global.getValueOf(functionIdentifier);
		ValueInterface condition = parsingTable.getValueOf(node.getCondition());

		ValueInterface result = parsingTable.getValueOf(node.getResult());

		function.addExpression(condition, result);
	}

	@Override
	public void outABodyFunctionDefinition(ABodyFunctionDefinition node) {
		super.outABodyFunctionDefinition(node);

		String functionIdentifier = node.getFunctionIdentifier().getText();
		UserDefinedFunctionValue function = (UserDefinedFunctionValue) global.getValueOf(functionIdentifier);
		NullaryExpressionValue condition = new NullaryExpressionValue("Tautology", new BooleanValue("True", true));

		ValueInterface result = parsingTable.getValueOf(node.getResult());

		function.addExpression(condition, result);
	}

	@Override
	public void outAFunctionApplicationWithArgumentExpression(AFunctionApplicationWithArgumentExpression node) {
		super.outAFunctionApplicationWithArgumentExpression(node);

		String functionIdentifier = node.getFunctionIdentifier().getText();
		ValueInterface value = parsingTable.getValueOf(node.getFirst());
		LinkedList<ValueInterface> expressions = new LinkedList<>();
		expressions.add(value);

		for (PExpression nodeExpression : node.getRest()) {
			value = parsingTable.getValueOf(nodeExpression);
			expressions.add(value);
		}

		FunctionCallExpressionValue functionExpression = new FunctionCallExpressionValue(functionIdentifier, expressions);
		parsingTable.putValue(node, functionExpression);
	}

	@Override
	public void outAFunctionApplicationWithoutArgumentExpression(AFunctionApplicationWithoutArgumentExpression node) {
		super.outAFunctionApplicationWithoutArgumentExpression(node);

		String functionIdentifier = node.getFunctionIdentifier().getText();
		LinkedList<ValueInterface> expressions = new LinkedList<>();

		FunctionCallExpressionValue functionExpression = new FunctionCallExpressionValue(functionIdentifier, expressions);
		parsingTable.putValue(node, functionExpression);
	}

	@Override
	public void outAAnonymousFunctionBodyGuard(AAnonymousFunctionBodyGuard node) {
		super.outAAnonymousFunctionBodyGuard(node);

		ValueInterface condition = parsingTable.getValueOf(node.getCondition());
		ValueInterface result = parsingTable.getValueOf(node.getResult());
		AnonymousFunctionBodyGuard bodyGuard = new AnonymousFunctionBodyGuard(condition, result);
		parsingTable.putValue(node, bodyGuard);
	}

	@Override
	public void outASingleExpressionAnonymousFunctionBody(ASingleExpressionAnonymousFunctionBody node) {
		super.outASingleExpressionAnonymousFunctionBody(node);

		ValueInterface expression = parsingTable.getValueOf(node.getExpression());
		AnonymousFunctionBody body = new AnonymousFunctionBody(expression);
		parsingTable.putValue(node, body);
	}

	@Override
	public void outABodyGuardAnonymousFunctionBody(ABodyGuardAnonymousFunctionBody node) {
		super.outABodyGuardAnonymousFunctionBody(node);

		AnonymousFunctionBodyGuard value = (AnonymousFunctionBodyGuard) parsingTable.getValueOf(node.getFirst());
		LinkedList<AnonymousFunctionBodyGuard> guards = new LinkedList<>();
		guards.add(value);

		for (PAnonymousFunctionBodyGuard nodeExpression : node.getRest()) {
			value = (AnonymousFunctionBodyGuard) parsingTable.getValueOf(nodeExpression);
			guards.add(value);
		}

		AnonymousFunctionBody body = new AnonymousFunctionBody(guards);
		parsingTable.putValue(node, body);
	}

	@Override
	public void outAAnonymousFunctionWithoutArgumentExpression(AAnonymousFunctionWithoutArgumentExpression node) {
		super.outAAnonymousFunctionWithoutArgumentExpression(node);

		TypeValueInterface returnType = (TypeValueInterface) parsingTable.getValueOf(node.getReturnType());

		AnonymousFunctionBody body = (AnonymousFunctionBody) parsingTable.getValueOf(node.getBody());
		UserDefinedFunctionValue function = new UserDefinedFunctionValue(node.toString(), returnType);
		if (body.isSingleExpression()) {
			NullaryExpressionValue condition = new NullaryExpressionValue("Tautology", new BooleanValue("True", true));
			function.addExpression(condition, body.getExpression());
		} else {
			function.setExpressions(body.getGuards());
		}

		AnonymousFunctionExpressionValue anonFunction = new AnonymousFunctionExpressionValue(node.toString(), function);
		parsingTable.putValue(node, anonFunction);
	}

	@Override
	public void outAAnonymousFunctionWithArgumentExpression(AAnonymousFunctionWithArgumentExpression node) {
		super.outAAnonymousFunctionWithArgumentExpression(node);

		TypeValueInterface returnType = (TypeValueInterface) parsingTable.getValueOf(node.getReturnType());
		LinkedList<ArgumentValue> arguments = new LinkedList<>();

		ArgumentValue value = (ArgumentValue) parsingTable.getValueOf(node.getFirst());
		arguments.add(value);
		for (PArgument nodeArgument : node.getRest()) {
			value = (ArgumentValue) parsingTable.getValueOf(nodeArgument);
			arguments.add(value);
		}

		AnonymousFunctionBody body = (AnonymousFunctionBody) parsingTable.getValueOf(node.getBody());
		UserDefinedFunctionValue function = new UserDefinedFunctionValue(node.toString(), returnType, arguments);
		if (body.isSingleExpression()) {
			NullaryExpressionValue condition = new NullaryExpressionValue("Tautology", new BooleanValue("True", true));
			function.addExpression(condition, body.getExpression());
		} else {
			function.setExpressions(body.getGuards());
		}

		AnonymousFunctionExpressionValue anonFunction = new AnonymousFunctionExpressionValue(node.toString(), function);
		parsingTable.putValue(node, anonFunction);
	}

	@Override
	public void outAExpressionLineLine(AExpressionLineLine node) {
		super.outAExpressionLineLine(node);

		ValueInterface value = parsingTable.getValueOf(node.getExpression());
		System.out.println(this.prompt + value.evaluate(global));
	}

	private BinaryExpressionValue buildExpression(String id, Node lho, Node rho, OperatorInterface op) {
		ValueInterface leftHand = parsingTable.getValueOf(lho);
		ValueInterface rightHand = parsingTable.getValueOf(rho);

		return new BinaryExpressionValue(id, op, leftHand, rightHand);
	}

	private UnaryExpressionValue buildExpression(String id, Node lho, OperatorInterface op) {
		ValueInterface leftHand = parsingTable.getValueOf(lho);

		return new UnaryExpressionValue(id, op, leftHand);
	}

}
