/*
 * Copyright (c) 2012-2017 Shailendra Singh <shailendra_01@outlook.com>
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


import in.koyad.piston.app.api.annotation.AnnoPluginAction;
import in.koyad.piston.app.api.model.Request;
import in.koyad.piston.app.api.plugin.BasePluginAction;
import in.koyad.piston.app.identityMgmt.client.impl.IdentityMgmtClientImpl;
import in.koyad.piston.app.identityMgmt.sdk.model.User;
import in.koyad.piston.app.identiyMgmt.sdk.api.IdentityMgmtService;
import in.koyad.piston.app.usermgmt.forms.UserDetailsPluginForm;
import in.koyad.piston.common.basic.StringUtil;
import in.koyad.piston.common.basic.exception.FrameworkException;
import in.koyad.piston.common.constants.MsgType;
import in.koyad.piston.common.util.BeanPropertyUtils;
import in.koyad.piston.common.util.LogUtil;
import in.koyad.piston.common.util.Message;

@AnnoPluginAction(
	name = SaveUserPluginAction.ACTION_NAME
)
public class SaveUserPluginAction extends BasePluginAction {
	
	private final IdentityMgmtService identityMgmtClient = new IdentityMgmtClientImpl();
	
	public static final String ACTION_NAME = "saveUser";

	private static final LogUtil LOGGER = LogUtil.getLogger(SaveUserPluginAction.class);
	
	@Override
	public String execute(Request req) throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		UserDetailsPluginForm form = req.getPluginForm(UserDetailsPluginForm.class);
		try {
			User user = new User();
			BeanPropertyUtils.copyProperties(user, form);
			
			identityMgmtClient.saveUser(user);
			
			if(StringUtil.isEmpty(form.getId())) {
				form.setId(user.getId());
				req.setAttribute("msg", new Message(MsgType.INFO, "User created successfully."));
			} else {
				req.setAttribute("msg", new Message(MsgType.INFO, "User details updated successfully."));
			}
		} catch(FrameworkException ex) {
			LOGGER.logException(ex);
			
			if(StringUtil.isEmpty(form.getId())) {
				req.setAttribute("msg", new Message(MsgType.ERROR, "Error occured while creating user."));
			} else {
				req.setAttribute("msg", new Message(MsgType.ERROR, "Error occured while updating user details."));
			}
		}
		
		req.setAttribute(UserDetailsPluginForm.FORM_NAME, form);
			
		LOGGER.exitMethod("execute");
		return "/pages/userDetails.xml";
	}

}
