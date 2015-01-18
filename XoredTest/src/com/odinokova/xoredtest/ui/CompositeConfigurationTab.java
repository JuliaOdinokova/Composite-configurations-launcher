package com.odinokova.xoredtest.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;

import com.odinokova.xoredtest.CompositePlugin;
import com.odinokova.xoredtest.configurationtypes.ICompositeConfigurationConstants;

/**
 * Implementation of new configuration tab class
 */
public class CompositeConfigurationTab extends AbstractLaunchConfigurationTab {

	private TableViewer selectedConfigurationViewer;
	private TableViewer availableConfigurationViewer;
	private final IDebugModelPresentation debugModelPresentation = DebugUITools
			.newDebugModelPresentation();
	private final ILaunchManager manager = DebugPlugin.getDefault()
			.getLaunchManager();
	private static final String COMPOSITE_CONFIGURATION_TYPE_ID = "com.odinokova.xoredtest.compositeConfigurationType";//$NON-NLS-1$

	@Override
	public void createControl(Composite parent) {
		Composite panel = new Composite(parent, SWT.NONE);
		setControl(panel);
		panel.setFont(parent.getFont());
		GridLayoutFactory.fillDefaults().numColumns(3).margins(5, 5)
				.applyTo(panel);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(panel);

		createAvailableConfigurationsGroup(panel);
		createSelectedConfigurationsGroup(panel);
		createMoveButtons(panel);
	}

	/**
	 * Create and add to parent control new configuration group (create group,
	 * table and button) for available configurations
	 * 
	 * @param panel
	 *            - parent control
	 */
	private void createAvailableConfigurationsGroup(Composite panel) {
		Group availableConfigurationsGroup = new Group(panel, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(5, 5)
				.applyTo(availableConfigurationsGroup);
		GridDataFactory.fillDefaults().grab(true, true)
				.applyTo(availableConfigurationsGroup);
		availableConfigurationsGroup
				.setText(Messages.CompositeConfigurationTab_Group_Configurations);

		Table availableConfigurationsTable = new Table(
				availableConfigurationsGroup, SWT.MULTI);
		GridDataFactory.fillDefaults().grab(true, true)
				.applyTo(availableConfigurationsTable);

		availableConfigurationViewer = new TableViewer(
				availableConfigurationsTable);
		availableConfigurationViewer
				.setContentProvider(new IStructuredContentProvider() {

					@Override
					public void inputChanged(Viewer viewer, Object oldInput,
							Object newInput) {
					}

					@Override
					public void dispose() {
					}

					@Override
					public Object[] getElements(Object inputElement) {
						try {
							List<ILaunchConfiguration> configurations = new ArrayList<ILaunchConfiguration>();
							for (ILaunchConfiguration configuration : ((ILaunchManager) inputElement)
									.getLaunchConfigurations())
								if (!COMPOSITE_CONFIGURATION_TYPE_ID
										.equals(configuration.getType()
												.getIdentifier()))
									configurations.add(configuration);
							return configurations.toArray();
						} catch (CoreException ex) {
							CompositePlugin.getDefault().log(
									"Error while getting configurations", ex);//$NON-NLS-1$
						}
						return null;
					}
				});

		availableConfigurationViewer
				.setLabelProvider(new ConfigurationsLabelProvider());

		availableConfigurationViewer.setInput(manager);

		Button buttonAdd = new Button(availableConfigurationsGroup, SWT.PUSH);
		GridDataFactory.fillDefaults().applyTo(buttonAdd);
		buttonAdd.setText(Messages.CompositeConfigurationTab_Add);
		buttonAdd.addSelectionListener(new SelectionAdapter() {

			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				ISelection selection = availableConfigurationViewer
						.getSelection();

				if (selection instanceof IStructuredSelection
						&& !selection.isEmpty()) {

					List<Object> selectedObjects = new ArrayList<Object>();
					for (Object object : (Object[]) selectedConfigurationViewer
							.getInput())
						selectedObjects.add(object);
					selectedObjects.addAll(((IStructuredSelection) selection)
							.toList());

					selectedConfigurationViewer.setInput(selectedObjects
							.toArray());
					setDirty(true);
					updateLaunchConfigurationDialog();
				}
			}
		});
	}

	/**
	 * Create and add to parent control new configuration group (create group,
	 * table and button) for selected configurations
	 * 
	 * @param panel
	 *            - parent control
	 */
	private void createSelectedConfigurationsGroup(Composite panel) {
		Group selectedConfigurationsGroup = new Group(panel, SWT.NONE);
		GridLayoutFactory.fillDefaults().margins(5, 5)
				.applyTo(selectedConfigurationsGroup);
		GridDataFactory.fillDefaults().grab(true, true)
				.applyTo(selectedConfigurationsGroup);
		selectedConfigurationsGroup
				.setText(Messages.CompositeConfigurationTab_Group_Selected_Configurations);

		Table selectedConfigurationsTable = new Table(
				selectedConfigurationsGroup, SWT.MULTI);
		GridDataFactory.fillDefaults().grab(true, true)
				.applyTo(selectedConfigurationsTable);

		selectedConfigurationViewer = new TableViewer(
				selectedConfigurationsTable);
		selectedConfigurationViewer
				.setContentProvider(new IStructuredContentProvider() {

					@Override
					public void inputChanged(Viewer viewer, Object oldInput,
							Object newInput) {
					}

					@Override
					public void dispose() {
					}

					@Override
					public Object[] getElements(Object inputElement) {
						return (Object[]) inputElement;
					}
				});

		selectedConfigurationViewer
				.setLabelProvider(new ConfigurationsLabelProvider());

		Button buttonRemove = new Button(selectedConfigurationsGroup, SWT.PUSH);
		GridDataFactory.fillDefaults().applyTo(buttonRemove);
		buttonRemove.setText(Messages.CompositeConfigurationTab_Remove);
		buttonRemove.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) selectedConfigurationViewer
						.getSelection();

