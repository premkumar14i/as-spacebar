package com.tibco.as.spacebar.ui.wizards.space.def;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import com.tibco.as.util.Property;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.model.Space;

public class SpaceDefEditor extends Composite {

	private DataBindingContext bindingContext;
	private Space space;

	public SpaceDefEditor(Composite parent, int style, Space newSpace) {
		super(parent, style);
		space = newSpace;
		bindingContext = new DataBindingContext();
		setLayout(new GridLayout());
		TabFolder folder = new TabFolder(this, SWT.NONE);
		Map<Property, List<Property>> properties = SpaceBarPlugin.getDefault()
				.getSpaceDefProperties();
		for (Entry<Property, List<Property>> entry : properties.entrySet()) {
			Property label = entry.getKey();
			if ("LabelGeneral".equals(label.getId())) {
				continue;
			}
			String displayName = label.getDisplayName().getValue();
			Composite composite = createTab(folder, displayName);
			for (Property property : entry.getValue()) {
				createControl(composite, property);
			}
		}
	}

	private void createControl(Composite parent, Property property) {
		IObservableValue observeValue = PojoObservables.observeValue(space,
				getPropertyName(property.getId()));
		IObservableValue observeWidget = getObserveWidget(parent, property);
		bindingContext.bindValue(observeWidget, observeValue, null, null);
	}

	private String getPropertyName(String id) {
		if ("TTL".equals(id)) {
			return id;
		}
		return Character.toLowerCase(id.charAt(0)) + id.substring(1);
	}

	private IObservableValue getObserveWidget(Composite parent,
			Property property) {
		String type = property.getDataType().getValue();
		String displayName = property.getDisplayName().getValue();
		if ("boolean".equals(type)) {
			Button button = createButton(parent, displayName);
			return SWTObservables.observeSelection(button);
		} else {
			createLabel(parent, displayName);
			if ("string".equals(type) || "long".equals(type)
					|| "integer".equals(type)) {
				Text text = createText(parent);
				return SWTObservables.observeText(text, SWT.Modify);
			} else if ("enumeration".equals(type)) {
				Class<?> enumType = getEnumType(property
						.getEnumerationJavaClass().getValue());
				if (enumType != null) {
					ComboViewer viewer = createCombo(parent, enumType);
					return ViewersObservables.observeSingleSelection(viewer);
				}
			}
		}
		return null;
	}

	private Class<?> getEnumType(String value) {
		int pos = value.lastIndexOf(".");
		try {
			return Class.forName(value.substring(0, pos) + "$"
					+ value.substring(pos + 1));
		} catch (ClassNotFoundException e) {
			SpaceBarPlugin.logException(e);
			return null;
		}
	}

	private Button createButton(Composite group, String text) {
		Button button = new Button(group, SWT.CHECK | SWT.LEFT);
		button.setText(text);
		GridDataFactory.defaultsFor(button).span(2, 1).applyTo(button);
		return button;
	}

	private ComboViewer createCombo(Composite group, Class<?> type) {
		ComboViewer viewer = new ComboViewer(group, SWT.READ_ONLY);
		viewer.setContentProvider(ArrayContentProvider.getInstance());
		viewer.setInput(type.getEnumConstants());
		Combo combo = viewer.getCombo();
		GridDataFactory.defaultsFor(combo).applyTo(combo);
		return viewer;
	}

	private void createLabel(Composite group, String text) {
		new Label(group, SWT.NONE).setText(text);
	}

	private Text createText(Composite group) {
		Text text = new Text(group, SWT.BORDER | SWT.SINGLE);
		GridDataFactory.defaultsFor(text).applyTo(text);
		return text;
	}

	private Composite createTab(TabFolder folder, String text) {
		TabItem item = new TabItem(folder, SWT.NONE);
		item.setText(text);
		Composite composite = new Composite(folder, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
		item.setControl(composite);
		return composite;
	}

}
