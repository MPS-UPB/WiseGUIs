package mps.parser.implementation;

import com.sun.xml.xsom.parser.XSOMParser;
import com.sun.xml.xsom.*;

import java.io.File;
import java.io.IOException;
import java.util.*;
import mps.parser.implementation.SimpleTypeRestriction;

import org.xml.sax.SAXException;

public class Parser {

    public static void parseXSDFile(String fileName) {
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
                System.out.println("--------------------");
                System.out.println("Tip complex : " + e.getName());
                getAttributesForComplex(e);

            }

            // cauta tipuri simple
            Iterator<XSSimpleType> str = s.iterateSimpleTypes();
            while (str.hasNext()) {

                XSSimpleType e = (XSSimpleType) str.next();
                SimpleTypeRestriction simpleRestriction = new SimpleTypeRestriction();

                initRestrictions(e, simpleRestriction);
                System.out.println("--------------------------");
                System.out.println("Tip simplu : " + e.getName());
                System.out.println(simpleRestriction.pattern);

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

    private static void getAttributesForComplex(XSComplexType xsComplexType) {
        Collection<? extends XSAttributeUse> c = xsComplexType.getAttributeUses();
        Iterator<? extends XSAttributeUse> i = c.iterator();
        while (i.hasNext()) {
            XSAttributeDecl attributeDecl = i.next().getDecl();
            System.out.println("\ttype: " + attributeDecl.getType());
            System.out.println("\tname:" + attributeDecl.getName());



        }
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
        }
    }

    private static void getAttributesForSimple(XSSimpleType xsSimpleType) {
        Collection<? extends XSAttributeUse> c = ((XSAttContainer) xsSimpleType).getAttributeUses();
        Iterator<? extends XSAttributeUse> i = c.iterator();
        while (i.hasNext()) {
            XSAttributeDecl attributeDecl = i.next().getDecl();
            System.out.println("\ttype: " + attributeDecl.getType());
            System.out.println("\tname:" + attributeDecl.getName());
        }
    }

    private static void getOptionalElements(List<String> list, XSParticle xsParticle) {
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


            } else if (pterm.isModelGroup()) {

                XSModelGroup xsModelGroup2 = pterm.asModelGroup();
                XSParticle[] xsParticleArray = xsModelGroup2.getChildren();
                for (XSParticle xsParticleTemp : xsParticleArray) {
                    getOptionalElements(list, xsParticleTemp);
                }
            }
        }
    }

    public static List<Operation> getExecTypes() {

        List<Operation> ops = new ArrayList<Operation>();
        Operation op1 = new Operation(0, "rotate");
        Operation op2 = new Operation(0, "crop");
        Operation op3 = new Operation(1, "otsu1");
        Operation op4 = new Operation(1, "otsu2");

        SimpleTypeParameter param0 = new SimpleTypeParameter();
        param0.setBaseType("string");
        param0.setName("inputFile");


        SimpleTypeParameter param1 = new SimpleTypeParameter();
        param1.setBaseType("string");
        param1.setName("outputFile");

        SimpleTypeParameter param2 = new SimpleTypeParameter();
        param2.setBaseType("positiveInteger");
        param2.setName("Intreg pozitiv");

        SimpleTypeParameter param3 = new SimpleTypeParameter();
        param3.setBaseType("float");
        param3.setName("Un float");
        SimpleTypeRestriction restr = new SimpleTypeRestriction();
        restr.maxValue = new Float(89.45).toString();
        param3.setRestrictions(restr);

        SimpleTypeParameter param4 = new SimpleTypeParameter();
        param4.setBaseType("string");
        param4.setName("Enumerare");
        SimpleTypeRestriction restr2 = new SimpleTypeRestriction();
        restr2.enumeration = new String[3];
        restr2.enumeration[0] = "ceva1";
        restr2.enumeration[1] = "ceva2";
        restr2.enumeration[2] = "ceva3";
        param4.setRestrictions(restr2);

         //definire rotate
        param2.setBaseType("float");
        param2.setName("angle");
        
        op1.getParameters().add(param0);
        op1.getParameters().add(param1);
        op1.getParameters().add(param2);
        
        op2.getParameters().add(param0);
        op2.getParameters().add(param1);
        op2.getParameters().add(param2);
        op2.getParameters().add(param3);
        op2.getParameters().add(param4);      
        
        op3.getParameters().add(param0);
        op3.getParameters().add(param1);
        op3.getParameters().add(param2);
        op3.getParameters().add(param3);
        op3.getParameters().add(param4);
        
        op4.getParameters().add(param0);
        op4.getParameters().add(param1);
        op4.getParameters().add(param2);
        op4.getParameters().add(param3);
        op4.getParameters().add(param4);

        ops.add(op1);
        ops.add(op2);
        ops.add(op3);
        ops.add(op4);
        return ops;
    }

    public static void main(String[] args) {
        parseXSDFile("crop.xsd");
    }
}
