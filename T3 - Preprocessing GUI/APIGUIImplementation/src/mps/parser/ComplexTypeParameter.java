/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mps.parser;

import java.util.ArrayList;

/**
 *
 * @author Liz
 */
public class ComplexTypeParameter extends SimpleTypeParameter {

    public ComplexTypeParameter(){
        super();
        attributes = new ArrayList<Attribute>();
    }
    
    /**
     * Lista de atribute asociata tipului complex al parametrului.
     */
    private ArrayList<Attribute> attributes;

    /**
     * @return the attributes
     */
    public ArrayList<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(ArrayList<Attribute> attributes) {
        this.attributes.addAll(attributes);
    }
    
    public Attribute getAttribute(String name) {
        
        for (Attribute attr : attributes) {
            
            if (attr.getName().equals(name))
                return attr;
        }
        
        return null;
    }
    
     public void setAttribute(String name, String value) {
        
         System.out.println(value);
         
        for (Attribute attr : attributes) {
            
            if (attr.getName().equals(name)) {
                attr.setValue(value);
                System.out.println("seteaza");
            }
        }
    }
     
      @Override
    public boolean equals(Object obj) {

        //verific sa fie toate chestiile egale (nume, tip de baza, restrictii...)
        //s-ar putea sa arunce NullPointerException, daca n-are completate valorile...
        
        if (!super.equals(obj)) 
            return false;
          
        ComplexTypeParameter newParam = (ComplexTypeParameter)obj;  

        for (int i = 0 ; i < attributes.size(); i++) {

           if (!this.attributes.get(i).equals(newParam.getAttributes().get(i)))
               return false;
        }      
                
        return true;
    }
}
