package org.csvator.interpreter;

import java.util.LinkedList;

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
import org.csvator.interpreter.environment.operators.comparsion.Different;
import org.csvator.interpreter.environment.operators.comparsion.Equal;
import org.csvator.interpreter.environment.operators.comparsion.GreaterEqual;
import org.csvator.interpreter.environment.operators.comparsion.GreaterThan;
import org.csvator.interpreter.environment.operators.comparsion.LessEqual;
import org.csvator.interpreter.environment.operators.comparsion.LessThan;
import org.csvator.interpreter.environment.operators.string.Concat;
import org.csvator.interpreter.parsingTable.ArgumentValue;
import org.csvator.interpreter.parsingTable.BinaryExpressionValue;
import org.csvator.interpreter.parsingTable.BooleanValue;
import org.csvator.interpreter.parsingTable.DoubleValue;
import org.csvator.interpreter.parsingTable.ExpressionValueInterface;
import org.csvator.interpreter.parsingTable.FunctionExpressionValue;
import org.csvator.interpreter.parsingTable.IntegerValue;
import org.csvator.interpreter.parsingTable.NullaryExpressionValue;
import org.csvator.interpreter.parsingTable.ParsingTable;
import org.csvator.interpreter.parsingTable.StringValue;
import org.csvator.interpreter.parsingTable.UnaryExpressionValue;
import org.csvator.interpreter.parsingTable.ValueInterface;
import org.csvator.interpreter.parsingTable.VariableValue;
import org.csvator.interpreter.parsingTable.function.FunctionCall;
import org.csvator.interpreter.parsingTable.function.FunctionValue;
import org.csvator.interpreter.parsingTable.typeValues.BoolTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.DoubleTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.FunctionTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.IntTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.StringTypeValue;
import org.csvator.interpreter.parsingTable.typeValues.TypeValueInterface;
import org.csvator.core.analysis.DepthFirstAdapter;
import org.csvator.core.node.AAndExpression;
import org.csvator.core.node.AArgument;
import org.csvator.core.node.ABodyFunctionDefinition;
import org.csvator.core.node.ABodyGuardFunctionDefinition;
import org.csvator.core.node.ABoolTypeSpecifier;
import org.csvator.core.node.AConcatExpressionExpression;
import org.csvator.core.node.ADeclarationFunctionDefinition;
import org.csvator.core.node.ADeclarationWithArgumentFunctionDefinition;
import org.csvator.core.node.ADifferentExpression;
import org.csvator.core.node.ADivExpression;
import org.csvator.core.node.ADoubleExpression;
import org.csvator.core.node.ADoubleTypeSpecifier;
import org.csvator.core.node.AEqualExpression;
import org.csvator.core.node.AExpressionLineLine;
import org.csvator.core.node.AFalseExpression;
import org.csvator.core.node.AFunctionApplicationWithArgumentExpression;
import org.csvator.core.node.AFunctionApplicationWithoutArgumentExpression;
import org.csvator.core.node.AFunctionTypeSpecifierNoParametersTypeSpecifier;
import org.csvator.core.node.AFunctionTypeSpecifierWithParametersTypeSpecifier;
import org.csvator.core.node.AGreaterEqualExpression;
import org.csvator.core.node.AGreaterExpression;
import org.csvator.core.node.AImpliesExpression;
import org.csvator.core.node.AIntExpression;
import org.csvator.core.node.AIntTypeSpecifier;
import org.csvator.core.node.ALessEqualExpression;
import org.csvator.core.node.ALessExpression;
import org.csvator.core.node.AMultExpression;
import org.csvator.core.node.ANegativeExpression;
import org.csvator.core.node.ANotExpression;
import org.csvator.core.node.AOrExpression;
import org.csvator.core.node.AParenExpression;
import org.csvator.core.node.AStringLiteralExpression;
import org.csvator.core.node.AStringTypeSpecifier;
import org.csvator.core.node.ASubExpression;
import org.csvator.core.node.ASumExpression;
import org.csvator.core.node.ATrueExpression;
import org.csvator.core.node.AVarExpression;
import org.csvator.core.node.AVariableDefinition;
import org.csvator.core.node.AXorExpression;
import org.csvator.core.node.Node;
import org.csvator.core.node.PArgument;
import org.csvator.core.node.PExpression;
import org.csvator.core.node.PTypeSpecifier;

