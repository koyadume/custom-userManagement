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
import in.koyad.piston.app.userMgmt.sdk.model.User;
import in.koyad.piston.app.usermgmt.forms.UserDetailsPluginForm;
import in.koyad.piston.common.constants.MsgType;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.BeanPropertyUtils;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.Message;
import in.koyad.piston.common.utils.StringUtil;
import in.koyad.piston.controller.plugin.PluginAction;
import in.koyad.piston.controller.plugin.annotations.AnnoPluginAction;
import in.koyad.piston.ui.utils.FormUtils;
import in.koyad.piston.ui.utils.RequestContextUtil;

@AnnoPluginAction(
	name = SaveUserPluginAction.ACTION_NAME
)
public class SaveUserPluginAction extends PluginAction {
	
	private final UserManagementService userManagementService = new UserManagementImpl();
	
	public static final String ACTION_NAME = "saveUser";

	private static final LogUtil LOGGER = LogUtil.getLogger(SaveUserPluginAction.class);
	
	@Override
	public String execute() throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		UserDetailsPluginForm form = FormUtils.createFormWithReqParams(UserDetailsPluginForm.class);
		try {
			User user = new User();
			BeanPropertyUtils.copyProperties(user, form);
			
			userManagementService.saveUser(user);
			
			if(StringUtil.isEmpty(form.getId())) {
				form.setId(user.getId());
				RequestContextUtil.setRequestAttribute("msg", new Message(MsgType.INFO, "User created successfully."));
			} else {
				RequestContextUtil.setRequestAttribute("msg", new Message(MsgType.INFO, "User details updated successfully."));
			}
		} catch(FrameworkException ex) {
			LOGGER.logException(ex);
			
			if(StringUtil.isEmpty(form.getId())) {
				RequestContextUtil.setRequestAttribute("msg", new Message(MsgType.ERROR, "Error occured while creating user."));
			} else {
				RequestContextUtil.setRequestAttribute("msg", new Message(MsgType.ERROR, "Error occured while updating user details."));
			}
		}
		
		RequestContextUtil.setRequestAttribute(UserDetailsPluginForm.FORM_NAME, form);
			
		LOGGER.exitMethod("execute");
		return "/pages/userDetails.xml";
	}

}
