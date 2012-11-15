package mps.PGUI.parser;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ParserXSD {
	

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
		parse(new File("rotate.xsd"));
		
		//un map cu valori pentru gui : ex <unghi,JSpinner>
	}
	
}
	
