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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.koyad.piston.core.model.Group;
import org.koyad.piston.core.model.User;

import in.koyad.piston.app.userMgmt.sdk.api.UserManagementService;
import in.koyad.piston.app.userMgmt.sdk.impl.UserManagementImpl;
import in.koyad.piston.app.usermgmt.forms.GroupDetailsPluginForm;
import in.koyad.piston.common.bo.Attribute;
import in.koyad.piston.common.constants.MsgType;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.Message;
import in.koyad.piston.common.utils.StringUtil;
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
		try {
			Group group = new Group();
//			BeanPropertyUtils.copyProperties(group, form);
			group.setId(form.getId());
			group.setName(form.getName());
			
			if(null != form.getMembers()) {
				List<User> members = new ArrayList<>();
				for(String member : form.getMembers()) {
					List<Attribute> atts = new ArrayList<>();
					atts.add(new Attribute("uid", member.split(":")[1]));
					
					User user = userManagementService.searchUsers(atts).get(0);
					
					members.add(user);
				}
				group.setMembers(new HashSet<>(members));
			}
			
			userManagementService.saveGroup(group);
			
			if(StringUtil.isEmpty(form.getId())) {
				form.setId(group.getId());
				RequestContextUtil.setRequestAttribute("msg", new Message(MsgType.INFO, "Group created successfully."));
			} else {
				RequestContextUtil.setRequestAttribute("msg", new Message(MsgType.INFO, "Group details updated successfully."));
			}
		} catch(FrameworkException ex) {
			LOGGER.logException(ex);
			
			if(StringUtil.isEmpty(form.getId())) {
				RequestContextUtil.setRequestAttribute("msg", new Message(MsgType.ERROR, "Error occured while creating group."));
			} else {
				RequestContextUtil.setRequestAttribute("msg", new Message(MsgType.ERROR, "Error occured while updating group details."));
			}
		}
		
		RequestContextUtil.setRequestAttribute(GroupDetailsPluginForm.FORM_NAME, form);
			
		LOGGER.exitMethod("execute");
		return "/pages/groupDetails.xml";
	}

}
