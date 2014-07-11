package com.tibco.as.spacebar.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.osgi.util.NLS;
import org.eclipse.ui.progress.UIJob;

import com.tibco.as.spacebar.ui.handlers.metaspace.MetaspaceRule;
import com.tibco.as.spacebar.ui.model.Metaspace;

public class DisconnectJob extends UIJob {

	private Metaspace metaspace;

	public DisconnectJob(Metaspace metaspace) {
		super("Disconnect metaspace");
		setUser(true);
		setRule(new MetaspaceRule(metaspace));
		this.metaspace = metaspace;
	}

	@Override
	public IStatus runInUIThread(IProgressMonitor monitor) {
		monitor.beginTask(getName(), IProgressMonitor.UNKNOWN);
		try {
			metaspace.disconnect();
			if (monitor.isCanceled()) {
				return Status.CANCEL_STATUS;
			}
			return Status.OK_STATUS;
		} catch (Exception e) {
			String message = NLS.bind("Could not disconnect metaspace ''{0}''",
					metaspace.getConnection().getMetaspaceName());
			return SpaceBarPlugin.logException(message, e);
		} finally {
			monitor.done();
		}
	}
}