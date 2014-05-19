package com.tibco.as.spacebar.ui.wizards.transfer;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.tibco.as.spacebar.ui.Messages;

import com.tibco.as.io.Import;
import com.tibco.as.io.Operation;
import com.tibco.as.space.Member.DistributionRole;

public class ImportEditor extends Composite {

	private DataBindingContext m_bindingContext;
	private Import config;
	protected ComboViewer distributionRoleCombo;
	protected ComboViewer operationCombo;
	protected Text batchSizeText;
	protected Text workerCountText;

	public ImportEditor(Composite parent, int style, Import config) {
		super(parent, style);
		this.config = config;
		setLayout(new GridLayout(2, false));

		Label batchSizeLabel = new Label(this, SWT.NONE);
		batchSizeLabel.setText(Messages.Transfer_Import_Batch_Size);
		batchSizeLabel
				.setToolTipText(Messages.Transfer_Import_Batch_Size_ToolTip);

		batchSizeText = new Text(this, SWT.BORDER | SWT.SINGLE);
		batchSizeText
				.setToolTipText(Messages.Transfer_Import_Batch_Size_ToolTip);
		batchSizeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));

		Label workerCountLabel = new Label(this, SWT.NONE);
		workerCountLabel.setText(Messages.Transfer_Import_Worker_Count);
		workerCountLabel.setToolTipText(Messages.Transfer_Import_Worker_Count_ToolTip);

		workerCountText = new Text(this, SWT.BORDER | SWT.SINGLE);
		workerCountText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false));
		workerCountText.setToolTipText(Messages.Transfer_Import_Worker_Count_ToolTip);

		Label distributionRoleLabel = new Label(this, SWT.NONE);
		distributionRoleLabel.setText(Messages.Transfer_Import_Distribution_Role);
		distributionRoleLabel.setToolTipText(Messages.Transfer_Import_Distribution_Role_ToolTip);

		distributionRoleCombo = new ComboViewer(this, SWT.READ_ONLY);
		distributionRoleCombo.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		distributionRoleCombo.setContentProvider(ArrayContentProvider
				.getInstance());
		distributionRoleCombo.setInput(DistributionRole.values());

		new Label(this, SWT.NONE).setText("Space operation:");

		operationCombo = new ComboViewer(this, SWT.READ_ONLY);
		operationCombo.getCombo().setLayoutData(
				new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		operationCombo.setContentProvider(ArrayContentProvider.getInstance());
		operationCombo.setInput(Operation.values());

		if (this.config != null) {
			m_bindingContext = initDataBindings();
		}
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	private DataBindingContext initDataBindings() {
		IObservableValue distributionRoleObserveWidget = ViewersObservables
				.observeSingleSelection(distributionRoleCombo);
		IObservableValue distributionRoleObserveValue = PojoObservables
				.observeValue(config, "distributionRole");
		IObservableValue operationObserveWidget = ViewersObservables
				.observeSingleSelection(operationCombo);
		IObservableValue operationObserveValue = PojoObservables.observeValue(
				config, "operation");
		IObservableValue batchSizeObserveWidget = SWTObservables.observeText(
				batchSizeText, SWT.Modify);
		IObservableValue batchSizeObserveValue = PojoObservables.observeValue(
				config, "batchSize");
		IObservableValue workerCountObserveWidget = SWTObservables.observeText(
				workerCountText, SWT.Modify);
		IObservableValue workerCountObserveValue = PojoObservables
				.observeValue(config, "workerCount");
		//
		DataBindingContext bindingContext = new DataBindingContext();
		//
		bindingContext.bindValue(distributionRoleObserveWidget,
				distributionRoleObserveValue, null, null);
		bindingContext.bindValue(operationObserveWidget, operationObserveValue,
				null, null);
		bindingContext.bindValue(batchSizeObserveWidget, batchSizeObserveValue,
				null, null);
		bindingContext.bindValue(workerCountObserveWidget,
				workerCountObserveValue, null, null);
		//
		return bindingContext;
	}

	public Import getImport() {
		return config;
	}

	public void setImportBean(Import newImportBean) {
		setImportBean(newImportBean, true);
	}

	public void setImportBean(Import newImportBean, boolean update) {
		config = newImportBean;
		if (update) {
			if (m_bindingContext != null) {
				m_bindingContext.dispose();
				m_bindingContext = null;
			}
			if (config != null) {
				m_bindingContext = initDataBindings();
			}
		}
	}

}
