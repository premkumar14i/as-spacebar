package com.tibco.as.spacebar.ui.wizards.transfer;

import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.PlatformObject;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.ui.model.IWorkbenchAdapter;

/**
 * Tree content provider for objects that can be adapted to the interface
 * {@link org.eclipse.ui.model.IWorkbenchAdapter IWorkbenchAdapter}.
 * <p>
 * This class may be instantiated, or subclassed.
 * </p>
 * 
 * @see IWorkbenchAdapter
 */
public class BaseWorkbenchContentProvider implements ITreeContentProvider {

	/**
	 * Creates a new workbench content provider.
	 * 
	 */
	public BaseWorkbenchContentProvider() {
		super();
	}

	@Override
	public void dispose() {
		// do nothing
	}

	/**
	 * Returns the implementation of IWorkbenchAdapter for the given object.
	 * Returns null if the adapter is not defined or the object is not
	 * adaptable.
	 * <p>
	 * </p>
	 * 
	 * @param element
	 *            the element
	 * @return the corresponding workbench adapter object
	 */
	protected IWorkbenchAdapter getAdapter(Object element) {
		return (IWorkbenchAdapter) getAdapter(element, IWorkbenchAdapter.class);
	}

	private static Object getAdapter(Object sourceObject, Class<?> adapterType) {
		Assert.isNotNull(adapterType);
		if (sourceObject == null) {
			return null;
		}
		if (adapterType.isInstance(sourceObject)) {
			return sourceObject;
		}

		if (sourceObject instanceof IAdaptable) {
			IAdaptable adaptable = (IAdaptable) sourceObject;

			Object result = adaptable.getAdapter(adapterType);
			if (result != null) {
				// Sanity-check
				Assert.isTrue(adapterType.isInstance(result));
				return result;
			}
		}

		if (!(sourceObject instanceof PlatformObject)) {
			Object result = Platform.getAdapterManager().getAdapter(
					sourceObject, adapterType);
			if (result != null) {
				return result;
			}
		}

		return null;
	}

	@Override
	public Object[] getChildren(Object element) {
		IWorkbenchAdapter adapter = getAdapter(element);
		if (adapter != null) {
			return adapter.getChildren(element);
		}
		return new Object[0];
	}

	@Override
	public Object[] getElements(Object element) {
		return getChildren(element);
	}

	@Override
	public Object getParent(Object element) {
		IWorkbenchAdapter adapter = getAdapter(element);
		if (adapter != null) {
			return adapter.getParent(element);
		}
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		return getChildren(element).length > 0;
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// do nothing
	}

}
