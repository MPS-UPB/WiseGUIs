package mps.parser.implementation;
import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.xml.namespace.NamespaceContext;

import org.xml.sax.SAXException;

import mps.GUI.window.implementation.Operation;

public class Parser {

	public static void parseXSDFile(String fileName){
		XSOMParser parser = new XSOMParser();
		parser.setErrorHandler(null);
		parser.setEntityResolver(null);
		try {
			parser.parse( new File(fileName));
			XSSchemaSet sset = parser.getResult();
			
			Iterator<XSSchema> itr = sset.iterateSchema();
			while( itr.hasNext() ) {
			  XSSchema s = (XSSchema)itr.next();

			  System.out.println("Target namespace: "+s.getTargetNamespace());

			  Iterator<XSElementDecl> jtr = s.iterateElementDecls();
			
			  while( jtr.hasNext() ) {
			    XSElementDecl e = (XSElementDecl)jtr.next();
			    System.out.println( e.getName());
			    System.out.println("copii >"+e.asElementDecl());
			   // XSElementDecl elem = e.getSubstAffiliation();
			    XSType xsComplexType = e.getType();
			   // XSContentType xscont = xsComplexType.getContentType();
			    //XSParticle particle = xscont.asParticle();
			    System.out.println("I? : "+xsComplexType);
		     /*   while(i.hasNext()){
		        	 XSAttributeDecl attributeDecl = i.next().getDecl(); 
		             System.out.println("type: "+attributeDecl.getType());
		             System.out.println("name:"+attributeDecl.getName());
		        	}
			  /* // System.out.println("\ncomplextype  -- "+e.getType().asComplexType());
			    XSComplexType xsComplexType = e.getType().asComplexType();
			    Iterator<? extends XSAttributeUse> i = xsComplexType.getAttributeUses().iterator();
		        while(i.hasNext()){
		        	 XSAttributeDecl attributeDecl = i.next().getDecl(); 
		             System.out.println("type: "+attributeDecl.getType());
		             System.out.println("name:"+attributeDecl.getName());
		        	}
			    */
			  }
			
			  Iterator<XSComplexType> ztr = s.iterateComplexTypes();
			    while( ztr.hasNext() ) {
			    	System.out.println(((XSComplexType)ztr.next()).getName());
			    }
			  
			}
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
