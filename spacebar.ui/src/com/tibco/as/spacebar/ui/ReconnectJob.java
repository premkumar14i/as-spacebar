package com.tibco.as.spacebar.ui;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.osgi.util.NLS;

import com.tibco.as.spacebar.ui.handlers.metaspace.MetaspaceRule;
import com.tibco.as.spacebar.ui.model.Connection;
import com.tibco.as.spacebar.ui.model.Metaspace;
import com.tibco.as.util.Utils;

public class ReconnectJob extends Job {

	private Metaspace metaspace;

	public ReconnectJob(Metaspace metaspace) {
		super("Reconnect metaspace");
		setUser(true);
		setRule(new MetaspaceRule(metaspace));
		this.metaspace = metaspace;
	}

	@Override
	public IStatus run(IProgressMonitor monitor) {
		String metaspaceName = Utils.getMetaspaceName(metaspace
				.getMetaspaceName());
		monitor.beginTask(
				NLS.bind("Reconnecting to metaspace ''{0}''", metaspaceName),
				IProgressMonitor.UNKNOWN);

		try {
			/*
			 * Disconnect part
			 */
			if (metaspace.isConnected()) {
				metaspace.getConnection().close();
				metaspace.setConnection(null);
			}

			/*
			 * Connect part
			 */
			Connection connection = new Connection(metaspace);
			connection.open();
			metaspace.setConnection(connection);
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
					"Could not reconnect to metaspace ''{0}''", metaspaceName));
		} finally {
			monitor.done();
		}

		return Status.OK_STATUS;
	}
}
