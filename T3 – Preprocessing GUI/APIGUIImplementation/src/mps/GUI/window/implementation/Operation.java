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
    
    String generalType;
    String name;
    LinkedHashMap<String, String> paramsList;
    LinkedHashMap<String, String> paramsValues;
    
    public Operation() {
        
        paramsList = new LinkedHashMap<String,String>();
        paramsValues = new LinkedHashMap<String,String>();
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
    
    @Override
    public Operation clone() {
        
        Operation newOp = new Operation();  
        
        newOp.generalType = this.generalType;
        newOp.name = this.name;
        newOp.paramsList.putAll(this.paramsList);
        newOp.paramsValues.putAll(this.paramsValues);
        
        return newOp;
    }
}
