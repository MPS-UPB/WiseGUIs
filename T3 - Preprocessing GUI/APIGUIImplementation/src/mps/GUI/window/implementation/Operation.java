/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mps.GUI.window.implementation;

import java.util.LinkedHashMap;

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
     * Lista cu numele parametrilor si tipul asociat fiecaruia.
     */
    //atentie!! tipul e de fapt tipul grafic, adica ce element grafic au asociat (TextBox, Spinner etc.) - asa vine din parser; 
    //putem schimba, putem sa trecem tipuri de genul decimal, string etc, asa cum vin din xsd 
    //si sa la asociem elemente grafice aici
    private LinkedHashMap<String, String> paramsList;
    /**
     * Lista cu numele parametrilor si valoarea efectiva asociata fiecaruia.
     */
    private LinkedHashMap<String, String> paramsValues;

    public Operation() {

        //Initializari
        paramsList = new LinkedHashMap<String, String>();
        paramsValues = new LinkedHashMap<String, String>();
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
        this.name = name;
    }

    public LinkedHashMap<String, String> getParamsList() {
        return paramsList;
    }

    public void setParamsList(LinkedHashMap<String, String> paramsList) {
        this.paramsList = paramsList;
    }

    public LinkedHashMap<String, String> getParamsValues() {
        return paramsValues;
    }

    public void setParamsValues(LinkedHashMap<String, String> paramsValues) {
        this.paramsValues = paramsValues;
    }

    /**
     * Metoda care executa programul, pe imaginea originara, cu un anumit set de
     * parametri.
     *
     * @return intoarce fisierul rezultat (TODO)
     */
    public void execute() {
        //adaugare parametru fisier de iesire
        //generateXML();
        //lansare in executie
    }

    /**
     * Metoda care genereaza un fisier XML cu toti parametrii specifici unui
     * executabil.
     *
     * @return fisierul XML generat (TODO)
     */
    public void generateXML() {
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
        newOp.type = this.type;
        newOp.name = this.name;
        newOp.paramsList.putAll(this.paramsList);
        newOp.paramsValues.putAll(this.paramsValues);

        return newOp;
    }
}
