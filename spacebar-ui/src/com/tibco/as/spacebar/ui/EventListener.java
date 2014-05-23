package com.tibco.as.spacebar.ui;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.statushandlers.StatusManager;

import com.tibco.as.io.IEvent;
import com.tibco.as.io.IEvent.Severity;
import com.tibco.as.io.IEventListener;

public class EventListener implements IEventListener {

	private StatusManager manager;

	public EventListener(StatusManager manager) {
		this.manager = manager;
	}

	@Override
	public void onEvent(IEvent event) {
		manager.handle(new Status(getSeverity(event.getSeverity()),
				SpaceBarPlugin.ID_PLUGIN, 0, event.getMessage(), event
						.getException()));
	}

	private int getSeverity(Severity severity) {
		switch (severity) {
		case DEBUG:
			return IStatus.INFO;
		case INFO:
			return IStatus.INFO;
		case WARN:
			return IStatus.WARNING;
		case ERROR:
			return IStatus.ERROR;
		default:
			return IStatus.OK;
		}
	}
}
