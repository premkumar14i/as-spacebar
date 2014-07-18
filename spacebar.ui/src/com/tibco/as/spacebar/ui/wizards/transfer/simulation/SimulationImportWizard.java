package com.tibco.as.spacebar.ui.wizards.transfer.simulation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.osgi.util.NLS;

import com.tibco.as.spacebar.ui.Image;
import com.tibco.as.spacebar.ui.SpaceBarPlugin;
import com.tibco.as.spacebar.ui.preferences.Preferences;
import com.tibco.as.spacebar.ui.wizards.transfer.AbstractImportWizard;
import com.tibco.as.io.IMetaspaceTransfer;
import com.tibco.as.io.Import;
import com.tibco.as.io.Transfer;
import com.tibco.as.io.simulation.ObjectFactory;
import com.tibco.as.io.simulation.Simulation;
import com.tibco.as.io.simulation.SimulationImport;
import com.tibco.as.io.simulation.SimulationImporter;
import com.tibco.as.space.Metaspace;

public class SimulationImportWizard extends AbstractImportWizard<Object[]> {

	public SimulationImportWizard() {
		super("SimulationImportWizard", Image.WIZBAN_SIMULATION);
	}

	@Override
	protected Transfer createTransfer() {
		SimulationImport config = new SimulationImport();
		Preferences.configureImport(config);
		return config;
	}

	@Override
	protected Collection<IMetaspaceTransfer> getImporters(List<File> files,
			Import defaultImport) {
		Map<String, Metaspace> metaspaces = getConnectedMetaspaces();
		Collection<IMetaspaceTransfer> importers = new ArrayList<IMetaspaceTransfer>();
		for (File file : files) {
			Simulation simulation;
			try {
				simulation = getSimulation(file);
			} catch (JAXBException e) {
				SpaceBarPlugin.errorDialog(getShell(), NLS.bind(
						"Could not load simulation file ''{0}''", file), e);
				continue;
			}
			String metaspaceName = simulation.getMetaspace();
			Metaspace metaspace = metaspaces.containsKey(metaspaceName) ? metaspaces
					.get(metaspaceName) : metaspaces.values().iterator().next();
			SimulationImporter importer = new SimulationImporter(metaspace,
					simulation);
			importer.setDefaultTransfer(defaultImport);
			importers.add(importer);
		}
		return importers;
	}

	@Override
	protected SimulationImportMainPage getMainPage(
			IStructuredSelection selection) {
		return new SimulationImportMainPage();
	}

	private Simulation getSimulation(File file) throws JAXBException {
		JAXBContext jc = JAXBContext.newInstance(ObjectFactory.class);
		Unmarshaller unmarshaller = jc.createUnmarshaller();
		@SuppressWarnings("unchecked")
		JAXBElement<Simulation> element = (JAXBElement<Simulation>) unmarshaller
				.unmarshal(file);
		return element.getValue();
	}

}
