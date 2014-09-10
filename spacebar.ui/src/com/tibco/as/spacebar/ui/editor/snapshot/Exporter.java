package com.tibco.as.spacebar.ui.editor.snapshot;

import com.tibco.as.convert.ConvertException;
import com.tibco.as.convert.IConverter;
import com.tibco.as.convert.UnsupportedConversionException;
import com.tibco.as.io.AbstractExporter;
import com.tibco.as.io.AbstractTransfer;
import com.tibco.as.io.IOutputStream;
import com.tibco.as.io.TransferException;
import com.tibco.as.space.Metaspace;
import com.tibco.as.space.SpaceDef;
import com.tibco.as.space.Tuple;
import com.tibco.as.spacebar.ui.editor.Export;

public class Exporter extends AbstractExporter<Tuple> {

	public Exporter(Metaspace metaspace) {
		super(metaspace);
	}

	@Override
	protected IConverter<Tuple, Tuple> getConverter(
			com.tibco.as.io.AbstractTransfer transfer, SpaceDef spaceDef)
			throws UnsupportedConversionException {
		return new IConverter<Tuple, Tuple>() {

			@Override
			public Tuple convert(Tuple source) throws ConvertException {
				return source;
			}

		};
	}

	@Override
	protected Export createTransfer() {
		return new Export();
	}

	@Override
	protected IOutputStream<Tuple> getOutputStream(Metaspace metaspace,
			AbstractTransfer transfer, SpaceDef spaceDef)
			throws TransferException {
		return null;
	}

}
