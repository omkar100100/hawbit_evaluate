package org.eclipse.hawkbit.ui.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.eclipse.hawkbit.repository.TargetFilterQueryManagement;
import org.eclipse.hawkbit.repository.TargetManagement;
import org.eclipse.hawkbit.ui.components.SPUIComponentProvider;
import org.eclipse.hawkbit.ui.decorators.SPUIButtonStyleSmallNoBorder;
import org.eclipse.hawkbit.ui.distributions.state.ManageDistUIState;
import org.eclipse.hawkbit.ui.filtermanagement.DistributionSetSelectWindow;
import org.eclipse.hawkbit.ui.filtermanagement.TargetFilterBeanQuery;
import org.eclipse.hawkbit.ui.filtermanagement.state.FilterManagementUIState;
import org.eclipse.hawkbit.ui.utils.SPUIDefinitions;
import org.eclipse.hawkbit.ui.utils.SPUILabelDefinitions;
import org.eclipse.hawkbit.ui.utils.TableColumn;
import org.eclipse.hawkbit.ui.utils.UIComponentIdProvider;
import org.eclipse.hawkbit.ui.utils.UINotification;
import org.eclipse.hawkbit.ui.utils.VaadinMessageSource;
import org.vaadin.addons.lazyquerycontainer.BeanQueryFactory;
import org.vaadin.addons.lazyquerycontainer.LazyQueryContainer;
import org.vaadin.addons.lazyquerycontainer.LazyQueryDefinition;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBus.UIEventBus;

import com.google.common.collect.Maps;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.ui.Button;
import com.vaadin.ui.Link;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.ValoTheme;

public class UserManagementTable extends Table {

	private final VaadinMessageSource i18n;

	private final UINotification notification;

	private final transient EventBus.UIEventBus eventBus;

	private final FilterManagementUIState filterManagementUIState;

	private final transient TargetFilterQueryManagement targetFilterQueryManagement;

	// private final DistributionSetSelectWindow dsSelectWindow;

	private Container container;

	private static final int PROPERTY_DEPT = 3;

	public UserManagementTable(final VaadinMessageSource i18n, final UINotification notification,
			final UIEventBus eventBus, final FilterManagementUIState filterManagementUIState,
			final TargetFilterQueryManagement targetFilterQueryManagement) {
		this.i18n = i18n;
		this.notification = notification;
		this.eventBus = eventBus;
		this.filterManagementUIState = filterManagementUIState;
		this.targetFilterQueryManagement = targetFilterQueryManagement;
		/*
		 * this.dsSelectWindow = new DistributionSetSelectWindow(i18n, eventBus,
		 * targetManagement, targetFilterQueryManagement, manageDistUIState);
		 */

		setStyleName("sp-table");
		setSizeFull();
		setImmediate(true);
		setHeight(100.0F, Unit.PERCENTAGE);
		addStyleName(ValoTheme.TABLE_NO_VERTICAL_LINES);
		addStyleName(ValoTheme.TABLE_SMALL);
		addCustomGeneratedColumns();
		populateTableData();
		setColumnCollapsingAllowed(true);
		setColumnProperties();
		setId(UIComponentIdProvider.TARGET_FILTER_TABLE_ID);
		// eventBus.subscribe(this);
	}

	private void populateTableData() {
		container = createContainer();
		addContainerproperties();
		setContainerDataSource(container);
		setColumnProperties();

	}
	
	private List<TableColumn> getVisbleColumns() {
        final List<TableColumn> columnList = new ArrayList<>(7);
        columnList.add(new TableColumn("UserName", i18n.getMessage("header.name"), 0.2F));
        columnList.add(new TableColumn("Password", i18n.getMessage("header.password"), 0.2F));
        columnList.add(new TableColumn(SPUILabelDefinitions.VAR_CREATED_USER, i18n.getMessage("header.createdBy"), 0.1F));
        columnList.add(new TableColumn(SPUILabelDefinitions.VAR_CREATED_DATE, i18n.getMessage("header.createdDate"), 0.2F));
        columnList.add(new TableColumn(SPUILabelDefinitions.VAR_MODIFIED_BY, i18n.getMessage("header.modifiedBy"), 0.1F));
        columnList.add(new TableColumn(SPUILabelDefinitions.VAR_MODIFIED_DATE, i18n.getMessage("header.modifiedDate"), 0.2F));
       /* columnList.add(new TableColumn(SPUILabelDefinitions.AUTO_ASSIGN_DISTRIBUTION_SET,
                i18n.getMessage("header.auto.assignment.ds"), 0.1F));*/
        columnList.add(new TableColumn(SPUIDefinitions.CUSTOM_FILTER_DELETE, i18n.getMessage("header.delete"), 0.1F));
        return columnList;

    }
	
