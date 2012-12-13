/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mps.parser.implementation;

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
}
