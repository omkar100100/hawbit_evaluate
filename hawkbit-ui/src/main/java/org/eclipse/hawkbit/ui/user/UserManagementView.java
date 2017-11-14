package org.eclipse.hawkbit.ui.user;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.eclipse.hawkbit.repository.TargetFilterQueryManagement;
import org.eclipse.hawkbit.ui.AbstractHawkbitUI;
import org.eclipse.hawkbit.ui.SpPermissionChecker;
import org.eclipse.hawkbit.ui.components.AbstractNotificationView;
import org.eclipse.hawkbit.ui.components.NotificationUnreadButton;
import org.eclipse.hawkbit.ui.components.RefreshableContainer;
import org.eclipse.hawkbit.ui.distributions.DistributionsView;
import org.eclipse.hawkbit.ui.filtermanagement.TargetFilterHeader;
import org.eclipse.hawkbit.ui.filtermanagement.TargetFilterTable;
import org.eclipse.hawkbit.ui.filtermanagement.state.FilterManagementUIState;
import org.eclipse.hawkbit.ui.menu.DashboardMenuItem;
import org.eclipse.hawkbit.ui.utils.UINotification;
import org.eclipse.hawkbit.ui.utils.VaadinMessageSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.events.EventBus.UIEventBus;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.Page.BrowserWindowResizeEvent;
import com.vaadin.server.Page.BrowserWindowResizeListener;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;

@UIScope
@SpringView(name = UserManagementView.VIEW_NAME, ui = AbstractHawkbitUI.class)
public class UserManagementView extends VerticalLayout implements View {
	private static final long serialVersionUID = 1L;
	
	public static final String VIEW_NAME = "userManagement";

    private final TargetFilterHeader targetFilterHeader;

    private final UserManagementTable userManagementTable;
    
    private final FilterManagementUIState filterManagementUIState;
	
	@Autowired
	UserManagementView(final VaadinMessageSource i18n, final UIEventBus eventBus,
            final FilterManagementUIState filterManagementUIState, final TargetFilterQueryManagement targetFilterQueryManagement, final SpPermissionChecker permissionChecker,
            final UINotification notification){
		 this.targetFilterHeader = new TargetFilterHeader(eventBus, filterManagementUIState, permissionChecker);
		 
		 this.userManagementTable = new UserManagementTable(i18n, notification, eventBus, filterManagementUIState,
	                targetFilterQueryManagement);
		 
		 this.filterManagementUIState = filterManagementUIState;
	}

	 @PostConstruct
	    void init() {
	        setSizeFull();
	        setImmediate(true);
	        buildLayout();
	       // eventBus.subscribe(this);
	    }

	 private void buildLayout() {
	        setSizeFull();
	        setSpacing(false);
	        setMargin(false);
	        /*if (filterManagementUIState.isCreateFilterViewDisplayed()) {
	            viewCreateTargetFilterLayout();
	        } else if (filterManagementUIState.isEditViewDisplayed()) {
	            viewTargetFilterDetailLayout();
	        } else {
	            viewListView();
	        }*/
	        
	        viewListView();
	    }
	

	 private void viewListView() {
	        removeAllComponents();
	        final VerticalLayout tableListViewLayout = new VerticalLayout();
	        tableListViewLayout.setSizeFull();
	        tableListViewLayout.setSpacing(false);
	        tableListViewLayout.setMargin(false);
	        tableListViewLayout.setStyleName("table-layout");
	        tableListViewLayout.addComponent(targetFilterHeader);
	        tableListViewLayout.setComponentAlignment(targetFilterHeader, Alignment.TOP_CENTER);
	        tableListViewLayout.addComponent(userManagementTable);
	        tableListViewLayout.setComponentAlignment(userManagementTable, Alignment.TOP_CENTER);
	        tableListViewLayout.setExpandRatio(userManagementTable, 1.0F);
	        addComponent(tableListViewLayout);
	    }
	 
	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}



	
}