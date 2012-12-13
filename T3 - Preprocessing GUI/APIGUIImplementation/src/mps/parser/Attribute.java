/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mps.parser;

/**
 *
 * @author Liz
 */
public class Attribute extends SimpleTypeParameter {

    /**
     * Specifica daca atributul este required sau optional.
     */
    private String use;

    /**
     * @return the use
     */
    public String getUse() {
        return use;
    }

    /**
     * @param use the use to set
     */
    public void setUse(String use) {
        this.use = use;
    }
}
