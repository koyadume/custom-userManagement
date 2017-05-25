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
import in.koyad.piston.app.identityMgmt.sdk.model.Group;
import in.koyad.piston.app.identiyMgmt.sdk.api.IdentityMgmtService;
import in.koyad.piston.app.usermgmt.forms.GroupDetailsPluginForm;
import in.koyad.piston.app.usermgmt.utils.PopulateFormUtil;
import in.koyad.piston.common.basic.exception.FrameworkException;
import in.koyad.piston.common.util.LogUtil;

@AnnoPluginAction(
	name = GroupDetailsPluginAction.ACTION_NAME
)
public class GroupDetailsPluginAction extends BasePluginAction {
	
	private final IdentityMgmtService identityMgmtClient = new IdentityMgmtClientImpl();
	
	public static final String ACTION_NAME = "groupDetails";

	private static final LogUtil LOGGER = LogUtil.getLogger(GroupDetailsPluginAction.class);
	
	@Override
	public String execute(Request req) throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		
		String groupId = req.getParameter("id");
		
		if(null != groupId) {
			Group group = identityMgmtClient.fetchGroup(groupId);

			GroupDetailsPluginForm form = new GroupDetailsPluginForm();
			PopulateFormUtil.populateGroupDetails(form, group);
			
			req.setAttribute(GroupDetailsPluginForm.FORM_NAME, form);
		}
			
		LOGGER.exitMethod("execute");
		return "/pages/groupDetails.xml";
	}

}
