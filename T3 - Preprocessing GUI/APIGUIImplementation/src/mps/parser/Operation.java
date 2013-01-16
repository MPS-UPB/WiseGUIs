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

import mps.GUI.MainWindow;

/**
 * 
 * @author Liz
 */
public class Operation {

	/**
	 * Camp static care da id-urile opratiilor, in mod unic.
	 */
	public static int opGlobalId = 0;

	/**
	 * Id-ul operatiei, dupa care aceasta poate fi identificata unic.
	 */
	private int id;

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
	private String desc;
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
	 * Calea catre folder-ul in care se vor depozita XML-urile.
	 */
	private String XMLFolder = "XMLs";

	// date pentru parser
	String tagForExecName = null;
	String tagForExecType = null;
	String tagForExecDescription = null;

	public Operation() {

		// Initializari
		parameters = new ArrayList<SimpleTypeParameter>();
		description = new LinkedHashMap<String, String>();
	}

	public Operation(int tip, String nume) {

		this();
		type = tip;
		name = nume;
	}


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

		// numele sa fie fara extensie
		String delims = "\\.";
		String[] tokens = name.split(delims);
		this.name = tokens[0];
	}

	/**
	 * @return the description
	 */
	public String getDesc() {
		return desc;
	}

	/**
	 * @param toolTip
	 *            setarea textului pentru descriere
	 */
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * @return the parameters
	 */
	public ArrayList<SimpleTypeParameter> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            the parameters to set
	 */
	public void setParameters(ArrayList<SimpleTypeParameter> parameters) {
		this.parameters.clear();

		for (SimpleTypeParameter param : parameters) {

			this.parameters.add(param.copy());
		}
	}

	/**
	 * @return the rootElement
	 */
	public String getRootElement() {
		return rootElement;
	}

	/**
	 * @param rootElement
	 *            the rootElement to set
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
	 * @param rootDescription
	 *            the rootDescription to set
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
	 * @param description
	 *            the description to set
	 */
	public void setDescription(LinkedHashMap<String, String> description) {

		this.description.clear();
		this.description.putAll(description);
	}

	/**
	 * @return the XMLFolder
	 */
	public String getXMLFolder() {
		return XMLFolder;
	}

	/**
	 * @param XMLFolder
	 *            the XMLFolder to set
	 */
	public void setXMLFolder(String XMLFolder) {
		this.XMLFolder = XMLFolder;
	}
	
	/**
	 * Metoda care adauga un parametru in lista de parametri ai operatiei.
	 * @param param parametrul de adaugat
	 */
	public void addParameter(SimpleTypeParameter param) {

		parameters.add(param);
	}
	
	/**
	 * Metoda care intoarce valoare unui parametru al operatiei
	 * @param paramName numele parametrului 
	 * @return valoarea parametrului
	 */
	public SimpleTypeParameter getParameter(String paramName) {

		for (SimpleTypeParameter param : parameters) {

			if (param.getName().equals(paramName))
				return param;
		}

		return null;
	}

	/**
	 * Seteaza valoarea unui parametru al operatiei
	 * @param paramName numele parametrului
	 * @param value valoarea noua
	 */
	public void setParameter(String paramName, String value) {

		//Se cauta parametrul dupa nume, in lista de parametri
		for (SimpleTypeParameter param : parameters) {

			if (param.getName().equals(paramName))
				param.setValue(value);
		}
	}

	/**
	 * Metoda care executa programul, pe imaginea originara, cu un anumit set de
	 * parametri.
	 * 
	 * @return intoarce calea catre fisierul rezultat 
	 */
	public String execute() {

		String delims = "\\.";
		String[] tokens = ((ComplexTypeParameter) getParameter("inputFile"))
				.getAttribute("name").getValue().split(delims);

		String outputPath;

		// adaugare parametru fisier de iesire
		if (type == 0) {

			outputPath = ((ComplexTypeParameter) getParameter("inputFile"))
					.getAttribute("name").getValue();

		} // output in fisere distincte, pentru executabilele de binarizare
		else {

			outputPath = tokens[0] + "_binariz_output" + id + ".tiff"; // "." +
																		// tokens[1];
		}
		((ComplexTypeParameter) getParameter("outputFile")).setAttribute(
				"name", outputPath);
		String localXMLPath = generateXML();

		try {

			String thisExecPath = MainWindow.execPath + "\\" + getName()
					+ ".exe";

			// Lansare in executie
			
			// Definire proces
			ProcessBuilder exec = new ProcessBuilder(thisExecPath, localXMLPath);
			// Lansare proces
			Process proc = exec.start();
			// Asteapta sa se termine executia programului
			proc.waitFor();

			// Stergere a fisierului XML generat (nu mai este necesar)
			new File(localXMLPath).delete();
			
		} catch (IOException ex) {
			Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null,
					ex);
		} catch (InterruptedException ex) {
		}

		return outputPath;
	}

	/**
	 * Metoda care genereaza un fisier XML cu toti parametrii specifici unui
	 * executabil.
	 * 
	 * @return calea catre fisierul XML generat 
	 */
	public String generateXML() {

		// Fisierul XML se va genera intr-un folder separat de cel cu executabilele
		String thisXMLPath = getXMLFolder() + "\\" + getName() + id + ".xml";

		// Se ia fiecare parametru si se trece in tag-uri
		File file = new File(thisXMLPath);

		try {
			file.createNewFile();
		} catch (IOException ex) {
			Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null,
					ex);
		}

		BufferedWriter writer;
		try {

			writer = new BufferedWriter(new FileWriter(file));

			// Se urmareste structura documentului

			// Tag-ul task
			writer.write("<" + getRootElement() + ">");

			writer.newLine();
			writer.write('\t');
			writer.write("<" + getRootDescription() + ">");

			// Descrierea executabilului
			for (Map.Entry<String, String> entry : getDescription().entrySet()) {

				writer.newLine();
				writer.write('\t');
				writer.write('\t');
				writer.write("<" + entry.getKey() + ">" + entry.getValue()
						+ "</" + entry.getKey() + ">");
			}

			writer.newLine();
			writer.write("</" + getRootDescription() + ">");

			// Lista de parametri

			for (SimpleTypeParameter param : getParameters()) {

				// Daca elementul e de tip simplu
				if (!(param instanceof ComplexTypeParameter)) {

					writer.newLine();
					writer.write('\t');
					writer.write("<" + param.getName() + ">");
					writer.write(param.getValue());
					writer.write("</" + param.getName() + ">");
				} 
				// Daca este element de tip complex
				else {

					writer.newLine();
					writer.write('\t');

					ComplexTypeParameter complexParam = (ComplexTypeParameter) param;

					// Tag-ul

					writer.write("<" + complexParam.getName());

					// Atributele

					ArrayList<Attribute> attributes = complexParam
							.getAttributes();

					for (Attribute attribute : attributes) {

						/* 
						 * Daca valoarea atributului nu este nula, inseamna ca:
						 * 1. este required
						 * 2. nu este required, dar valoarea lui a fost completata in fereastra de parametri
						 */
						if (attribute.getValue() != null) {
							writer.write(" " + attribute.getName() + "=" + "\""
									+ attribute.getValue() + "\"");
						}
					}

					writer.write(">");

					/*
					 *  Se scrie valoarea efectiva a parametrului;
					 * daca tag-ul e de tip empty, atunci inseamna ca valoarea e null si nu se afiseaza nimic
					 */

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
			Logger.getLogger(Operation.class.getName()).log(Level.SEVERE, null,
					ex);
		}

		return thisXMLPath;
	}

	/**
	 * Metoda care genereaza o copie a instantei Operation curente.
	 * 
	 * @return o noua instanta Operation, avand campuri cu aceleasi valori ca
	 *         ale instantei curente
	 */
	public Operation copy() {

		Operation newOp = new Operation();

		// Copiere valori campuri
		newOp.setType(this.getType());
		newOp.setName(this.getName());
		newOp.setDesc(this.getDesc());
		newOp.setXMLFolder(this.getXMLFolder());
		newOp.setDescription(this.getDescription());
		newOp.setParameters(this.getParameters());
		newOp.setRootDescription(this.getRootDescription());
		newOp.setRootElement(this.getRootElement());

		return newOp;
	}

	@Override
	public String toString() {

		String concat = new String();

		concat += "<html>";

		for (int i = 2; i < parameters.size(); i++) {

			concat += parameters.get(i).toString();
		}

		concat += "</html>";

		return concat;
	}
	

	/**
	 * Metoda folosita pentru afisarea tool tip-urilor in Main Window.
	 * Deosebirea fata de metoda toString() este ca se afiseaza si numele executabilului.
	 * @return
	 */
	public String toString2() {

		String concat = new String();

		concat += "<html>" + getName() + "<br>";

		for (int i = 2; i < parameters.size(); i++) {

			concat += parameters.get(i).toString();
		}

		concat += "</html>";

		return concat;
	}	

	@Override
	public boolean equals(Object obj) {

		Operation newOp = (Operation) obj;

		if (!(this.name.equals(newOp.name)))
			return false;

		for (int i = 0; i < parameters.size(); i++) {

			if (!this.parameters.get(i).equals(newOp.getParameters().get(i)))
				return false;
		}

		return true;
	}
}
