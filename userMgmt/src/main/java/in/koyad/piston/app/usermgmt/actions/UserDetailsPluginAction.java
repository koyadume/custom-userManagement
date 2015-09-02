/*
 * Copyright (c) 2012-2015 Shailendra Singh <shailendra_01@outlook.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package in.koyad.piston.app.usermgmt.actions;

import org.koyad.piston.core.model.User;

import in.koyad.piston.app.userMgmt.sdk.api.UserManagementService;
import in.koyad.piston.app.usermgmt.forms.UserDetailsPluginForm;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.BeanPropertyUtils;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.ServiceManager;
import in.koyad.piston.controller.plugin.PluginAction;
import in.koyad.piston.controller.plugin.annotations.AnnoPluginAction;
import in.koyad.piston.ui.utils.RequestContextUtil;

@AnnoPluginAction(
	name = UserDetailsPluginAction.ACTION_NAME
)
public class UserDetailsPluginAction extends PluginAction {
	
	private final UserManagementService userManagementService = ServiceManager.getService(UserManagementService.class);
	
	public static final String ACTION_NAME = "userDetails";

	private static final LogUtil LOGGER = LogUtil.getLogger(UserDetailsPluginAction.class);
	
	@Override
	public String execute() throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		String userId = RequestContextUtil.getParameter("id");
		
		if(null != userId) {
			User user = userManagementService.fetchUser(userId);

			UserDetailsPluginForm form = new UserDetailsPluginForm();
			BeanPropertyUtils.copyProperties(form, user);
			
			RequestContextUtil.setRequestAttribute(UserDetailsPluginForm.FORM_NAME, form);
		}
			
		LOGGER.exitMethod("execute");
		return "/pages/userDetails.xml";
	}

}
