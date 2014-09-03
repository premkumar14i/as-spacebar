package com.tibco.as.spacebar.ui;

import java.text.MessageFormat;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.tibco.as.space.security.AuthenticationInfo;

/**
 * Login dialog, which prompts for the user's account info, and has Login and
 * Cancel buttons.
 */
public class AuthenticationDialog extends Dialog {

	private AuthenticationInfo info;

	private Text usernameText;
	private Text domainText;
	private Text passwordText;

	private String username;

	private String domain;

	private String password;

	public AuthenticationDialog(Shell shell, AuthenticationInfo info) {
		super(shell);
		this.info = info;
	}

	@Override
	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		String title;
		if (info.getHint() == null) {
			title = "Authentication";
		} else {
			title = MessageFormat.format("{0} - {1}", "Authentication",
					info.getHint());
		}
		shell.setText(title);
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		composite.setLayout(layout);

		Label domainLabel = new Label(composite, SWT.NONE);
		domainLabel.setText("&Domain:");
		domainLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER,
				false, false));

		domainText = new Text(composite, SWT.BORDER);
		GridData gridData = new GridData(GridData.FILL, GridData.FILL, true,
				false);
		gridData.widthHint = convertHeightInCharsToPixels(20);
		domainText.setLayoutData(gridData);

		Label usernameLabel = new Label(composite, SWT.NONE);
		usernameLabel.setText("&Username:");
		usernameLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER,
				false, false));

		usernameText = new Text(composite, SWT.BORDER);
		usernameText.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, false));

		Label passwordLabel = new Label(composite, SWT.NONE);
		passwordLabel.setText("&Password:");
		passwordLabel.setLayoutData(new GridData(GridData.END, GridData.CENTER,
				false, false));

		passwordText = new Text(composite, SWT.BORDER | SWT.PASSWORD);
		passwordText.setLayoutData(new GridData(GridData.FILL, GridData.FILL,
				true, false));
		restoreSettings();
		return composite;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
				true);
		createButton(parent, IDialogConstants.CANCEL_ID,
				IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		username = usernameText.getText();
		if (username.isEmpty()) {
			MessageDialog.openError(getShell(), "Invalid Username",
					"Username field must not be blank.");
			return;
		}
		domain = domainText.getText();
		password = passwordText.getText();
		IDialogSettings settings = SpaceBarPlugin.getDefault()
				.getDialogSettings();
		settings.put("username", username);
		settings.put("domain", domain);
		super.okPressed();
	}

	private void restoreSettings() {
		IDialogSettings settings = SpaceBarPlugin.getDefault()
				.getDialogSettings();
		String domain = settings.get("domain");
		String username = settings.get("username");
		if (domain != null) {
			domainText.setText(domain);
		}
		if (username != null) {
			usernameText.setText(domain);
		}
	}

	public String getDomain() {
		return domain;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}