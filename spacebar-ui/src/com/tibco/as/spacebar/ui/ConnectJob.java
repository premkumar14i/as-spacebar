package com.tibco.as.spacebar.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Display;

import com.tibco.as.spacebar.ui.handlers.metaspace.MetaspaceRule;
import com.tibco.as.spacebar.ui.model.Connection;
import com.tibco.as.spacebar.ui.model.Metaspace;
import com.tibco.as.spacebar.ui.model.Metaspace.State;

import com.tibco.as.utils.ASUtils;

public class ConnectJob extends Job {

	private Metaspace metaspace;

	public ConnectJob(Metaspace metaspace) {
		super("Connect metaspace");
		setUser(true);
		setRule(new MetaspaceRule(metaspace));
		this.metaspace = metaspace;
	}

	@Override
	public IStatus run(IProgressMonitor monitor) {
		String metaspaceName = ASUtils.getMetaspaceName(metaspace
				.getMetaspaceName());
		monitor.beginTask(
				NLS.bind("Connecting to metaspace ''{0}''", metaspaceName),
				IProgressMonitor.UNKNOWN);
		try {
			if (metaspace.getState() != State.DISCONNECTED) {
				return Status.OK_STATUS;
			}
			Connection connection = new Connection();
			connection.connect(metaspace);
			metaspace.setConnection(connection);
			return Status.OK_STATUS;
		} catch (ExceptionInInitializerError e) {
			String message = SpaceBarPlugin
					.getEnvironmentVariableErrorMessage(SpaceBarPlugin
							.getSharedLibraryEnvironmentVariableName());
			return SpaceBarPlugin.createStatus(e, message);
		} catch (NoClassDefFoundError e) {
			return SpaceBarPlugin.createClasspathErrorStatus(e);
		} catch (NoSuchMethodError e) {
			return SpaceBarPlugin.createStatus(e,
					"Incompatible ActiveSpaces version");
		} catch (Throwable e) {
			return SpaceBarPlugin.createStatus(e, NLS.bind(
					"Could not connect to metaspace ''{0}''", metaspaceName));
		} finally {
			monitor.done();
		}
	}

	@Override
	protected void canceling() {
		try {
			metaspace.disconnect();
		} catch (Exception e) {
			SpaceBarPlugin.errorDialog(Display.getDefault()
					.getActiveShell(), "Could not disconnect", e);
		}
		super.canceling();
	}
}
