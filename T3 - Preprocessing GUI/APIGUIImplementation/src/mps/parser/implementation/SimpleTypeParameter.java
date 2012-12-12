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

public class SimpleTypeParameter {
    
    /**
     * Numele tag-ului asociat parametrului.
     */
    private String name;
    /**
     * Tipul de baza (xs:...), luat direct din declaratia elementului sau din restrictia de tip asociata tipului
     */
    private String baseType;   
    /**
     * Lista de restrictii asociate tipului parametrului.
     */
    private ArrayList<SimpleTypeRestriction> restrictions;
    /**
     * Valoarea efectiva a parametrului.
     */
    private String value;
    
    SimpleTypeParameter() {
        
        value = null;
    }    

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the baseType
     */
    public String getBaseType() {
        return baseType;
    }

    /**
     * @param baseType the baseType to set
     */
    public void setBaseType(String baseType) {
        this.baseType = baseType;
    }

    /**
     * @return the restrictions
     */
    public ArrayList<SimpleTypeRestriction> getRestrictions() {
        return restrictions;
    }

    /**
     * @param restrictions the restrictions to set
     */
    public void setRestrictions(ArrayList<SimpleTypeRestriction> restrictions) {
        this.restrictions.addAll(restrictions);
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(Object name) {
        
        return this.name == (String)name;
    }
    
    public SimpleTypeParameter copy() {
        
        SimpleTypeParameter clone = new SimpleTypeParameter();
        clone.setName(this.getName());
        clone.setBaseType(this.getBaseType());
        clone.setRestrictions(this.getRestrictions());
        
        return clone;
    }
}
