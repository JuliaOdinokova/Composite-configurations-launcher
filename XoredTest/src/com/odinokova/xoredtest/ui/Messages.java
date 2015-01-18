package com.odinokova.xoredtest.ui;

import org.eclipse.osgi.util.NLS;

class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.odinokova.xoredtest.ui.messages"; //$NON-NLS-1$
	public static String CompositeConfigurationTab_Add;
	public static String CompositeConfigurationTab_Empty_Error_Message;
	public static String CompositeConfigurationTab_Move_Down;
	public static String CompositeConfigurationTab_Group_Configurations;
	public static String CompositeConfigurationTab_Group_Selected_Configurations;
	public static String CompositeConfigurationTab_Name;
	public static String CompositeConfigurationTab_Remove;
	public static String CompositeConfigurationTab_Move_Up;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
