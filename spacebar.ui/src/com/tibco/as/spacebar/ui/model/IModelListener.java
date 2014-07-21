package com.tibco.as.spacebar.ui.model;

public interface IModelListener {

	void added(IElement element);

	void removed(IElement element);

	void changed(IElement element, String propertyName, Object oldValue,
			Object newValue);

}
