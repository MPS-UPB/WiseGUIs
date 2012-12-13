/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mps.parser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.sun.xml.xsom.*;
import mps.parser.SimpleTypeParameter;
import mps.parser.ComplexTypeParameter;
import java.io.FileOutputStream;

/**
 *
 * @author Liz
 */
public class Operation {
    /**
     * Tipul general al executabilului: de procesare sau de binarizare. 0 pentru
     * preprocesare sau 1 pentru binarizare
     */
    private int type;
    /**
     * Numele executabilului.
     */
    private String name;
    /**
     * Descrierea executabilului.
     */
    private String toolTip;
    //!!!!!!!!!!!!!!!!!!!!!!!!!s
    //nu e foarte eficient sa tinem toate informatiile despre un xsd in fiecare operatie
    //mai bine am avea un tip abstract de operatie
    //si mai multe tipuri mici, care vor face referire la tipul abstract
    //desi s-ar putea sa fie tot aia pana la urma...
    /**
     * Lista de parametri asociata operatiei.
     */
    private ArrayList<SimpleTypeParameter> parameters;
    /**
     * Tag-ul de baza.
     */
    private String rootElement = "task";
    /**
     * Tagul de baza al sectiunii de descriere executabil.
     */
    private String rootDescription = "execInfo";
    /**
     * Tag-urile si valorile asociate pentru descrierea executabilului.
     */
    private LinkedHashMap<String, String> description;
    /**
     * Calea catre executabil.
     */
    private String execFolder = "execs";
    /**
     * Calea catre folder-ul in care se vor depozita XML-urile.
     */
    private String XMLFolder = "XMLs";

   //date pentru parser 
    String tagForExecName = null;
    String tagForExecType = null;
    String tagForExecDescription = null;
    
    
    public Operation() {

        //Initializari
        parameters = new ArrayList<SimpleTypeParameter>();
        description = new LinkedHashMap<String, String>();
    }

    public Operation(int tip, String nume) {

        this();
        type = tip;
        name = nume;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
       
        //numele sa fie fara extensie
        String delims = "\\.";       
        String[] tokens = name.split(delims);
        this.name = tokens[0];
    }

    /**
     * @return the toolTip
     */
    public String getToolTip() {
        return toolTip;
    }

    /**
     * @param toolTip the toolTip to set
     */
    public void setToolTip(String toolTip) {
        this.toolTip = toolTip;
    }

    /**
     * @return the parameters
     */
    public ArrayList<SimpleTypeParameter> getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(ArrayList<SimpleTypeParameter> parameters) {
        this.parameters.addAll(parameters);
    }

    /**
     * @return the rootElement
     */
    public String getRootElement() {
        return rootElement;
    }

    /**
     * @param rootElement the rootElement to set
     */
    public void setRootElement(String rootElement) {
        this.rootElement = rootElement;
    }

    /**
     * @return the rootDescription
     */
    public String getRootDescription() {
        return rootDescription;
    }

    /**
     * @param rootDescription the rootDescription to set
     */
    public void setRootDescription(String rootDescription) {
        this.rootDescription = rootDescription;
    }

    /**
     * @return the description
     */
    public LinkedHashMap<String, String> getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(LinkedHashMap<String, String> description) {
        this.description.putAll(description);
    }

    /**
     * @return the execFolder
     */
    public String getExecFolder() {
        return execFolder;
    }

    /**
     * @param execFolder the execFolder to set
     */
    public void setExecFolder(String execFolder) {
        this.execFolder = execFolder;
    }

    /**
     * @return the XMLFolder
     */
    public String getXMLFolder() {
        return XMLFolder;
    }

    /**
     * @param XMLFolder the XMLFolder to set
     */
    public void setXMLFolder(String XMLFolder) {
        this.XMLFolder = XMLFolder;
    }

