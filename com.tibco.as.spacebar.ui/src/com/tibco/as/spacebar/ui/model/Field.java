package com.tibco.as.spacebar.ui.model;

import com.tibco.as.space.FieldDef.FieldType;

public class Field extends Element implements Cloneable {

	private FieldType type;
	private boolean nullable;
	private boolean encrypted;

	public Field(SpaceFields fields, String name) {
		super(fields, name);
	}

	@Override
	public SpaceFields getParent() {
		return (SpaceFields) super.getParent();
	}

	public boolean isNullable() {
		return nullable;
	}

	public void setNullable(boolean nullable) {
		firePropertyChange("nullable", this.nullable, this.nullable = nullable);
	}

	public boolean isEncrypted() {
		return encrypted;
	}

	public void setEncrypted(boolean encrypted) {
		firePropertyChange("encrypted", this.encrypted,
				this.encrypted = encrypted);
	}

	public FieldType getType() {
		return type;
	}

	public void setType(FieldType type) {
		firePropertyChange("type", this.type, this.type = type);
	}

	public void copyTo(Field field) {
		super.copyTo(field);
		field.type = type;
		field.nullable = nullable;
		field.encrypted = encrypted;
	}

	@Override
	public Field clone() {
		Field field = new Field((SpaceFields) getParent(), getName());
		copyTo(field);
		return field;
	}

	public boolean isKey() {
		return getParent().getParent().getKeys().getChild(getName()) != null;
	}

	public boolean isDistribution() {
		return getParent().getParent().getDistribution().getChild(getName()) != null;
	}

}