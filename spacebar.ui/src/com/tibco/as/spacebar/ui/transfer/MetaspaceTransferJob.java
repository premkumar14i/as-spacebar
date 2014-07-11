package com.tibco.as.spacebar.ui.transfer;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import com.tibco.as.spacebar.ui.SpaceBarPlugin;

import com.tibco.as.io.IMetaspaceTransfer;

public class MetaspaceTransferJob extends Job {

	private String taskName;

	private IMetaspaceTransfer transfer;

	private String errorMessage;

	public MetaspaceTransferJob(String name, String taskName,
			IMetaspaceTransfer transferer, String errorMessage) {
		super(name);
		setUser(true);
		this.taskName = taskName;
		this.transfer = transferer;
		this.errorMessage = errorMessage;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		transfer.addListener(new MetaspaceTransferListener(monitor, taskName));
		try {
			transfer.execute();
		} catch (Exception e) {
			return SpaceBarPlugin.createStatus(e, errorMessage);
		} finally {
			monitor.done();
		}
		if (transfer.isStopped()) {
			return Status.CANCEL_STATUS;
		}
		return Status.OK_STATUS;
	}

	@Override
	protected void canceling() {
		try {
			transfer.stop();
		} catch (Exception e) {
			SpaceBarPlugin.logException("Could not stop transfer", e);
		}
		super.canceling();
	}

}
