package com.tibco.as.spacebar.ui.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.PlatformObject;

public abstract class Element extends PlatformObject implements Cloneable {

	private Element parent;

	private String name;

	private List<Element> children = new ArrayList<Element>();

	public Element(Element parent, String name) {
		this.parent = parent;
		this.name = name;
	}

	public Element getParent() {
		return parent;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		firePropertyChange("name", this.name, this.name = name);
	}

	public List<Element> getChildren() {
		return children;
	}

	public void setChildren(List<Element> children) {
		List<Element> oldValue = new ArrayList<Element>(this.children);
		firePropertyChange("children", oldValue, this.children = children);
	}

	public void addChild(Element child) {
		Element existing = getChild(child.getName());
		if (existing == null) {
			List<Element> oldValue = new ArrayList<Element>(children);
			if (children.add(child)) {
				firePropertyChange("children", oldValue, children);
			}
		} else {
			existing.copyTo(child);
		}
	}

	protected void copyTo(Element element) {
		element.parent = parent;
		element.setName(name);
		element.setChildren(new ArrayList<Element>(children));
	}

	public void removeChild(String name) {
		for (Element child : new ArrayList<Element>(children)) {
			if (child.getName().equals(name)) {
				removeChild(child);
			}
		}
	}

	private void removeChild(Element child) {
		if (child == null) {
			return;
		}
		List<Element> oldValue = new ArrayList<Element>(children);
		if (children.remove(child)) {
			firePropertyChange("children", oldValue, children);
		}
	}

	public Element getChild(String name) {
		for (Element child : children) {
			if (child.getName().equals(name)) {
				return child;
			}
		}
		return null;
	}

	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(
			this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(propertyName, listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(propertyName, listener);
	}

	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		changeSupport.firePropertyChange(propertyName, oldValue, newValue);
	}

	public int getIndex() {
		return parent.getChildren().indexOf(this);
	}

}
