package com.tibco.as.spacebar.ui.editor.snapshot;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.preferences.Preferences;

import com.tibco.as.space.ASException;
import com.tibco.as.space.Tuple;

public class SaveJob extends Job {

	private SnapshotBrowser editor;

	public SaveJob(String name, SnapshotBrowser editor) {
		super(name);
		setUser(true);
		this.editor = editor;
	}

	protected IStatus run(IProgressMonitor monitor) {
		Stack<Change> changeList = editor.getChangeList();
		monitor.beginTask("Saving", changeList.size());
		com.tibco.as.space.Space space;
		try {
			try {
				space = editor.getSpace().getSpace();
			} catch (ASException e) {
				return SpaceBarPlugin.createStatus(e,
						"Could not get space ''{0}''", editor.getSpace());
			}
			try {
				Long timeout = Preferences
						.getLong(Preferences.IMPORT_WAIT_FOR_READY_TIMEOUT);
				monitor.subTask(MessageFormat.format(
						"Waiting {0} milliseconds for space to be ready",
						timeout));
				space.waitForReady(timeout);
				int batchSize = Preferences
						.getInteger(Preferences.IMPORT_BATCH_SIZE);
				List<Tuple> puts = new ArrayList<Tuple>(batchSize);
				List<Tuple> takes = new ArrayList<Tuple>(batchSize);
				while (!changeList.isEmpty()) {
					Change change = changeList.pop();
					if (monitor.isCanceled()) {
						return Status.CANCEL_STATUS;
					}
					Tuple tuple = change.getTuple();
					switch (change.getType()) {
					case PUT:
						if (!takes.isEmpty()) {
							space.takeAll(takes);
							takes.clear();
						}
						puts.add(tuple);
						if (puts.size() >= batchSize) {
							space.putAll(puts);
							puts.clear();
						}
						break;
					case TAKE:
						if (!puts.isEmpty()) {
							space.putAll(puts);
							puts.clear();
						}
						takes.add(tuple);
						if (takes.size() >= batchSize) {
							space.takeAll(takes);
							takes.clear();
						}
						break;
					}
					monitor.worked(1);
				}
				if (!puts.isEmpty()) {
					space.putAll(puts);
				}
				if (!takes.isEmpty()) {
					space.takeAll(takes);
				}
				editor.setDirty(false);
				if (monitor.isCanceled()) {
					return Status.CANCEL_STATUS;
				}
				return Status.OK_STATUS;
			} catch (com.tibco.as.space.ASException e) {
				return SpaceBarPlugin.createStatus(e, "Could not save");
			} finally {
				try {
					space.close();
				} catch (ASException e) {
					return SpaceBarPlugin.createStatus(e,
							"Could not close space");
				}
			}
		} finally {
			monitor.done();
		}
	}

}