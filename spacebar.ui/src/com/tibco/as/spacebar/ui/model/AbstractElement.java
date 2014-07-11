package com.tibco.as.spacebar.ui.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.PlatformObject;

public abstract class AbstractElement extends PlatformObject implements
		IElement {

	protected void fireNameChange(String oldName, String newName) {
		firePropertyChange("name", oldName, newName);
	}

	protected void fireChildrenChange(List<?> oldList, List<?> newList) {
		firePropertyChange("children", oldList, newList);
	}

	public List<? extends IElement> getChildren() {
		return Collections.emptyList();
	}

	@Override
	public boolean addChild(IElement child) {
		IElement existing = getChild(child);
		if (existing == null) {
			List<IElement> oldValue = new ArrayList<IElement>(getChildren());
			if (add(child)) {
				fireChildrenChange(oldValue, getChildren());
				return true;
			}
			return false;
		} else {
			copyChild(existing, child);
			return true;
		}
	}

	protected boolean add(IElement child) {
		return false;
	}

	protected boolean remove(IElement child) {
		return false;
	}

	protected void copyChild(IElement source, IElement target) {
	}

	@Override
	public boolean removeChild(String name) {
		IElement child = getChild(name);
		if (child == null) {
			return false;
		}
		return remove(child);
	}

	@Override
	public boolean removeChild(IElement element) {
		return removeChild(element.getName());
	}

	@Override
	public IElement getChild(String name) {
		for (IElement child : getChildren()) {
			if (child.getName().equals(name)) {
				return child;
			}
		}
		return null;
	}

	@Override
	public IElement getChild(IElement child) {
		return getChild(child.getName());
	}

	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(
			this);

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		changeSupport.removePropertyChangeListener(listener);
	}

	@Override
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

	@Override
	public String toString() {
		return getName();
	}

}
