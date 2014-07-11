package com.tibco.as.spacebar.ui.model;

import com.tibco.as.space.FieldDef.FieldType;

public class Field extends AbstractElement implements Cloneable {

	private Fields fields;
	private String name;
	private FieldType type;
	private boolean nullable;
	private boolean encrypted;
	private boolean key;
	private boolean distribution;

	public Field(Fields fields) {
		this.fields = fields;
	}

	public boolean isKey() {
		return key;
	}

	public void setKey(boolean key) {
		firePropertyChange("key", this.key, this.key = key);
	}

	public boolean isDistribution() {
		return distribution;
	}

	public void setDistribution(boolean distribution) {
		firePropertyChange("distribution", this.distribution,
				this.distribution = distribution);
	}

	@Override
	public Fields getParent() {
		return fields;
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
		field.name = name;
		field.type = type;
		field.nullable = nullable;
		field.encrypted = encrypted;
		field.key = key;
		field.distribution = distribution;
	}

	@Override
	public Field clone() {
		Field field = new Field(fields);
		copyTo(field);
		return field;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		fireNameChange(this.name, this.name = name);
	}

}