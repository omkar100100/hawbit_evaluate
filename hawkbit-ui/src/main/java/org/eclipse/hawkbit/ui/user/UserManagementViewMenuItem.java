package org.eclipse.hawkbit.ui.user;

import java.util.Arrays;
import java.util.List;

import org.eclipse.hawkbit.im.authentication.SpPermission;
import org.eclipse.hawkbit.ui.management.AbstractDashboardMenuItemNotification;
import org.springframework.core.annotation.Order;

import com.vaadin.server.FontAwesome;
import com.vaadin.server.Resource;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;

@SpringComponent
@UIScope
@Order(900)
public class UserManagementViewMenuItem  extends AbstractDashboardMenuItemNotification{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String getViewName() {
		return UserManagementView.VIEW_NAME;
	}

	@Override
	public Resource getDashboardIcon() {
		return FontAwesome.USERS;
	}

	@Override
	public String getDashboardCaption() {
		return "User Management";
	}

	@Override
	public String getDashboardCaptionLong() {
		return "User Management";
	}

	@Override
	public List<String> getPermissions() {
		return Arrays.asList(SpPermission.USER_MANAGEMENT);
	}

}
