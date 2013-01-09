package mps.parser;

//import com.sun.xml.internal.ws.message.saaj.SAAJHeader;
import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.*;

import java.io.File;
import java.io.IOException;
import java.util.*;

import mps.parser.SimpleTypeRestriction;

import org.xml.sax.SAXException;

public class Parser {

    public static Operation parseXSDFile(String fileName) {
    	Operation operation = new Operation();
    	Map<String,SimpleTypeRestriction>tipuriSimple = new HashMap<>();
    	Map<String,ComplexTypeParameter>tipuriComplexe = new HashMap<>();
        XSOMParser parser = new XSOMParser();
       
        XSType type = null;
        XSContentType xsContentType = null;
       
        try {
            parser.parse(new File(fileName));
            XSSchemaSet sset = parser.getResult();

            XSSchema s = (XSSchema) sset.getSchema(1);


            // cauta definitii de tipuri complexe
            Iterator<XSComplexType> ztr = s.iterateComplexTypes();
            while (ztr.hasNext()) {
                XSComplexType e = (XSComplexType) ztr.next();
                ComplexTypeParameter ctp = getAttributesForComplex(e);
                ctp.setName(e.getName());
                tipuriComplexe.put(ctp.getName(),ctp);
            }

            // cauta tipuri simple
            Iterator<XSSimpleType> str = s.iterateSimpleTypes();
            while (str.hasNext()) {

                XSSimpleType e = (XSSimpleType) str.next();
                SimpleTypeRestriction simpleRestriction = new SimpleTypeRestriction();
                initRestrictions(e, simpleRestriction);
                registerType(tipuriSimple,e.getName(),simpleRestriction);
            }

            // cauta definitii de elemente - de fapt e unul singur - task
            Iterator<XSElementDecl> jtr = s.iterateElementDecls();
            while (jtr.hasNext()) {
                XSElementDecl e = (XSElementDecl) jtr.next();
                operation.setRootElement(e.getName());
                type = e.getType();
                xsContentType = type.asComplexType().getContentType();
                XSParticle xsParticle = xsContentType.asParticle();
                getOptionalElements(operation,tipuriComplexe, xsParticle);
            }

        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

         
        operation.setName(tipuriSimple.get(operation.tagForExecName).pattern);
        operation.setToolTip(tipuriSimple.get(operation.tagForExecDescription).pattern);
        if(tipuriSimple.get(operation.tagForExecType).pattern.equals("binarization")){
        	operation.setType(1);
        }
        else{ 
        	operation.setType(0);
        }
        
        for (SimpleTypeParameter stp : operation.getParameters()){
            
        	if(!(stp instanceof ComplexTypeParameter) && tipuriSimple.containsKey(stp.getBaseType())){
        		stp.setBaseType(tipuriSimple.get(stp.getBaseType()).baseType);
        	}
        }
     
       
        return operation;
    }

    private static void registerType(Map<String, SimpleTypeRestriction> tipuri,
			String name, SimpleTypeRestriction simpleRestriction) {
		tipuri.put(name,simpleRestriction);
		
	}

    //intoarce un tip complex complet - in care apar atributele definite
	private static ComplexTypeParameter getAttributesForComplex(
			XSComplexType xsComplexType) {

		ComplexTypeParameter ctp = new ComplexTypeParameter();

		Collection<? extends XSAttributeUse> c = xsComplexType
				.getAttributeUses();
		Iterator<? extends XSAttributeUse> i = c.iterator();
		while (i.hasNext()) {

			Attribute a = new Attribute();
			XSAttributeDecl attributeDecl = i.next().getDecl();
			a.setBaseType(attributeDecl.getType().getName());
			a.setName(attributeDecl.getName());
			ctp.getAttributes().add(a);
		}
		return ctp;

	}

    private static void initRestrictions(XSSimpleType xsSimpleType, SimpleTypeRestriction t) {
        XSRestrictionSimpleType restriction = xsSimpleType.asRestriction();
        if (restriction != null) {
            Vector<String> enumeration = new Vector<String>();
            Iterator<? extends XSFacet> i = restriction.getDeclaredFacets().iterator();
            while (i.hasNext()) {
                XSFacet facet = i.next();
                if (facet.getName().equals(XSFacet.FACET_ENUMERATION)) {
                    enumeration.add(facet.getValue().value);
                }
                if (facet.getName().equals(XSFacet.FACET_MAXINCLUSIVE)) {
                    t.maxValue = facet.getValue().value;
                }
                if (facet.getName().equals(XSFacet.FACET_MININCLUSIVE)) {
                    t.minValue = facet.getValue().value;
                }
                if (facet.getName().equals(XSFacet.FACET_MAXEXCLUSIVE)) {
                    t.maxValue = String.valueOf(Integer.parseInt(facet.getValue().value) - 1);
                }
                if (facet.getName().equals(XSFacet.FACET_MINEXCLUSIVE)) {
                    t.minValue = String.valueOf(Integer.parseInt(facet.getValue().value) + 1);
                }
                if (facet.getName().equals(XSFacet.FACET_LENGTH)) {
                    t.length = facet.getValue().value;
                }
                if (facet.getName().equals(XSFacet.FACET_MAXLENGTH)) {
                    t.maxLength = facet.getValue().value;
                }
                if (facet.getName().equals(XSFacet.FACET_MINLENGTH)) {
                    t.minLength = facet.getValue().value;
                }
                if (facet.getName().equals(XSFacet.FACET_PATTERN)) {
                    t.pattern = facet.getValue().value;
                }
                if (facet.getName().equals(XSFacet.FACET_TOTALDIGITS)) {
                    t.totalDigits = facet.getValue().value;
                }
               
            }
            if (enumeration.size() > 0) {
                t.enumeration = enumeration.toArray(new String[]{});
            }
            t.baseType = restriction.getBaseType().getName();
          //  System.out.println(xsSimpleType.getName()+" BASE :"+restriction.getBaseType().getName());
        }
        
    }
/*
    private static void getAttributesForSimple(XSSimpleType xsSimpleType) {
        Collection<? extends XSAttributeUse> c = ((XSAttContainer) xsSimpleType).getAttributeUses();
        Iterator<? extends XSAttributeUse> i = c.iterator();
        while (i.hasNext()) {
            XSAttributeDecl attributeDecl = i.next().getDecl();
        //    System.out.println("\ttype: " + attributeDecl.getType());
         //   System.out.println("\tname:" + attributeDecl.getName());
        }
    }
*/
    private static void getOptionalElements(Operation operation,Map<String,ComplexTypeParameter>tipuriComplexe, XSParticle xsParticle) {
        if (xsParticle != null) {
            XSTerm pterm = xsParticle.getTerm();
            // daca am gasit un element inner
  
            if (pterm.isElementDecl()) {

            	
            	if(pterm.asElementDecl().getName().equals("execInfo")){
					getOptionalElements(operation,tipuriComplexe, pterm.asElementDecl()
							.getType().asComplexType().getContentType()
							.asParticle());
					return;
            	}
            	
            	if(pterm.asElementDecl().getName().equals("name")){
            		operation.tagForExecName = pterm.asElementDecl().getType().getName();
            		
				//	System.out.println("unde caut numele exec :"+pterm.asElementDecl().getType().getName());
					return;
            	}
            	if(pterm.asElementDecl().getName().equals("type")){
            		operation.tagForExecType = pterm.asElementDecl().getType().getName();
				//	System.out.println("unde caut tipul exec :"+pterm.asElementDecl().getType().getName());
					return;
            	}
            	
            	if(pterm.asElementDecl().getName().equals("description")){
            		operation.tagForExecDescription = pterm.asElementDecl().getType().getName();
				//	System.out.println("unde caut desscrierea exec :"+pterm.asElementDecl().getType().getName());
					return;
            	}
             
               

                SimpleTypeParameter stp = null;
                if(tipuriComplexe.containsKey( pterm.asElementDecl()
                                                    .getType().getName())){
                     stp = createComplexParameter(pterm.asElementDecl()
                                                    .getType().getName(),tipuriComplexe);
                     stp.setName(pterm.asElementDecl().getName());
                }else{
                     stp = createSimpleParameter(pterm
                                            .asElementDecl().getName(), pterm.asElementDecl()
                                                    .getType().getName());
                }
		operation.getParameters().add(stp);
           
                XSComplexType xsComplexType = (pterm.asElementDecl()).getType()
                        .asComplexType();


                


            } else if (pterm.isModelGroup()) {

            	
                XSModelGroup xsModelGroup2 = pterm.asModelGroup();
                XSParticle[] xsParticleArray = xsModelGroup2.getChildren();
                for (XSParticle xsParticleTemp : xsParticleArray) {
                    getOptionalElements(operation,tipuriComplexe, xsParticleTemp);
                }
            }
        }
    }

    private static SimpleTypeParameter createSimpleParameter(String name,
			String type) {
    	return new SimpleTypeParameter(name, type);
		
	}

	public static List<Operation> getExecTypes() {

        List<Operation> ops = new ArrayList<Operation>();
        Operation op1 = parseXSDFile("crop.xsd");
        Operation op2 = parseXSDFile("rotate.xsd");
        Operation op3 = parseXSDFile("otsu.xsd");
        ops.add(op1);
        ops.add(op2);
        ops.add(op3);
        return ops;
    }

    private static SimpleTypeParameter createComplexParameter(String name, 
            Map<String,ComplexTypeParameter> tipuriComplexe) {
        
        ComplexTypeParameter stp = new ComplexTypeParameter();
        ArrayList<Attribute> attributes = new ArrayList<Attribute>();
        for(Attribute a : tipuriComplexe.get(name).getAttributes()){
            Attribute b = new Attribute();
            b.setBaseType(a.getBaseType());
            b.setName(a.getName());
            b.setRestrictions(a.getRestrictions());
            b.setUse(a.getUse());
            b.setValue(a.getValue());
            attributes.add(b);
        }
        stp.setAttributes(attributes);
        return stp;
    }
}
