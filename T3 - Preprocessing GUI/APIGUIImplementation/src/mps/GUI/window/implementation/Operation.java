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

    // 0 petru preprocesare sau 1 pentru binarizare
    private int type;
    private String name;
    private LinkedHashMap<String, String> paramsList;
    private LinkedHashMap<String, String> paramsValues;

    public Operation() {
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

    //intoarce fisierul rezultat
    public void execute() {
        //adaugare parametru fisier de iesire
        //generateXML();
        //lansare in executie
    }

    //intoarce fisierul XML
    public void generateXML() {
    }

    public Operation copy() {

        Operation newOp = new Operation();

        newOp.type = this.type;
        newOp.name = this.name;
        newOp.paramsList.putAll(this.paramsList);
        newOp.paramsValues.putAll(this.paramsValues);

        return newOp;
    }
}
