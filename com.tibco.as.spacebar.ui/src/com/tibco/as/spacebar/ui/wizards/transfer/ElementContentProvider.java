package com.tibco.as.spacebar.ui.wizards.transfer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.tibco.as.spacebar.ui.model.Element;

/**
 * The content provider for the tree viewer
 * 
 */
public class ElementContentProvider implements ITreeContentProvider {

	private Class<? extends Element> type;
	private Class<? extends Element> parentType;

	public ElementContentProvider(Class<? extends Element> type,
			Class<? extends Element> parentType) {
		this.type = type;
		this.parentType = parentType;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof Element) {
			Element element = (Element) parentElement;
			List<Element> children = getElements(element);
			return children.toArray();
		}
		if (parentElement instanceof List<?>) {
			return ((List<?>) parentElement).toArray();
		}
		return new Object[0];
	}

	private List<Element> getElements(Element element) {
		List<Element> children = new ArrayList<Element>();
		for (Element child : element.getChildren()) {
			if (type.isInstance(child)) {
				children.add(child);
			} else {
				children.addAll(getElements(child));
			}
		}
		return children;
	}

	@Override
	public Object getParent(Object element) {
		if (element instanceof Element) {
			Element parent = ((Element) element).getParent();
			if (parentType.isInstance(parent)) {
				return parent;
			}
			return getParent(parent);
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

}
