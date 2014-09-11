package com.tibco.as.spacebar.ui;

import java.io.IOException;

import org.eclipse.equinox.security.storage.ISecurePreferences;
import org.eclipse.equinox.security.storage.StorageException;

import com.tibco.as.spacebar.ui.model.Metaspace;

public class SpaceBarSecureStore {

	private static final String PASSWORD = "password"; //$NON-NLS-1$

	private final ISecurePreferences preferences;

	public SpaceBarSecureStore(ISecurePreferences preferences) {
		this.preferences = preferences;
	}

	/**
	 * Puts password for the given metaspace into the secure store
	 *
	 */
	public void putPassword(Metaspace metaspace, String password)
			throws StorageException, IOException {
		String pathName = getNodePath(metaspace);
		ISecurePreferences node = preferences.node(pathName);
		node.put(PASSWORD, password, true);
		node.flush();
	}

	/**
	 * Retrieves password stored for the given metaspace from the secure store
	 *
	 */
	public String getPassword(Metaspace metaspace) throws StorageException {
		String pathName = getNodePath(metaspace);
		if (!preferences.nodeExists(pathName))
			return null;
		ISecurePreferences node = preferences.node(pathName);
		return node.get(PASSWORD, ""); //$NON-NLS-1$
	}

	private String getNodePath(Metaspace metaspace) {
		return metaspace.getName();
	}

	/**
	 * Clear password for the given metaspace.
	 *
	 */
	public void clearPassword(Metaspace metaspace) throws IOException {
		String pathName = getNodePath(metaspace);
		if (preferences.nodeExists(pathName)) {
			ISecurePreferences node = preferences.node(pathName);
			node.removeNode();
			node.flush();
		}
	}
}
