package mps.parser.implementation;

import java.util.*;

import mps.GUI.window.implementation.Operation;

public class Parser {

	public static  List<Operation> getExecTypes(){
	
		List <Operation> ops = new ArrayList<Operation>();
		Operation op1 = new Operation(0,"rotate");
		Operation op2 = new Operation(0,"crop");
		Operation op3 = new Operation(1,"otsu1");
		Operation op4 = new Operation(1,"otsu2");
	
		op1.getParamsList().put("Angle", "JSpinner");
		op1.getParamsList().put("Some param ", "JComboBox");
		op1.getParamsList().put("Other param", "JTextField");
		op2.getParamsList().put("Angle", "JSpinner");
		op2.getParamsList().put("Some param ", "JComboBox");
		op2.getParamsList().put("Other param", "JTextField");
		op3.getParamsList().put("Angle", "JSpinner");
		op3.getParamsList().put("Some param ", "JComboBox");
		op3.getParamsList().put("Other param", "JTextField");
		op4.getParamsList().put("Angle", "JSpinner");
		op4.getParamsList().put("Some param ", "JComboBox");
		op4.getParamsList().put("Other param", "JTextField");
		ops.add(op1);
		ops.add(op2);
		ops.add(op3);
		ops.add(op4);
		return ops;
	}

}
