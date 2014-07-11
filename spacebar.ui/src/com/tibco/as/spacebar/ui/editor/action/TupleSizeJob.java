package com.tibco.as.spacebar.ui.editor.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;

import com.tibco.as.space.ASException;
import com.tibco.as.space.Tuple;

public class TupleSizeJob extends Job {

	private List<Tuple> tuples;
	private int index = 0;
	private long totalSize = 0;

	private Collection<ITupleSizeListener> listeners = new ArrayList<ITupleSizeListener>();

	public TupleSizeJob(List<Tuple> tuples) {
		super("Tuple size");
		setUser(true);
		setSystem(false);
		this.tuples = tuples;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		for (Tuple tuple : tuples) {
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
			try {
				byte[] serialize = tuple.serialize();
				index++;
				totalSize += serialize.length;
				if (index % 1000 == 0) {
					for (ITupleSizeListener listener : listeners) {
						listener.tupleSize(getSize());
					}
				}
			} catch (ASException e) {
				return SpaceBarPlugin.createStatus(e,
						"Could not compute tuple size");
			}
		}
		for (ITupleSizeListener listener : listeners) {
			listener.tupleSize(getSize());
		}
		for (ITupleSizeListener listener : listeners) {
			listener.done();
		}
		if (monitor.isCanceled()) {
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;
	}

	private Long getSize() {
		if (index == 0) {
			return null;
		}
		return totalSize / index;
	}

	public void addListener(ITupleSizeListener listener) {
		listeners.add(listener);
	}
}
