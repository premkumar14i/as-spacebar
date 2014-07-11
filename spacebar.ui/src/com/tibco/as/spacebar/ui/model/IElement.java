package com.tibco.as.spacebar.ui.model;

import java.beans.PropertyChangeListener;
import java.util.List;

public interface IElement {

	String getName();

	IElement getParent();

	List<? extends IElement> getChildren();

	IElement getChild(String name);

	IElement getChild(IElement child);

	boolean addChild(IElement child);

	boolean removeChild(String name);

	boolean removeChild(IElement element);

	void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener);

}
