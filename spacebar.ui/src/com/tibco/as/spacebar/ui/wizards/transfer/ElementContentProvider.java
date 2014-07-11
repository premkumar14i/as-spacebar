package com.tibco.as.spacebar.ui.wizards.transfer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.tibco.as.spacebar.ui.model.IElement;

/**
 * The content provider for the tree viewer
 * 
 */
public class ElementContentProvider implements ITreeContentProvider {

	private Class<? extends IElement> type;
	private Class<? extends IElement> parentType;

	public ElementContentProvider(Class<? extends IElement> type,
			Class<? extends IElement> parentType) {
		this.type = type;
		this.parentType = parentType;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		if (parentElement instanceof IElement) {
			IElement element = (IElement) parentElement;
			List<IElement> children = getElements(element);
			return children.toArray();
		}
		if (parentElement instanceof List<?>) {
			return ((List<?>) parentElement).toArray();
		}
		return new Object[0];
	}

	private List<IElement> getElements(IElement element) {
		List<IElement> children = new ArrayList<IElement>();
		for (IElement child : element.getChildren()) {
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
		if (element instanceof IElement) {
			IElement parent = ((IElement) element).getParent();
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
