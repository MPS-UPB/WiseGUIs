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
}