public class Interpreter extends DepthFirstAdapter {

	private ParsingTable parsingTable;
	private Environment global;

	public Interpreter() {
		parsingTable = new ParsingTable();
		global = new Environment();
	}

	@Override
	public void outAIntExpression(AIntExpression node) {
		// TODO Auto-generated method stub
		super.outAIntExpression(node);

		String textInt = node.getInteger().getText().trim();
		IntegerValue intVal = new IntegerValue(node.toString(), textInt);
		NullaryExpressionValue expression = new NullaryExpressionValue(node.toString(), intVal);
		parsingTable.putValue(node, expression);
	}

	@Override
	public void outADoubleExpression(ADoubleExpression node) {
		// TODO Auto-generated method stub
		super.outADoubleExpression(node);

		String textDouble = node.getDouble().getText().trim();
		DoubleValue doubleVal = new DoubleValue(node.toString(), textDouble);
		NullaryExpressionValue expression = new NullaryExpressionValue(node.toString(), doubleVal);
		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAStringLiteralExpression(AStringLiteralExpression node) {
		// TODO Auto-generated method stub
		super.outAStringLiteralExpression(node);

		String value = node.getString().getText();
		value = value.substring(1, value.length() - 1);
		StringValue strVal = new StringValue(node.toString(), value);
		NullaryExpressionValue expression = new NullaryExpressionValue(node.toString(), strVal);
		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAVarExpression(AVarExpression node) {
		// TODO Auto-generated method stub
		super.outAVarExpression(node);

		String identifier = node.getIdentifier().getText();
		VariableValue value = new VariableValue(identifier);
		NullaryExpressionValue expression = new NullaryExpressionValue(identifier, value);
		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAParenExpression(AParenExpression node) {
		// TODO Auto-generated method stub
		super.outAParenExpression(node);

		ValueInterface value = parsingTable.getValueOf(node.getExpression());
		parsingTable.putValue(node, value);
	}

	@Override
	public void outASumExpression(ASumExpression node) {
		// TODO Auto-generated method stub
		super.outASumExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new Sum());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outASubExpression(ASubExpression node) {
		// TODO Auto-generated method stub
		super.outASubExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new Sub());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAMultExpression(AMultExpression node) {
		// TODO Auto-generated method stub
		super.outAMultExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(),
				new Mult());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outADivExpression(ADivExpression node) {
		// TODO Auto-generated method stub
		super.outADivExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new Div());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outANegativeExpression(ANegativeExpression node) {
		// TODO Auto-generated method stub
		super.outANegativeExpression(node);

		UnaryExpressionValue expression = buildExpression(node.toString(), node.getExpression(), new Negative());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outATrueExpression(ATrueExpression node) {
		// TODO Auto-generated method stub
		super.outATrueExpression(node);

		String textTrue = node.getKwTrue().getText().trim();
		BooleanValue boolVal = new BooleanValue(node.toString(), textTrue);
		parsingTable.putValue(node, boolVal);
	}

	@Override
	public void outAFalseExpression(AFalseExpression node) {
		// TODO Auto-generated method stub
		super.outAFalseExpression(node);

		String textFalse = node.getKwFalse().getText().trim();
		BooleanValue boolVal = new BooleanValue(node.toString(), textFalse);
		parsingTable.putValue(node, boolVal);
	}

	@Override
	public void outANotExpression(ANotExpression node) {
		// TODO Auto-generated method stub
		super.outANotExpression(node);

		UnaryExpressionValue expression = buildExpression(node.toString(), node.getExpression(), new Not());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAAndExpression(AAndExpression node) {
		// TODO Auto-generated method stub
		super.outAAndExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new And());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAOrExpression(AOrExpression node) {
		// TODO Auto-generated method stub
		super.outAOrExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new Or());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAXorExpression(AXorExpression node) {
		// TODO Auto-generated method stub
		super.outAXorExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new Xor());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAImpliesExpression(AImpliesExpression node) {
		// TODO Auto-generated method stub
		super.outAImpliesExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(),
				new Implies());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAGreaterExpression(AGreaterExpression node) {
		// TODO Auto-generated method stub
		super.outAGreaterExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(),
				new GreaterThan());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outALessExpression(ALessExpression node) {
		// TODO Auto-generated method stub
		super.outALessExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(),
				new LessThan());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAGreaterEqualExpression(AGreaterEqualExpression node) {
		// TODO Auto-generated method stub
		super.outAGreaterEqualExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(),
				new GreaterEqual());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outALessEqualExpression(ALessEqualExpression node) {
		// TODO Auto-generated method stub
		super.outALessEqualExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(),
				new LessEqual());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAEqualExpression(AEqualExpression node) {
		// TODO Auto-generated method stub
		super.outAEqualExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(),
				new Equal());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outADifferentExpression(ADifferentExpression node) {
		// TODO Auto-generated method stub
		super.outADifferentExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(),
				new Different());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAConcatExpressionExpression(AConcatExpressionExpression node) {
		// TODO Auto-generated method stub
		super.outAConcatExpressionExpression(node);

		BinaryExpressionValue expression = buildExpression(node.toString(), node.getLeft(), node.getRight(), new Concat());

		parsingTable.putValue(node, expression);
	}

	@Override
	public void outAVariableDefinition(AVariableDefinition node) {
		// TODO Auto-generated method stub
		super.outAVariableDefinition(node);

		ValueInterface value = parsingTable.getValueOf(node.getExpression());
		VariableValue variableValue = new VariableValue(node.getVariable().getText(), value);
		parsingTable.putValue(node.getVariable(), variableValue);
		global.putValue(node.getVariable().getText(), value);
	}

	@Override
	public void outAIntTypeSpecifier(AIntTypeSpecifier node) {
		// TODO Auto-generated method stub
		super.outAIntTypeSpecifier(node);

		IntTypeValue type = IntTypeValue.getInstace();
		parsingTable.putValue(node, type);
	}

	@Override
	public void outADoubleTypeSpecifier(ADoubleTypeSpecifier node) {
		// TODO Auto-generated method stub
		super.outADoubleTypeSpecifier(node);

		DoubleTypeValue type = DoubleTypeValue.getInstace();
		parsingTable.putValue(node, type);
	}

	@Override
	public void outABoolTypeSpecifier(ABoolTypeSpecifier node) {
		// TODO Auto-generated method stub
		super.outABoolTypeSpecifier(node);

		BoolTypeValue type = BoolTypeValue.getInstace();
		parsingTable.putValue(node, type);
	}

	@Override
	public void outAStringTypeSpecifier(AStringTypeSpecifier node) {
		// TODO Auto-generated method stub
		super.outAStringTypeSpecifier(node);

		StringTypeValue type = StringTypeValue.getInstace();
		parsingTable.putValue(node, type);
	}

	@Override
	public void outAFunctionTypeSpecifierNoParametersTypeSpecifier(
			AFunctionTypeSpecifierNoParametersTypeSpecifier node) {
		// TODO Auto-generated method stub
		super.outAFunctionTypeSpecifierNoParametersTypeSpecifier(node);

		TypeValueInterface result = (TypeValueInterface) parsingTable.getValueOf(node.getResult());
		FunctionTypeValue type = new FunctionTypeValue(result);
		parsingTable.putValue(node, type);
	}

	@Override
	public void outAFunctionTypeSpecifierWithParametersTypeSpecifier(
			AFunctionTypeSpecifierWithParametersTypeSpecifier node) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		super.outAArgument(node);

		TypeValueInterface type = (TypeValueInterface) parsingTable.getValueOf(node.getTypeSpecifier());
		ArgumentValue identifier = new ArgumentValue(node.toString(), node.getIdentifier().getText(), type);
		parsingTable.putValue(node, identifier);
	}

	@Override
	public void outADeclarationFunctionDefinition(ADeclarationFunctionDefinition node) {
		// TODO Auto-generated method stub
		super.outADeclarationFunctionDefinition(node);

		String functionIdentifier = node.getFunctionIdentifier().getText();
		TypeValueInterface returnType = (TypeValueInterface) parsingTable.getValueOf(node.getReturnType());

		FunctionValue function = new FunctionValue(functionIdentifier, returnType);
		global.putValue(functionIdentifier, function);
	}

	@Override
	public void outADeclarationWithArgumentFunctionDefinition(ADeclarationWithArgumentFunctionDefinition node) {
		// TODO Auto-generated method stub
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

		FunctionValue function = new FunctionValue(functionIdentifier, returnType, arguments);
		global.putValue(functionIdentifier, function);
	}

	@Override
	public void outABodyGuardFunctionDefinition(ABodyGuardFunctionDefinition node) {
		// TODO Auto-generated method stub
		super.outABodyGuardFunctionDefinition(node);

		String functionIdentifier = node.getFunctionIdentifier().getText();
		FunctionValue function = (FunctionValue) global.getValueOf(functionIdentifier);
		ExpressionValueInterface condition = (ExpressionValueInterface) parsingTable.getValueOf(node.getCondition());

		ExpressionValueInterface result = (ExpressionValueInterface) parsingTable.getValueOf(node.getResult());

		function.addExpression(condition, result);
	}

	@Override
	public void outABodyFunctionDefinition(ABodyFunctionDefinition node) {
		// TODO Auto-generated method stub
		super.outABodyFunctionDefinition(node);

		String functionIdentifier = node.getFunctionIdentifier().getText();
		FunctionValue function = (FunctionValue) global.getValueOf(functionIdentifier);
		NullaryExpressionValue condition = new NullaryExpressionValue("Tautology", new BooleanValue("True", true));

		ExpressionValueInterface result = (ExpressionValueInterface) parsingTable.getValueOf(node.getResult());

		function.addExpression(condition, result);
	}

	@Override
	public void outAFunctionApplicationWithArgumentExpression(AFunctionApplicationWithArgumentExpression node) {
		// TODO Auto-generated method stub
		super.outAFunctionApplicationWithArgumentExpression(node);

		String functionIdentifier = node.getFunctionIdentifier().getText();
		FunctionCall call = new FunctionCall(functionIdentifier);
		ValueInterface value = parsingTable.getValueOf(node.getFirst());
		LinkedList<ValueInterface> expressions = new LinkedList<>();
		expressions.add(value);

		for (PExpression nodeExpression : node.getRest()) {
			value = parsingTable.getValueOf(nodeExpression);
			expressions.add(value);
		}

		FunctionExpressionValue functionExpression = new FunctionExpressionValue(functionIdentifier, call, expressions);
		parsingTable.putValue(node, functionExpression);
	}

	@Override
	public void outAFunctionApplicationWithoutArgumentExpression(AFunctionApplicationWithoutArgumentExpression node) {
		// TODO Auto-generated method stub
		super.outAFunctionApplicationWithoutArgumentExpression(node);

		String functionIdentifier = node.getFunctionIdentifier().getText();
		FunctionValue function = (FunctionValue) global.getValueOf(functionIdentifier);

		ValueInterface result = function.evaluate(global);
		parsingTable.putValue(node, result);
	}

	@Override
	public void outAExpressionLineLine(AExpressionLineLine node) {
		// TODO Auto-generated method stub
		super.outAExpressionLineLine(node);

		ValueInterface value = parsingTable.getValueOf(node.getExpression());
		System.out.println("Resultado: " + value.evaluate(global));
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
