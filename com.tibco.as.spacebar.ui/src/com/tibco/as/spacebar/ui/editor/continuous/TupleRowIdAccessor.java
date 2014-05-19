package com.tibco.as.spacebar.ui.editor.continuous;

import java.io.Serializable;

import org.eclipse.nebula.widgets.nattable.data.IRowIdAccessor;

public class TupleRowIdAccessor implements IRowIdAccessor<ObservableTuple> {

	@Override
	public Serializable getRowId(ObservableTuple rowObject) {
		return ((ObservableTuple) rowObject).getId();
	}
}
