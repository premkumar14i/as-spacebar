package com.tibco.as.spacebar.ui.transfer;

import org.eclipse.core.runtime.IProgressMonitor;

import com.tibco.as.io.ITransfer;
import com.tibco.as.io.ITransferListener;

public class TransferListener implements ITransferListener {

	private ITransfer transfer;
	private IProgressMonitor monitor;
	private long position;

	public TransferListener(ITransfer transfer, IProgressMonitor monitor) {
		this.transfer = transfer;
		this.monitor = monitor;
	}

	@Override
	public void opened() {
		position = getPosition();
	}

	private long getPosition() {
		return transfer.getInputStream().getPosition();
	}

	@Override
	public void transferred(int count) {
		long newPosition = getPosition();
		monitor.worked((int) (newPosition - position));
		position = newPosition;
	}
	
	@Override
	public void closed() {
	}

}
