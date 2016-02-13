/*
 * Copyright (c) 2012-2016 Shailendra Singh <shailendra_01@outlook.com>
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

import in.koyad.piston.app.userMgmt.sdk.api.UserManagementService;
import in.koyad.piston.app.userMgmt.sdk.impl.UserManagementImpl;
import in.koyad.piston.app.userMgmt.sdk.model.Group;
import in.koyad.piston.app.usermgmt.forms.GroupDetailsPluginForm;
import in.koyad.piston.app.usermgmt.utils.PopulateFormUtil;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.controller.plugin.PluginAction;
import in.koyad.piston.controller.plugin.annotations.AnnoPluginAction;
import in.koyad.piston.ui.utils.RequestContextUtil;

@AnnoPluginAction(
	name = GroupDetailsPluginAction.ACTION_NAME
)
public class GroupDetailsPluginAction extends PluginAction {
	
	private final UserManagementService userManagementService = new UserManagementImpl();
	
	public static final String ACTION_NAME = "groupDetails";

	private static final LogUtil LOGGER = LogUtil.getLogger(GroupDetailsPluginAction.class);
	
	@Override
	public String execute() throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		
		String groupId = RequestContextUtil.getParameter("id");
		
		if(null != groupId) {
			Group group = userManagementService.fetchGroup(groupId);

			GroupDetailsPluginForm form = new GroupDetailsPluginForm();
			PopulateFormUtil.populateGroupDetails(form, group);
			
			RequestContextUtil.setRequestAttribute(GroupDetailsPluginForm.FORM_NAME, form);
		}
			
		LOGGER.exitMethod("execute");
		return "/pages/groupDetails.xml";
	}

}
