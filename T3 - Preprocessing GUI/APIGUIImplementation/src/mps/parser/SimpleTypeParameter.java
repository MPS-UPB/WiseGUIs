/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mps.parser;

/**
 * Clasa care descrie un parametru de tip simplu, care este si tipul cel mai general.
 */
public class SimpleTypeParameter {

	/**
	 * Numele tag-ului asociat parametrului.
	 */
	private String name;
	/**
	 * Tipul de baza (xs:...), luat direct din declaratia elementului sau din
	 * restrictia de tip asociata tipului
	 */
	private String baseType;
	/**
	 * Lista de restrictii asociate tipului parametrului.
	 */
	private SimpleTypeRestriction restrictions;
	/**
	 * Valoarea efectiva a parametrului.
	 */
	private String value;

	public SimpleTypeParameter() {

		value = null;
		restrictions = new SimpleTypeRestriction();
	}

	public SimpleTypeParameter(String name) {

		this.name = name;
		value = null;
		restrictions = new SimpleTypeRestriction();
	}

	public SimpleTypeParameter(String name, String baseType) {
		this(name);
		this.baseType = baseType;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
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
	 * @param baseType
	 *            the baseType to set
	 */
	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}

	/**
	 * @return the restrictions
	 */
	public SimpleTypeRestriction getRestrictions() {
		return restrictions;
	}

	/**
	 * @param restrictions
	 *            the restrictions to set
	 */
	public void setRestrictions(SimpleTypeRestriction restrictions) {
		this.restrictions = restrictions;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value
	 *            the value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object obj) {

		SimpleTypeParameter newParam = (SimpleTypeParameter) obj;

		if (!this.name.equals(newParam.getName()))
			return false;
		if (this.value == null && newParam.getValue() != null
				|| this.value != null && newParam.getValue() == null)
			return false;
		if (this.value == null && newParam.getValue() == null)
			return true;
		return this.value.equals(newParam.getValue());
	}

	public SimpleTypeParameter copy() {

		SimpleTypeParameter clone = new SimpleTypeParameter();
		clone.setName(this.getName());
		clone.setBaseType(this.getBaseType());
		clone.setRestrictions(this.getRestrictions());
		clone.setValue(this.getValue());

		return clone;
	}

	@Override
	public String toString() {

		return "<br>" + getName() + " " + getValue();
	}
}
