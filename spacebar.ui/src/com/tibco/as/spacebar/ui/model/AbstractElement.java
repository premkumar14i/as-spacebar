package com.tibco.as.spacebar.ui.model;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.list.IListChangeListener;
import org.eclipse.core.databinding.observable.list.ListChangeEvent;
import org.eclipse.core.databinding.observable.list.ListDiffEntry;
import org.eclipse.core.runtime.PlatformObject;

public abstract class AbstractElement extends PlatformObject implements
		IElement {

	private PropertyChangeSupport changeSupport = new PropertyChangeSupport(
			this);

	protected void fireNameChange(String oldName, String newName) {
		firePropertyChange("name", oldName, newName);
	}

	protected void fireChildrenChange(List<?> oldList, List<?> newList) {
		firePropertyChange("children", oldList, newList);
	}

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
		}
		copyChild(existing, child);
		return true;
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
		IElement existing = getChild(name);
		if (existing == null) {
			return false;
		}
		List<IElement> oldValue = new ArrayList<IElement>(getChildren());
		if (remove(existing)) {
			fireChildrenChange(oldValue, getChildren());
			return true;
		}
		return false;
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

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public void addListener(final IModelListener listener) {
		BeanProperties.list("children").observe(this)
				.addListChangeListener(new IListChangeListener() {

					@Override
					public void handleListChange(ListChangeEvent event) {
						for (ListDiffEntry entry : event.diff.getDifferences()) {
							IElement element = (IElement) entry.getElement();
							if (entry.isAddition()) {
								element.addListener(listener);
								listener.added(element);
							} else {
								listener.removed(element);
							}
						}
					}
				});
		changeSupport.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				listener.changed(AbstractElement.this, evt.getPropertyName(),
						evt.getOldValue(), evt.getNewValue());
			}
		});
		for (IElement element : getChildren()) {
			element.addListener(listener);
		}
	}
}