	  private void setColumnProperties() {
	        setVisibleColumns(getVisbleColumns().stream().map(column -> {
	            setColumnHeader(column.getColumnPropertyId(), column.getColumnHeader());
	            setColumnExpandRatio(column.getColumnPropertyId(), column.getExpandRatio());
	            return column.getColumnPropertyId();
	        }).toArray());
	    }

	 private void addContainerproperties() {
	        /* Create HierarchicalContainer container */
	        container.addContainerProperty("UserName", String.class, null);
	        container.addContainerProperty("Password", String.class, null);
	        container.addContainerProperty(SPUILabelDefinitions.VAR_CREATED_USER, String.class, null);
	        container.addContainerProperty(SPUILabelDefinitions.VAR_CREATED_DATE, Date.class, null);
	        container.addContainerProperty(SPUILabelDefinitions.VAR_MODIFIED_DATE, Date.class, null);
	        container.addContainerProperty(SPUILabelDefinitions.VAR_MODIFIED_BY, String.class, null);
	        //container.addContainerProperty(SPUILabelDefinitions.AUTO_ASSIGN_DISTRIBUTION_SET, String.class, null);
	    }

	private Container createContainer() {
		final Map<String, Object> queryConfig = prepareQueryConfigFilters();
		final BeanQueryFactory<TargetFilterBeanQuery> targetQF = new BeanQueryFactory<>(TargetFilterBeanQuery.class);

		targetQF.setQueryConfiguration(queryConfig);
		// create lazy query container with lazy defination and query
		final LazyQueryContainer targetFilterContainer = new LazyQueryContainer(
				new LazyQueryDefinition(true, SPUIDefinitions.PAGE_SIZE, SPUILabelDefinitions.VAR_ID), targetQF);
		targetFilterContainer.getQueryView().getQueryDefinition().setMaxNestedPropertyDepth(PROPERTY_DEPT);

		return targetFilterContainer;

	}

	private Map<String, Object> prepareQueryConfigFilters() {
		final Map<String, Object> queryConfig = Maps.newHashMapWithExpectedSize(1);
		filterManagementUIState.getCustomFilterSearchText()
				.ifPresent(value -> queryConfig.put(SPUIDefinitions.FILTER_BY_TEXT, value));
		return queryConfig;
	}

	protected void addCustomGeneratedColumns() {
		addGeneratedColumn(SPUIDefinitions.CUSTOM_FILTER_DELETE,
				(source, itemId, columnId) -> getDeleteButton((Long) itemId));

		addGeneratedColumn(SPUILabelDefinitions.NAME,
				(source, itemId, columnId) -> customFilterDetailButton((Long) itemId));

		/*
		 * addGeneratedColumn(SPUILabelDefinitions.AUTO_ASSIGN_DISTRIBUTION_SET,
		 * (source, itemId, columnId) ->
		 * customFilterDistributionSetButton((Long) itemId));
		 */

	}

	private Button customFilterDetailButton(final Long itemId) {
		final Item row1 = getItem(itemId);
		final String tfName = (String) row1.getItemProperty(SPUILabelDefinitions.NAME).getValue();

		final Button updateIcon = SPUIComponentProvider.getButton(getDetailLinkId(tfName), tfName,
				SPUILabelDefinitions.UPDATE_CUSTOM_FILTER, null, false, null, SPUIButtonStyleSmallNoBorder.class);
		updateIcon.setData(tfName);
		updateIcon.addStyleName(ValoTheme.LINK_SMALL + " " + "on-focus-no-border link");
		// updateIcon.addClickListener(this::onClickOfDetailButton);
		return updateIcon;
	}

	private static String getDetailLinkId(final String filterName) {
		return new StringBuilder(UIComponentIdProvider.CUSTOM_FILTER_DETAIL_LINK).append('.').append(filterName)
				.toString();
	}

	private Button getDeleteButton(final Long itemId) {
		final Item row = getItem(itemId);
		final String tfName = (String) row.getItemProperty(SPUILabelDefinitions.NAME).getValue();
		final Button deleteIcon = SPUIComponentProvider.getButton(getDeleteIconId(tfName), "",
				SPUILabelDefinitions.DELETE_CUSTOM_FILTER, ValoTheme.BUTTON_TINY + " " + "blueicon", true,
				FontAwesome.TRASH_O, SPUIButtonStyleSmallNoBorder.class);
		deleteIcon.setData(itemId);
		// deleteIcon.addClickListener(this::onDelete);
		return deleteIcon;
	}

	private static String getDeleteIconId(final String targetFilterName) {
		return new StringBuilder(UIComponentIdProvider.CUSTOM_FILTER_DELETE_ICON).append('.').append(targetFilterName)
				.toString();
	}

}
