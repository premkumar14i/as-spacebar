package com.tibco.as.spacebar.ui.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ColorFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;

public abstract class AbstractPreferencePage extends FieldEditorPreferencePage
		implements IWorkbenchPreferencePage {

	public AbstractPreferencePage() {
		super(GRID);
		setPreferenceStore(SpaceBarPlugin.getDefault().getPreferenceStore());
	}

	@Override
	public void init(IWorkbench workbench) {
	}

	@Override
	protected void createFieldEditors() {
		createFieldEditors(getFieldEditorParent());
	}

	protected abstract void createFieldEditors(Composite parent);

	protected void setToolTip(BooleanFieldEditor field, Composite parent,
			String toolTip) {
		field.getDescriptionControl(parent).setToolTipText(toolTip);
	}

	protected void setToolTip(StringFieldEditor field, Composite parent,
			String toolTip) {
		field.getLabelControl(parent).setToolTipText(toolTip);
		field.getTextControl(parent).setToolTipText(toolTip);
	}

	protected void setToolTip(ComboFieldEditor field, Composite parent,
			String toolTip) {
		field.getLabelControl(parent).setToolTipText(toolTip);
	}

	protected BooleanFieldEditor addBooleanField(String name, String label,
			Composite parent, String toolTip) {
		BooleanFieldEditor field = new BooleanFieldEditor(name, label, parent);
		setToolTip(field, parent, toolTip);
		addField(field);
		return field;
	}

	protected IntegerFieldEditor addIntegerField(String name, String label,
			Composite parent, String toolTip) {
		IntegerFieldEditor field = new IntegerFieldEditor(name, label, parent);
		setToolTip(field, parent, toolTip);
		addField(field);
		return field;
	}

	protected IntegerFieldEditor addIntegerField(String name, String label,
			Composite parent, String toolTip, int min, int max) {
		IntegerFieldEditor field = addIntegerField(name, label, parent, toolTip);
		field.setValidRange(min, max);
		return field;
	}

	protected ComboFieldEditor addComboField(String name, String labelText,
			String[][] entryNamesAndValues, Composite parent, String toolTip) {
		ComboFieldEditor field = new ComboFieldEditor(name, labelText,
				entryNamesAndValues, parent);
		setToolTip(field, parent, toolTip);
		addField(field);
		return field;
	}

	protected StringFieldEditor addCharField(String name, String label,
			Composite parent, String toolTip, boolean emptyStringAllowed) {
		StringFieldEditor field = new StringFieldEditor(name, label, parent);
		field.setTextLimit(1);
		field.setEmptyStringAllowed(emptyStringAllowed);
		setToolTip(field, parent, toolTip);
		addField(field);
		return field;
	}

	protected StringFieldEditor addStringField(String name, String labelText,
			Composite parent, String toolTip) {
		StringFieldEditor field = new StringFieldEditor(name, labelText, parent);
		setToolTip(field, parent, toolTip);
		addField(field);
		return field;
	}

	protected ColorFieldEditor addColorField(String name, String labelText,
			Composite parent, String toolTip) {
		ColorFieldEditor field = new ColorFieldEditor(name, labelText, parent);
		field.getLabelControl(parent).setToolTipText(toolTip);
		field.getColorSelector().getButton().setToolTipText(toolTip);
		addField(field);
		return field;
	}

}
