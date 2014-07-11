package com.tibco.as.spacebar.ui.wizards.space;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.tibco.as.spacebar.ui.model.IElement;

public abstract class AbstractEditElementWizardPage<T extends IElement> extends
		WizardPage implements PropertyChangeListener {

	private T edited;
	private T original;

	public AbstractEditElementWizardPage(String pageName, T original, T edited) {
		super(pageName);
		this.original = original;
		this.edited = edited;
	}

	@Override
	public void createControl(Composite parent) {
		setControl(getControl(parent, edited));
		edited.addPropertyChangeListener("name", this);
		validate();
	}

	protected abstract Control getControl(Composite parent, T edited);

	protected T getEdited() {
		return edited;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		validate();
	}

	protected void validate() {
		String name = edited.getName();
		if (name == null || name.trim().length() == 0) {
			setErrorMessage(getEmptyNameMessage(edited));
			setPageComplete(false);
		} else {
			IElement existing = edited.getParent().getChild(name);
			if (existing != null && existing != original) {
				setErrorMessage(getExistsMessage(edited));
				setPageComplete(false);
			} else {
				setErrorMessage(null);
				setPageComplete(true);
			}
		}
	}

	protected abstract String getEmptyNameMessage(T element);

	protected abstract String getExistsMessage(T element);

}