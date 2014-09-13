package com.tibco.as.spacebar.ui.editor.continuous;

import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Collection;

import com.tibco.as.convert.UnsupportedConversionException;
import com.tibco.as.io.AbstractExporter;
import com.tibco.as.io.AbstractTransfer;
import com.tibco.as.io.IOutputStream;
import com.tibco.as.io.TransferException;
import com.tibco.as.space.FieldDef;
import com.tibco.as.space.Metaspace;
import com.tibco.as.space.SpaceDef;
import com.tibco.as.spacebar.ui.editor.SpaceEditorExport;
import com.tibco.as.spacebar.ui.editor.continuous.coder.BlobHashCoder;
import com.tibco.as.spacebar.ui.editor.continuous.coder.FieldHashCoder;
import com.tibco.as.spacebar.ui.editor.continuous.coder.IFieldHashCoder;
import com.tibco.as.spacebar.ui.editor.continuous.coder.TupleHashCoder;

public class Exporter extends AbstractExporter<ObservableTuple> {

	private PropertyChangeListener listener;

	public Exporter(Metaspace metaspace, PropertyChangeListener listener) {
		super(metaspace);
		this.listener = listener;
	}

	@Override
	protected SpaceEditorExport createTransfer() {
		return new SpaceEditorExport();
	}

	@Override
	protected IOutputStream<ObservableTuple> getOutputStream(
			Metaspace metaspace, AbstractTransfer transfer, SpaceDef spaceDef)
			throws TransferException {
		return null;
	}

	@Override
	protected TupleObservableConverter getConverter(AbstractTransfer transfer,
			SpaceDef spaceDef) throws UnsupportedConversionException {
		Collection<String> keys = spaceDef.getKeyDef().getFieldNames();
		TupleHashCoder coder = getHashCoder(spaceDef, keys);
		return new TupleObservableConverter(coder, listener);
	}

	private TupleHashCoder getHashCoder(SpaceDef spaceDef,
			Collection<String> keys) {
		Collection<IFieldHashCoder> coders = new ArrayList<IFieldHashCoder>();
		for (String key : keys) {
			coders.add(getFieldHashCoder(spaceDef.getFieldDef(key)));
		}
		return new TupleHashCoder(coders);
	}

	private IFieldHashCoder getFieldHashCoder(FieldDef fieldDef) {
		String fieldName = fieldDef.getName();
		switch (fieldDef.getType()) {
		case BLOB:
			return new BlobHashCoder(fieldName);
		default:
			return new FieldHashCoder(fieldName);
		}
	}

}
