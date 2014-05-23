package com.tibco.as.spacebar.ui.editor.continuous;

import java.beans.PropertyChangeListener;

import com.tibco.as.spacebar.ui.editor.continuous.coder.TupleHashCoder;

import com.tibco.as.convert.IConverter;
import com.tibco.as.space.Tuple;

public class TupleObservableConverter implements
		IConverter<Tuple, ObservableTuple> {

	private TupleHashCoder coder;
	private PropertyChangeListener listener;

	public TupleObservableConverter(TupleHashCoder coder,
			PropertyChangeListener listener) {
		this.coder = coder;
		this.listener = listener;
	}

	@Override
	public ObservableTuple convert(Tuple tuple) {
		return new ObservableTuple(tuple, coder.getHashCode(tuple), listener);
	}
}
