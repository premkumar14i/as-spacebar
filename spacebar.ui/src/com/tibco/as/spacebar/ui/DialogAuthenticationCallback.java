package com.tibco.as.spacebar.ui;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.tibco.as.space.security.AuthenticationCallback;
import com.tibco.as.space.security.AuthenticationInfo;
import com.tibco.as.space.security.UserPwdCredential;
import com.tibco.as.space.security.AuthenticationInfo.Method;

public class DialogAuthenticationCallback implements AuthenticationCallback {

	private Shell shell;

	public DialogAuthenticationCallback(Shell shell) {
		this.shell = shell;
	}

	@Override
	public void createUserCredential(AuthenticationInfo info) {
		if (info.getAuthenticationMethod() == Method.USERPWD) {
			final AuthenticationDialog dialog = new AuthenticationDialog(shell,
					info);
			Display display = shell.getDisplay();
			display.syncExec(new Runnable() {

				@Override
				public void run() {
					dialog.open();
				}
			});
			UserPwdCredential credential = (UserPwdCredential) info
					.getUserCredential();
			String domain = dialog.getDomain();
			if (!domain.isEmpty()) {
				credential.setDomain(domain);
			}
			credential.setUserName(dialog.getUsername());
			credential.setPassword(dialog.getPassword().toCharArray());
		}
	}

	@Override
	public void onCleanup() {
		// do nothing
	}

}
