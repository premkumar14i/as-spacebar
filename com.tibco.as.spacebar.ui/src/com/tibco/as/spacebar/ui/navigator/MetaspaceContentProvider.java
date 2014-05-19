package com.tibco.as.spacebar.ui.navigator;

import org.eclipse.core.databinding.beans.BeanProperties;
import org.eclipse.core.databinding.observable.IObservable;
import org.eclipse.core.databinding.observable.masterdetail.IObservableFactory;
import org.eclipse.jface.databinding.viewers.ObservableListTreeContentProvider;
import org.eclipse.jface.databinding.viewers.TreeStructureAdvisor;

import com.tibco.as.spacebar.ui.model.Element;

public class MetaspaceContentProvider extends ObservableListTreeContentProvider {

	public MetaspaceContentProvider() {
		super(getObservableListFactory(), getTreeStructureAdvisor());
	}

	private static IObservableFactory getObservableListFactory() {
		return new IObservableFactory() {
			@Override
			public IObservable createObservable(Object parent) {
				if (parent instanceof Element) {
					return BeanProperties.list("children").observe(parent);
				}
				return null;
			}
		};
	}

	private static TreeStructureAdvisor getTreeStructureAdvisor() {
		return new TreeStructureAdvisor() {

			@Override
			public Object getParent(Object element) {
				if (element instanceof Element) {
					return ((Element) element).getParent();
				}
				return super.getParent(element);
			}

		};
	}
}
