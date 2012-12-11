package mps.parser.implementation;
import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.*;

import java.io.File;
import java.io.IOException;
import java.util.*;


import org.xml.sax.SAXException;

import mps.GUI.window.implementation.Operation;

public class Parser {

	public static void parseXSDFile(String fileName){
		XSOMParser parser = new XSOMParser();
		parser.setErrorHandler(null);
		parser.setEntityResolver(null);
		XSType type = null;
		XSContentType xsContentType = null;
		List<String> list = new ArrayList<String>();
		try {
			parser.parse(new File(fileName));
			XSSchemaSet sset = parser.getResult();

			XSSchema s = (XSSchema) sset.getSchema(1);


			// cauta definitii de tipuri complexe
			Iterator<XSComplexType> ztr = s.iterateComplexTypes();
			while (ztr.hasNext()) {

				XSComplexType e = (XSComplexType) ztr.next();

				System.out.println("Tip complex : " + e.getName());
				
				 
				
			}

			// cauta tipuri simple
			Iterator<XSSimpleType> str = s.iterateSimpleTypes();
			while (str.hasNext()) {

				XSSimpleType e = (XSSimpleType) str.next();

				System.out.println("Tip simplu : " + e.getName());
			
			}

			// cauta definitii de elemente - de fapt e unul singur - task
			Iterator<XSElementDecl> jtr = s.iterateElementDecls();
			while (jtr.hasNext()) {
				XSElementDecl e = (XSElementDecl) jtr.next();
				System.out.println(e.getName());
				type = e.getType();
				xsContentType = type.asComplexType().getContentType();
				XSParticle xsParticle = xsContentType.asParticle();
				getOptionalElements(list, xsParticle);
			}

		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	

	private static void getOptionalElements(List <String> list, XSParticle xsParticle) {
		if (xsParticle != null) {
			XSTerm pterm = xsParticle.getTerm();
			// daca am gasit un element inner
			if (pterm.isElementDecl()) {

				System.out.println("---------------------------------");
				if (xsParticle.getMinOccurs() == 0) {
					System.out.println("elem cu minOccurs = 0 : "
							+ pterm.getSourceDocument().getTargetNamespace()
							+ ":" + pterm.asElementDecl().getName());
					/*list.add(pterm.getSourceDocument().getTargetNamespace()
							+ ":" + pterm.asElementDecl().getName());*/
				}

				
				System.out.println("Element Name : "
						+ pterm.asElementDecl().getName());
				System.out.println("Element Type : "
						+ pterm.asElementDecl().getType().getName());
				System.out.println("---------------------------------");

				XSComplexType xsComplexType = (pterm.asElementDecl()).getType()
						.asComplexType();

				
				//nu cred ca trebuie sa testam pentru enumeration si alte minuni
				//daca nu apar in exemplele date
				/*
				if (xsComplexType != null
						&& !(pterm.asElementDecl().getType()).toString()
								.contains("Enumeration")) {

					XSContentType xsContentType = xsComplexType
							.getContentType();

					XSParticle xsParticleInside = xsContentType.asParticle();
					getOptionalElements(list, xsParticleInside);
				}
*/
				
				
			}
			
			else if (pterm.isModelGroup()) {

				XSModelGroup xsModelGroup2 = pterm.asModelGroup();
				XSParticle[] xsParticleArray = xsModelGroup2.getChildren();
				for (XSParticle xsParticleTemp : xsParticleArray) {
					getOptionalElements(list, xsParticleTemp);
				}
			}
		}
	}
	
	
	
	
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
	
	public static void main(String []args){
		parseXSDFile("crop.xsd");
	}

}
