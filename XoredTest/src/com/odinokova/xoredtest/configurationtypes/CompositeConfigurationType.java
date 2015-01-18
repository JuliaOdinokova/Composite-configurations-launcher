package com.odinokova.xoredtest.configurationtypes;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;

import com.odinokova.xoredtest.CompositePlugin;

public class CompositeConfigurationType implements ILaunchConfigurationDelegate {

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {

		ILaunchConfiguration[] availableConfigurations;
		try {
			availableConfigurations = DebugPlugin.getDefault()
					.getLaunchManager().getLaunchConfigurations();
			List<String> savedConfigurationsNames;
			savedConfigurationsNames = configuration
					.getAttribute(
							ICompositeConfigurationConstants.ATTR_SELECTED_CONFIGURATIONS,
							new ArrayList<String>());
			List<ILaunchConfiguration> selectedConfigurations = new ArrayList<ILaunchConfiguration>();
			for (String name : savedConfigurationsNames)
				for (ILaunchConfiguration conf : availableConfigurations)
					if (conf.getName().equals(name)) {
						selectedConfigurations.add(conf);
						break;
					}
			for (ILaunchConfiguration conf : selectedConfigurations)
				conf.launch(mode, monitor);

		} catch (CoreException ex) {
			CompositePlugin.getDefault().log(
					"Error while launch composite configuration", ex);//$NON-NLS-1$
		}
	}
}
