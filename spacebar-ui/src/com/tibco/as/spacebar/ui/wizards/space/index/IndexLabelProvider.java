package com.tibco.as.spacebar.ui.wizards.space.index;

import java.util.List;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import com.tibco.as.spacebar.ui.model.Field;
import com.tibco.as.spacebar.ui.model.Index;

/**
 * Label provider for templates.
 */
public class IndexLabelProvider extends LabelProvider implements
		ITableLabelProvider {

	public Image getColumnImage(Object element, int columnIndex) {
		return null;
	}

	public String getColumnText(Object element, int columnIndex) {
		Index index = (Index) element;
		switch (columnIndex) {
		case 0:
			return index.getName();
		case 1:
			return index.getType() == null ? null : index.getType().name();
		case 2:
			String label = "";
			List<Field> fields = index.getFields();
			for (int i = 0; i < fields.size(); i++) {
				if (i > 0) {
					label += ", ";
				}
				label += fields.get(i).getName();
			}
			return label;
		default:
			return ""; //$NON-NLS-1$
		}
	}
}