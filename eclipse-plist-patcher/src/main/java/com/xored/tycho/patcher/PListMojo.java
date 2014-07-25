package com.xored.tycho.patcher;

/**
 * Based on MyMojo.java from https://github.com/komaz/eclipse-ini-patcher
 */
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Goal which adds aS environment variables to info.plist file for Mac OS X platform.
 * 
 * @goal patch-plist-file
 * @phase package
 */
public class PListMojo extends AbstractMojo {

	private static final String ERROR_NO_LAUNCHER = "Product %s does not contain equinox launcher, skipped";

	/**
	 * Location of the file.
	 * 
	 * @parameter expression="${project.build.directory}"
	 * @required
	 * 
	 */
	private File outputDirectory;

	public void execute() throws MojoExecutionException {
		File productsDir = new File(outputDirectory, "products");
		for (File productFile : productsDir.listFiles()) {
			if (productFile.isDirectory()) {
				// Ideally we should use tycho project utils here to get
				// destination directory of Mac OS X product, but for now just
				// assume default location

				// x86_64
				final File macDir64 = new File(productFile,
						"macosx/cocoa/x86_64");
				if (macDir64.exists() && macDir64.isDirectory()) {
					patchProduct(productFile, macDir64);
				}
				// x86
				final File macDir32 = new File(productFile, "macosx/cocoa/x86");
				if (macDir32.exists() && macDir32.isDirectory()) {
					patchProduct(productFile, macDir32);
				}
			}
		}
	}

	private void patchProduct(File productDir, File macDir) {
		while (macDir.list().length == 1) {
			macDir = macDir.listFiles()[0];
		}
		File launcherFile = findLauncher(macDir);
		if (launcherFile == null || !launcherFile.exists()) {
			getLog().warn(
					String.format(ERROR_NO_LAUNCHER, productDir.getName()));
			return;
		}
		if (launcherFile == null || !launcherFile.exists()) {
			getLog().warn(
					String.format(ERROR_NO_LAUNCHER, productDir.getName()));
			return;
		}

		for (File app : getAppDirs(macDir)) {
			File contents = new File(app, "Contents");
			File iniDir = new File(contents, "MacOS");
			if (!iniDir.exists() || !iniDir.isDirectory()) {
				continue;
			}
			patchInfoPListFile(contents);
		}

	}

	private void patchInfoPListFile(File contents) {
		File infoPListFile = new File(contents, "Info.plist");
		if (infoPListFile.exists()) {
			try {
				DocumentBuilder builder = DocumentBuilderFactory.newInstance()
						.newDocumentBuilder();
				Document document = builder.parse(infoPListFile);
				if (document == null) {
					return;
				}
				XPath xPath = XPathFactory.newInstance().newXPath();
				// No high resolution capable is set, so let's set it.
				Node dict = (Node) xPath.compile("/plist/dict[last()]")
						.evaluate(document, XPathConstants.NODE);
				Element nshrc = document.createElement("key");
				nshrc.setTextContent("LSEnvironment");
				Element envDict = document.createElement("dict");
				Element asHomeKey = document.createElement("key");
				asHomeKey.setTextContent("AS_HOME");
				envDict.appendChild(asHomeKey);
				Element asHomeValue = document.createElement("string");
				asHomeValue.setTextContent("/opt/tibco/as/2.1");
				envDict.appendChild(asHomeValue);
				Element libPathKey = document.createElement("key");
				libPathKey.setTextContent("DYLD_LIBRARY_PATH");
				envDict.appendChild(libPathKey);
				Element libPathValue = document.createElement("string");
				libPathValue.setTextContent("/opt/tibco/as/2.1/lib");
				envDict.appendChild(libPathValue);
				dict.appendChild(nshrc);
				dict.appendChild(envDict);

				// Store document back to file
				TransformerFactory factory = TransformerFactory.newInstance();
				Transformer transformer = factory.newTransformer();
				transformer.setOutputProperty(OutputKeys.INDENT, "yes");
				transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
				DOMSource source = new DOMSource(document);
				StreamResult result = new StreamResult(infoPListFile);
				transformer.transform(source, result);

			} catch (Exception e) {
				getLog().error(e);
			}
		}
	}

	private List<File> getAppDirs(File parent) {
		List<File> result = new ArrayList<File>();
		for (File child : parent.listFiles()) {
			if (child.isDirectory() && child.getName().endsWith(APP_EXT)) {
				result.add(child);
			}
		}
		return result;
	}

	/**
	 * Searches for equinox launcher jar, returns null if not found
	 * 
	 * @param macDir
	 * @return
	 */
	private File findLauncher(File macDir) {
		File plugins = new File(macDir, "plugins");
		if (!plugins.exists()) {
			return null;
		}

		List<File> candidates = new ArrayList<File>();
		for (File file : plugins.listFiles()) {
			if (file.isFile() && file.getName().startsWith(LAUNCHER_JAR)
					&& file.getName().endsWith(JAR_EXT)) {
				candidates.add(file);
			}
		}
		if (candidates.isEmpty()) {
			return null;
		}

		// here should be honest sort by version, but for now assume that
		// string comparison works fine
		Collections.sort(candidates);
		return candidates.get(candidates.size() - 1);
	}

	private static final String LAUNCHER_JAR = "org.eclipse.equinox.launcher_";
	private static final String JAR_EXT = ".jar";
	private static final String APP_EXT = ".app";
}
