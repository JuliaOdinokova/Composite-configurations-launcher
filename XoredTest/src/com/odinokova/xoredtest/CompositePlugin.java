package com.odinokova.xoredtest;

import org.eclipse.core.runtime.Status;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class CompositePlugin extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "XoredTest"; //$NON-NLS-1$

	// The shared instance
	private static CompositePlugin plugin;

	/**
	 * The constructor
	 */
	public CompositePlugin() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Write error message in log
	 * 
	 * @param message
	 *            - error message
	 * @param e
	 *            - exception
	 */
	public void log(String message, Exception e) {
		getLog().log(new Status(Status.ERROR, PLUGIN_ID, Status.OK, message, e));
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static CompositePlugin getDefault() {
		return plugin;
	}
}
