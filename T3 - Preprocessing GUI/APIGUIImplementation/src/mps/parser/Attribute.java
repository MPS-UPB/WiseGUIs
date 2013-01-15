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

	public Attribute() {

		super();
		use = "required";
	}

	/**
	 * @return the use
	 */
	public String getUse() {

		return use;
	}

	/**
	 * @param use
	 *            the use to set
	 */
	public void setUse(String use) {

		this.use = use;
	}

	@Override
	public String toString() {

		return "<pre>   " + getName() + " " + getValue() + "</pre>";
	}

	@Override
	public Attribute copy() {

		Attribute copy = new Attribute();
		copy.setName(this.getName());
		copy.setValue(this.getValue());
		copy.setBaseType(this.getBaseType());
		copy.setRestrictions(this.getRestrictions());
		copy.setUse(this.use);

		return copy;
	}
}
