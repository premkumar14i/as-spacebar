package com.tibco.as.spacebar.ui;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.statushandlers.StatusManager;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

import com.tibco.as.io.EventManager;
import com.tibco.as.spacebar.ui.model.Metaspace;
import com.tibco.as.spacebar.ui.model.Metaspaces;
import com.tibco.as.util.AS;
import com.tibco.as.util.MetaType.ListOfMeta.Meta;
import com.tibco.as.util.Property;
import com.tibco.as.util.Utils;

/**
 * The activator class controls the plug-in life cycle
 */
public class SpaceBarPlugin extends AbstractUIPlugin {

	private static final Map<String, String> AS_212_SPACEDEF_PROPERTIES = new HashMap<String, String>();
	static {
		AS_212_SPACEDEF_PROPERTIES.put("HostAwareReplication",
				"setHostAwareReplication");
		AS_212_SPACEDEF_PROPERTIES.put("QueryLimit", "setQueryLimit");
	}

	public static final String ID_PLUGIN = "spacebar.ui";

	public static final String ID_METASPACES = "com.tibco.as.spacebar.ui.navigator";

	public final static String ID_PERSPECTIVE = "com.tibco.as.spacebar.ui.perspective";

	private final static String ENV_NATIVE_AIX = "LIBPATH";
	private final static String ENV_NATIVE_HPUX = "SHLIB_PATH";
	private final static String ENV_NATIVE_LINUX = "LD_LIBRARY_PATH";
	private final static String ENV_NATIVE_MAC = "DYLD_LIBRARY_PATH";
	private final static String ENV_NATIVE_QNX = "LD_LIBRARY_PATH";
	private final static String ENV_NATIVE_SOLARIS = "LD_LIBRARY_PATH";
	private final static String ENV_NATIVE_WIN = "PATH";
	private final static String ENV_NATIVE_UNKNOWN = "???";
	private static final String ENV_AS_HOME = "AS_HOME";

	// The shared instance
	private static SpaceBarPlugin plugin;

	private Metaspaces metaspaces;

