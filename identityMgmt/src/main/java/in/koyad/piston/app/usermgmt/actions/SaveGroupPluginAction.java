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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import in.koyad.piston.app.api.annotation.AnnoPluginAction;
import in.koyad.piston.app.api.model.Request;
import in.koyad.piston.app.api.model.Response;
import in.koyad.piston.app.api.plugin.BasePluginAction;
import in.koyad.piston.app.identityMgmt.client.impl.IdentityMgmtClientImpl;
import in.koyad.piston.app.identityMgmt.sdk.model.Group;
import in.koyad.piston.app.identityMgmt.sdk.model.QueryAttribute;
import in.koyad.piston.app.identityMgmt.sdk.model.User;
import in.koyad.piston.app.identiyMgmt.sdk.api.IdentityMgmtService;
import in.koyad.piston.app.usermgmt.forms.GroupDetailsPluginForm;
import in.koyad.piston.common.basic.StringUtil;
import in.koyad.piston.common.basic.exception.FrameworkException;
import in.koyad.piston.common.constants.MsgType;
import in.koyad.piston.common.util.LogUtil;
import in.koyad.piston.common.util.Message;

@AnnoPluginAction(
	name = SaveGroupPluginAction.ACTION_NAME
)
public class SaveGroupPluginAction extends BasePluginAction {
	
	private final IdentityMgmtService identityMgmtClient = new IdentityMgmtClientImpl();
	
	public static final String ACTION_NAME = "saveGroup";

	private static final LogUtil LOGGER = LogUtil.getLogger(SaveGroupPluginAction.class);
	
	@Override
	public String execute(Request req, Response resp) throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		GroupDetailsPluginForm form = req.getPluginForm(GroupDetailsPluginForm.class);
		try {
			Group group = new Group();
			group.setId(form.getId());
			group.setName(form.getName());
			
			if(null != form.getMembers()) {
				List<User> members = new ArrayList<>();
				for(String member : form.getMembers()) {
					List<QueryAttribute> atts = new ArrayList<>();
					atts.add(QueryAttribute.builder().name("uid").value(member.split(":")[1]).build());
					
					User user = identityMgmtClient.searchUsers(atts).get(0);
					
					members.add(user);
				}
				group.setMembers(new HashSet<>(members));
			}
			
			identityMgmtClient.saveGroup(group);
			
			if(StringUtil.isEmpty(form.getId())) {
				form.setId(group.getId());
				req.setAttribute("msg", new Message(MsgType.INFO, "Group created successfully."));
			} else {
				req.setAttribute("msg", new Message(MsgType.INFO, "Group details updated successfully."));
			}
		} catch(FrameworkException ex) {
			LOGGER.logException(ex);
			
			if(StringUtil.isEmpty(form.getId())) {
				req.setAttribute("msg", new Message(MsgType.ERROR, "Error occured while creating group."));
			} else {
				req.setAttribute("msg", new Message(MsgType.ERROR, "Error occured while updating group details."));
			}
		}
		
		req.setAttribute(GroupDetailsPluginForm.FORM_NAME, form);
			
		LOGGER.exitMethod("execute");
		return "/groupDetails.xml";
	}

}