    /**
     * Metoda care executa programul, pe imaginea originara, cu un anumit set de
     * parametri.
     *
     * @return intoarce fisierul rezultat (TODO)
     */
    public String execute() {

        String delims = "\\.";       
        String[] tokens = ((ComplexTypeParameter)getParameter("inputFile")).getAttribute("name").getValue().split(delims);
   //       System.out.println(getParameter("inputFile").getValue() + " " + tokens.length);
        
        String outputPath;

        //adaugare parametru fisier de iesire
        if (type == 0) {

            outputPath = tokens[0] + "_preproc_output" + ".tiff"; //+ tokens[1];

        } //output in fisere dictincte, pentru executabilele de binarizare
        else {

            outputPath = tokens[0] + "_binariz_output" + hash() + ".tiff"; // "." + tokens[1];
        }
        ((ComplexTypeParameter)getParameter("outputFile")).setAttribute("name", outputPath);
        String localXMLPath = generateXML();

        try {

            //mai bine ar fi fost sa se citeasca executabilele din folder si apoi sa se dea calea absoluta ca param
            String thisExecPath = getExecFolder() + "\\" + getName() + ".exe";

            //lansare in executie
            //defineste proces
            ProcessBuilder exec = new ProcessBuilder(thisExecPath, localXMLPath);
            //lanseaza proces
            Process proc = exec.start();
            //asteapta sa se termine executia programului
            //nu e prea eficient; poate fi optimizat ulterior, din main window:
            //se apeleaza execute() pe fiecare operatie, care doar lanseaza in executie programele
            //apoi, inainte de a afisa imeginile rezultat in partea dreapta, se verifica daca procesul s-a terminat (tot cu waitFor() apelat pe undeva)
            //se tine o referinta la proces in clasa Operatie
            //inainte de a afisa imaginile rezultat se parcurge vectorul de operatii lansate in executie si se verifica daca procesul respectiv s-a incheiat, apelandu-se waitFor()
            proc.waitFor();

            //sterg fisierul XML generat anterior
           // new File(localXMLPath).delete();
            //pot sa fac clear la directorul cu XML-uri, cand ies din aplicatie; sau pot sa le pastrez ca un fel de log

        } catch (IOException ex) {
            Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
        }

        return outputPath;
    }

    /**
     * Metoda care genereaza un fisier XML cu toti parametrii specifici unui
     * executabil.
     *
     * @return fisierul XML generat (TODO)
     */
    public String generateXML() {

        String inputFileAux = ((ComplexTypeParameter)getParameter("inputFile")).getAttribute("name").getValue();
        String outputFileAux = ((ComplexTypeParameter)getParameter("outputFile")).getAttribute("name").getValue();
        
        System.out.println(inputFileAux);
        System.out.println(outputFileAux);
        
        ((ComplexTypeParameter)getParameter("inputFile")).getAttribute("name").setValue(
               "\"" + ((ComplexTypeParameter)getParameter("inputFile")).getAttribute("name").getValue() + "\"");
        
        ((ComplexTypeParameter)getParameter("outputFile")).getAttribute("name").setValue(
               '\"' + ((ComplexTypeParameter)getParameter("outputFile")).getAttribute("name").getValue() +  '\"' );        
        
        //fisierul XML se va genera intr-un folder separat de cel cu executabilele
        String thisXMLPath = getXMLFolder() + "\\" + getName() + hash() + ".xml";
        
        System.out.println("-----------GENERARE XML---------");
        System.out.println(thisXMLPath);
        System.out.println(this);

        //iau fiecare parametru si trec in tag-uri
        File file = new File(thisXMLPath);
        
        try {
            file.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        BufferedWriter writer;
        try {

            writer = new BufferedWriter(new FileWriter(file));

            //urmez structura documentului

            //tag-ul task
            writer.write("<" + getRootElement() + ">");

            /*
            writer.newLine();
            writer.write('\t');
            writer.write("<" + getRootDescription() + ">");

            //descrierea executabilului
            for (Map.Entry<String, String> entry : getDescription().entrySet()) {

                writer.newLine();
                writer.write('\t');
                writer.write('\t');
                writer.write("<" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">");
            }

*           writer.newLine();
            writer.write("</" + getRootDescription() + ">");
            */
            
            //lista de parametri

            for (SimpleTypeParameter param : getParameters()) {

                //daca elementul e de tip simplu
                if (!(param instanceof ComplexTypeParameter)) {

                    writer.newLine();
                    writer.write('\t');
                    writer.write("<" + param.getName() + ">");
                    writer.write(param.getValue());
                    writer.write("</" + param.getName() + ">");
                } //daca este element de tip complex
                else {

                    System.out.println("ALELUIA");
                    writer.newLine();
                    writer.write('\t');
                    
                    ComplexTypeParameter complexParam = (ComplexTypeParameter) param;

                    //tag-ul

                    writer.write("<" + complexParam.getName());

                    //atribute

                    ArrayList<Attribute> attributes = complexParam.getAttributes();

                    for (Attribute attribute : attributes) {

                         System.out.println("ceva");
                        //daca valoarea atributului nu este nula, inseamna ca:
                        //1. este required
                        //2. nu este required, dar valoarea lui a fost completata in fereastra de parametri
                        if (attribute.getValue() != null) {
                            writer.write(" " + attribute.getName() + "=" + attribute.getValue());
                        }
                    }

                    writer.write(">");

                    //se scrie valoarea efectiva a parametrului
                    //daca tag-ul e de tip empty, atunci valoarea ramane null
                    //elementele de tip simplu nu pot fi empty (cred) !!!!!!!!!!!!!!!!!

                    //!!!!!!!!!!!!!!!
                    //specificare inputFile si outputFile:
                    //acesti parametri sunt tratati la fel ca toti ceilalti
                    //ei reprezinta tag-uri vide, deci valorile lor vor ramane null                

                    if (complexParam.getValue() != null) {

                        writer.write(complexParam.getValue());
                    }

                    writer.write("</" + complexParam.getName() + ">");
                }
            }

            writer.newLine();
            writer.write("</" + getRootElement() + ">");
            
            writer.close();

        } catch (IOException ex) {
            Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null, ex);
        }
        
         ((ComplexTypeParameter)getParameter("inputFile")).getAttribute("name").setValue(inputFileAux);
         ((ComplexTypeParameter)getParameter("outputFile")).getAttribute("name").setValue(outputFileAux);

        return thisXMLPath;
    }

