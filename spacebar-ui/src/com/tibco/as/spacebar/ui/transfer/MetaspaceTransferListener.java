package com.tibco.as.spacebar.ui.transfer;

import java.util.Collection;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;

import com.tibco.as.io.IInputStream;
import com.tibco.as.io.IMetaspaceTransferListener;
import com.tibco.as.io.ITransfer;

public class MetaspaceTransferListener implements IMetaspaceTransferListener {

	private final static int PROGRESS_UNIT_FACTOR = 100;

	private IProgressMonitor monitor;

	private String taskName;

	public MetaspaceTransferListener(IProgressMonitor monitor, String taskName) {
		this.monitor = monitor;
		this.taskName = taskName;
	}

	@Override
	public void opening(Collection<ITransfer> transfers) {
		monitor.beginTask(taskName, transfers.size() * PROGRESS_UNIT_FACTOR);
	}

	@Override
	public void executing(ITransfer transfer) {
		long size = transfer.size();
		if (size == IInputStream.UNKNOWN_SIZE) {
			monitor.beginTask(taskName, IProgressMonitor.UNKNOWN);
			return;
		}
		SubProgressMonitor subMonitor = new SubProgressMonitor(monitor,
				PROGRESS_UNIT_FACTOR);
		subMonitor.beginTask(transfer.toString(), (int) size);
		transfer.addListener(new TransferListener(transfer, subMonitor));
	}

}
