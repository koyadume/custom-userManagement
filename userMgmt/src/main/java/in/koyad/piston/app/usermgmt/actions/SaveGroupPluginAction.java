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

import org.koyad.piston.core.model.Group;

import in.koyad.piston.app.userMgmt.sdk.api.UserManagementService;
import in.koyad.piston.app.userMgmt.sdk.impl.UserManagementImpl;
import in.koyad.piston.app.usermgmt.forms.GroupDetailsPluginForm;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.BeanPropertyUtils;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.controller.plugin.PluginAction;
import in.koyad.piston.controller.plugin.annotations.AnnoPluginAction;
import in.koyad.piston.ui.utils.FormUtils;
import in.koyad.piston.ui.utils.RequestContextUtil;

@AnnoPluginAction(
	name = SaveGroupPluginAction.ACTION_NAME
)
public class SaveGroupPluginAction extends PluginAction {
	
	private final UserManagementService userManagementService = new UserManagementImpl();
	
	public static final String ACTION_NAME = "saveGroup";

	private static final LogUtil LOGGER = LogUtil.getLogger(SaveGroupPluginAction.class);
	
	@Override
	public String execute() throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		GroupDetailsPluginForm form = FormUtils.createFormWithReqParams(GroupDetailsPluginForm.class);
		Group group = new Group();
		BeanPropertyUtils.copyProperties(group, form);
		
		userManagementService.saveGroup(group);
		
		form.setId(group.getId());
		
		RequestContextUtil.setRequestAttribute(GroupDetailsPluginForm.FORM_NAME, form);
			
		LOGGER.exitMethod("execute");
		return "/pages/groupDetails.xml";
	}

}