    /**
     * Metoda care genereaza o copie a instantei Operation curente.
     *
     * @return o noua instanta Operation, avand campuri cu aceleasi valori ca
     * ale instantei curente
     */
    public Operation copy() {

        Operation newOp = new Operation();

        //copiere valori campuri
        newOp.setType(this.getType());
        newOp.setName(this.getName());
        newOp.setToolTip(this.getToolTip());
        newOp.setExecFolder(this.getExecFolder());
        newOp.setXMLFolder(this.getXMLFolder());
        newOp.setDescription(this.getDescription());
        newOp.setParameters(this.getParameters());
        newOp.setRootDescription(this.getRootDescription());
        newOp.setRootElement(this.getRootElement());

        return newOp;
    }

    public int hash() {

        String concat = new String();

        for (SimpleTypeParameter param : parameters) {

            if (param.getValue() != null) {
                concat += param.getValue();
            }

            if (param instanceof ComplexTypeParameter) {

                ComplexTypeParameter complexParam = (ComplexTypeParameter) param;
                for (Attribute attribute : complexParam.getAttributes()) {

                    concat += attribute.getValue();
                }
            }
        }

        return concat.hashCode();
    }

    @Override
    public String toString() {

        String concat = new String();

        concat += getName();
        concat += "\n";

        for (SimpleTypeParameter param : parameters) {

            concat += param.toString() + "\n";
        }

        return concat;
    }
    
    public SimpleTypeParameter getParameter(String paramName) {
        
        for (SimpleTypeParameter param : parameters) {
            
            if (param.getName().equals(paramName))
                return param;
        }
        
        return null;
    }
    
     public void setParameter(String paramName, String value) {
        
        for (SimpleTypeParameter param : parameters) {
            
            if (param.getName().equals(paramName))
                param.setValue(value);
        }
    }
     
       @Override
    public boolean equals(Object obj) {
        
        Operation newOp = (Operation)obj;
        
        if (!(this.name.equals(newOp.name)))
            return false;
        
        for (int i = 0 ; i < parameters.size(); i++) {
            
            if (!this.parameters.get(i).equals(newOp.getParameters().get(i)))
                return false;                            
        }
        
        return true;
    }
}