	private AS metaModel;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		EventManager.addListener(new EventListener(StatusManager.getManager()));
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		for (Image image : Image.values()) {
			Bundle bundle = Platform.getBundle(ID_PLUGIN);
			IPath path = new Path("icons/" + image.getPath());
			URL url = FileLocator.find(bundle, path, null);
			ImageDescriptor desc = ImageDescriptor.createFromURL(url);
			reg.put(image.name(), desc);
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static SpaceBarPlugin getDefault() {
		return plugin;
	}

	public static void log(IStatus status) {
		StatusManager.getManager().handle(status);
	}

	public static IStatus createStatus(Throwable e, String format,
			Object... args) {
		return new Status(IStatus.ERROR, ID_PLUGIN, NLS.bind(format, args), e);
	}

	public static IStatus createStatus(int severity, String format,
			Object... args) {
		return new Status(severity, ID_PLUGIN, NLS.bind(format, args));
	}

	public AS getASMetaModel() throws JAXBException {
		if (metaModel == null) {
			metaModel = (AS) JAXBContext
					.newInstance(AS.class)
					.createUnmarshaller()
					.unmarshal(
							AS.class.getClassLoader().getResourceAsStream(
									"as-meta-model.xml"));
		}
		return metaModel;
	}

	public Map<Property, List<Property>> getSpaceDefProperties() {
		List<Meta> metas;
		try {
			metas = getASMetaModel().getSpaceDefMeta().getListOfMeta()
					.getMeta();
			for (Meta meta : metas) {
				if ("SpaceDef".equals(meta.getId())) {
					List<Property> properties = meta
							.getPropertyListDefinition().getProperty();
					Map<Property, List<Property>> map = new HashMap<Property, List<Property>>();
					List<Property> list = new ArrayList<Property>();
					for (Property property : properties) {
						if ("label".equals(property.getDataType().getValue())) {
							list = new ArrayList<Property>();
							map.put(property, list);
						} else {
							String id = property.getId();
							if (AS_212_SPACEDEF_PROPERTIES.containsKey(id)) {
								if (!Utils
										.hasSpaceDefMethod(AS_212_SPACEDEF_PROPERTIES
												.get(id))) {
									continue;
								}
							}
							list.add(property);
						}
					}
					return map;
				}
			}
		} catch (JAXBException e) {
			SpaceBarPlugin.logException("Could not load AS meta model", e);
		}
		return null;
	}

	private JAXBContext getContext(Class<?> clazz) throws JAXBException {
		return JAXBContext.newInstance(Metaspaces.class, Metaspace.class);
	}

	public Metaspaces getMetaspaces() {
		if (metaspaces == null) {
			File file = getMetaspacesFile();
			if (file.exists()) {
				try {
					metaspaces = (Metaspaces) getContext(Metaspaces.class)
							.createUnmarshaller().unmarshal(file);
					for (Metaspace metaspace : metaspaces.getMetaspaces()) {
						metaspace.setMetaspaces(metaspaces);
						if (metaspace.isAutoconnect()) {
							// delay connection to give UI time to load
							new ConnectJob(metaspace).schedule(1000);
						}
					}
				} catch (JAXBException e) {
					logException(
							NLS.bind(
									"Could not load metaspaces from file ''{0}''",
									file), e);
					metaspaces = new Metaspaces();
				}
			} else {
				metaspaces = new Metaspaces();
				Metaspace metaspace = new Metaspace();
				metaspace.setMetaspaces(metaspaces);
				metaspace.setName("Default");
				metaspace.setTimeout(30000L);
				try {
					String hostName = InetAddress.getLocalHost().getHostName();
					metaspace.setMemberName("SpaceBar-" + hostName);
				} catch (UnknownHostException e) {
					logException("Could not get local host name", e);
				}
				metaspaces.getMetaspaces().add(metaspace);
				saveMetaspaces();
			}
		}
		return metaspaces;
	}

	private File getMetaspacesFile() {
		return getStateLocation().append("metaspaces.xml").toFile();
	}

	public static void errorDialog(String message, Throwable e) {
		errorDialog(getShell(), message, e);
	}

	public static void errorDialog(Shell shell, String message, Throwable e) {
		MessageDialog.openError(shell, message, e.getMessage());
		logException(message, e);
	}

	public static void errorDialog(String title, String message) {
		errorDialog(getShell(), title, message);
	}

	public static void errorDialog(Shell shell, String title, String message) {
		MessageDialog.openError(shell, title, message);
	}

	public boolean isDefault(Color color) {
		return color.getRGB().equals(PreferenceConverter.COLOR_DEFAULT_DEFAULT);
	}

	public static IStatus createClasspathErrorStatus(NoClassDefFoundError e) {
		return SpaceBarPlugin.createStatus(e, "Class not found - {0}",
				getEnvironmentVariableErrorMessage(ENV_AS_HOME));
	}

	public static String getEnvironmentVariableErrorMessage(String variableName) {
		String value = System.getenv(variableName);
		if (value == null) {
			return NLS.bind("{0} not set", variableName);
		} else {
			return NLS
					.bind("Incompatible ActiveSpaces version or {0} not set properly ({1})",
							variableName, value);
		}
	}

	public static String getSharedLibraryEnvironmentVariableName() {
		String os = Platform.getOS();
		if (Platform.OS_AIX.equals(os))
			return ENV_NATIVE_AIX;
		else if (Platform.OS_HPUX.equals(os))
			return ENV_NATIVE_HPUX;
		if (Platform.OS_LINUX.equals(os))
			return ENV_NATIVE_LINUX;
		else if (Platform.OS_MACOSX.equals(os))
			return ENV_NATIVE_MAC;
		else if (Platform.OS_QNX.equals(os))
			return ENV_NATIVE_QNX;
		else if (Platform.OS_SOLARIS.equals(os))
			return ENV_NATIVE_SOLARIS;
		else if (Platform.OS_WIN32.equals(os))
			return ENV_NATIVE_WIN;
		else
			return ENV_NATIVE_UNKNOWN;
	}

	/**
	 * Adds message to log.
	 * 
	 * @param level
	 *            severity level of the message (OK, INFO, WARNING, ERROR,
	 *            OK_DEBUG, INFO_DEBUG, WARNING_DEBUG, ERROR_DEBUG)
	 * @param message
	 *            text to add to the log
	 * @param exception
	 *            exception thrown
	 */
	protected static IStatus _log(int severity, String message,
			Throwable exception) {
		message = message == null ? "null" : message; //$NON-NLS-1$
		Status statusObj = new Status(severity, ID_PLUGIN, severity, message,
				exception);
		Bundle bundle = Platform.getBundle(ID_PLUGIN);
		if (bundle != null) {
			Platform.getLog(bundle).log(statusObj);
		}
		return statusObj;
	}

	public static void log(int level, String message) {
		_log(level, message, null);
	}

	public static void log(int level, String message, Throwable e) {
		_log(level, message, e);
	}

	public static IStatus logException(String message, Throwable e) {
		return _log(IStatus.ERROR, message, e);
	}

	public static void logException(Throwable e) {
		logException(e.getMessage(), e);
	}

	public org.eclipse.swt.graphics.Image getImage(Image image) {
		return getImageRegistry().get(image.name());
	}

	public ImageDescriptor getImageDescriptor(Image image) {
		return getImageRegistry().getDescriptor(image.name());
	}

	/**
	 * Returns the currently active workbench window shell or <code>null</code>
	 * if none.
	 * 
	 * @return the currently active workbench window shell or <code>null</code>
	 */
	public static Shell getShell() {
		IWorkbenchWindow window = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow();
		if (window == null) {
			IWorkbenchWindow[] windows = PlatformUI.getWorkbench()
					.getWorkbenchWindows();
			if (windows.length > 0) {
				return windows[0].getShell();
			}
		} else {
			return window.getShell();
		}
		return null;
	}

	public void delete(Metaspace metaspace) {
		if (getMetaspaces().removeChild(metaspace)) {
			saveMetaspaces();
		}
	}

	public void saveMetaspaces() {
		File file = getMetaspacesFile();
		try {
			getContext(Metaspaces.class).createMarshaller().marshal(
					getMetaspaces(), file);
		} catch (JAXBException e) {
			logException(
					NLS.bind("Could not save metaspaces to file ''{0}''", file),
					e);
		}
	}

	public void add(Metaspace metaspace) {
		if (getMetaspaces().addChild(metaspace)) {
			saveMetaspaces();
		}
	}

}