				List<Object> selectedObjects = new ArrayList<Object>();
				Collections.addAll(selectedObjects,
						(Object[]) selectedConfigurationViewer.getInput());

				for (Object object : selection.toArray())
					selectedObjects.remove(object);

				selectedConfigurationViewer.setInput(selectedObjects.toArray());
				setDirty(true);
				updateLaunchConfigurationDialog();
			}
		});
	}

	/**
	 * Create and add to parent control buttons for moving up and down selected
	 * configurations
	 * 
	 * @param panel
	 *            - parent control
	 */
	private void createMoveButtons(Composite panel) {
		Composite composite = new Composite(panel, SWT.NONE);
		GridLayoutFactory.fillDefaults().applyTo(composite);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(composite);

		Button buttonMoveUp = new Button(composite, SWT.PUSH);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(buttonMoveUp);
		buttonMoveUp.setText(Messages.CompositeConfigurationTab_Move_Up);
		buttonMoveUp.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) selectedConfigurationViewer
						.getSelection();

				if (selection.size() != 1)
					return;

				List<Object> selectedObjects = new ArrayList<Object>();
				Collections.addAll(selectedObjects,
						(Object[]) selectedConfigurationViewer.getInput());

				int index = selectedConfigurationViewer.getTable()
						.getSelectionIndex();

				if (index <= 0)
					return;

				selectedObjects.remove(index);
				selectedObjects.add(index - 1, selection.getFirstElement());

				selectedConfigurationViewer.setInput(selectedObjects.toArray());
				selectedConfigurationViewer.getTable().setSelection(index - 1);
				setDirty(true);
				updateLaunchConfigurationDialog();
			}
		});

		Button buttonMoveDown = new Button(composite, SWT.PUSH);
		GridDataFactory.fillDefaults().grab(true, false)
				.applyTo(buttonMoveDown);
		buttonMoveDown.setText(Messages.CompositeConfigurationTab_Move_Down);
		buttonMoveDown.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) selectedConfigurationViewer
						.getSelection();

				if (selection.size() != 1)
					return;

				List<Object> selectedObjects = new ArrayList<Object>();
				Collections.addAll(selectedObjects,
						(Object[]) selectedConfigurationViewer.getInput());

				int index = selectedConfigurationViewer.getTable()
						.getSelectionIndex();

				if (index >= selectedObjects.size() - 1)
					return;

				selectedObjects.remove(index);
				selectedObjects.add(index + 1, selection.getFirstElement());

				selectedConfigurationViewer.setInput(selectedObjects.toArray());
				selectedConfigurationViewer.getTable().setSelection(index + 1);
				setDirty(true);
				updateLaunchConfigurationDialog();
			}
		});

	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		ILaunchConfiguration[] availableConfigurations;
		try {
			availableConfigurations = DebugPlugin.getDefault()
					.getLaunchManager().getLaunchConfigurations();

			List<String> savedConfigurationsNames;
			savedConfigurationsNames = configuration
					.getAttribute(
							ICompositeConfigurationConstants.ATTR_SELECTED_CONFIGURATIONS,
							new ArrayList<String>());

			List<ILaunchConfiguration> configurationsList = new ArrayList<ILaunchConfiguration>();
			for (String name : savedConfigurationsNames)
				for (ILaunchConfiguration config : availableConfigurations)
					if (config.getName().equals(name)) {
						configurationsList.add(config);
						break;
					}

			selectedConfigurationViewer.setInput(configurationsList.toArray());

		} catch (CoreException ex) {
			CompositePlugin.getDefault().log(
					"Error while initialize configuration dialog", ex);//$NON-NLS-1$
		}
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		List<String> savedConfigurationsNames = new ArrayList<String>();
		TableItem[] items = selectedConfigurationViewer.getTable().getItems();

		for (TableItem item : items)
			if (item.getData() instanceof ILaunchConfiguration)
				savedConfigurationsNames.add(((ILaunchConfiguration) item
						.getData()).getName());

		configuration.setAttribute(
				ICompositeConfigurationConstants.ATTR_SELECTED_CONFIGURATIONS,
				savedConfigurationsNames);
	}

	@Override
	public String getName() {
		return Messages.CompositeConfigurationTab_Name;
	}

	@Override
	public boolean canSave() {
		return true;
	}

	@Override
	public boolean isValid(ILaunchConfiguration configuration) {
		if (selectedConfigurationViewer.getTable().getItemCount() == 0) {
			setErrorMessage(Messages.CompositeConfigurationTab_Empty_Error_Message);
			return false;
		}
		setErrorMessage(null);
		return true;
	}

	/**
	 * Implementation of new label provider for configurations
	 */
	private class ConfigurationsLabelProvider extends LabelProvider {

		@Override
		public Image getImage(Object element) {
			return debugModelPresentation
					.getImage(((ILaunchConfiguration) element));
		}

		@Override
		public String getText(Object element) {
			return ((ILaunchConfiguration) element).getName();
		}
	}
}
