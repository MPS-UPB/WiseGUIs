package mps.PGUI.parser;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sun.xml.xsom.XSSchemaSet;
import com.sun.xml.xsom.parser.XSOMParser;

public class ParserXSD {
	

	public static void parseXSDFile(String fileName){
		XSOMParser parser = new XSOMParser();
		try {
			parser.parse( new File(fileName));
			XSSchemaSet sset = parser.getResult();
			
		/*	Iterator<XSSchema> itr = sset.iterateSchema();
			while( itr.hasNext() ) {
			  XSSchema s = (XSSchema)itr.next();

			  System.out.println("Target namespace: "+s.getTargetNamespace());

			  Iterator<XSElementDecl> jtr = s.iterateElementDecls();
			  while( jtr.hasNext() ) {
			    XSElementDecl e = (XSElementDecl)jtr.next();

			    System.out.print( e.getName() );
			    if( e.isAbstract() )
			      System.out.print(" (abstract)");
			    System.out.println();
			  }
			  
			}*/
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	public static void parse(File xmlFile){
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder;
		try {
			dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			
			NodeList nodeList =  doc.getDocumentElement().getChildNodes();
			System.out.println("nr nodes :"+nodeList.getLength());
			
			for(int  i = 0; i< nodeList.getLength();i++){
				if(nodeList.item(i).hasAttributes()){ 
					Node n = nodeList.item(i).getAttributes().item(0);
					System.out.println("child:"+n.getNodeValue());
					
					/*if(n.getNodeValue().equals("task")){
						NodeList in = n.getFirstChild().getChildNodes();
						System.out.println();
						for(int j = 0; j < in.getLength();j++){
							
							System.out.println(in.item(j).getNodeName());
						}
					}*/
					
				}
			}
			System.out.println("********************************");
			NodeList ni = doc.getElementsByTagName("xs:element");
			for(int i = 0; i< ni.getLength(); i++){
				if(ni.item(i).hasChildNodes()){ //&& ni.item(i).getAttributes().getNamedItem("xs:restriction") != null)
				    for(int k = 0; k< ni.item(i).getChildNodes().getLength();k++)
					System.out.println("\tR::"+ni.item(i).getChildNodes().item(k));//("xs:restriction").getAttributes().item(0));
				}
				NamedNodeMap attributes = ni.item(i).getAttributes();
				for(int j = 0; j<attributes.getLength();j++){
					System.out.println(i+"<"+attributes.item(j).getNodeName()+" "+attributes.item(j).getNodeValue()+">");
				}
			}
			
			System.out.println("-----------------------");

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	public static void main(String []args){
		//parse(new File("rotate.xsd"));
		parseXSDFile("rotate.xsd");
		//un map cu valori pentru gui : ex <unghi,JSpinner>
	}
	
}
	
