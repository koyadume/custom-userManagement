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

import java.util.Arrays;

import org.koyad.piston.core.model.enums.PrincipalType;

import in.koyad.piston.app.userMgmt.sdk.api.UserManagementService;
import in.koyad.piston.app.userMgmt.sdk.impl.UserManagementImpl;
import in.koyad.piston.app.usermgmt.forms.DeletePrincipalsPluginForm;
import in.koyad.piston.app.usermgmt.forms.SearchForm;
import in.koyad.piston.common.constants.FrameworkConstants;
import in.koyad.piston.common.constants.MsgType;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.Message;
import in.koyad.piston.controller.plugin.PluginAction;
import in.koyad.piston.controller.plugin.annotations.AnnoPluginAction;
import in.koyad.piston.ui.utils.FormUtils;
import in.koyad.piston.ui.utils.RequestContextUtil;

@AnnoPluginAction(
	name = DeletePrincipalsPluginAction.ACTION_NAME
)
public class DeletePrincipalsPluginAction extends PluginAction {
	
	private final UserManagementService userManagementService = new UserManagementImpl();
	
	public static final String ACTION_NAME = "deletePrincipals";	
	
	private static final LogUtil LOGGER = LogUtil.getLogger(DeletePrincipalsPluginAction.class);
	
	@Override
	protected String execute() throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		SearchForm form = FormUtils.createFormWithReqParams(SearchForm.class);
		DeletePrincipalsPluginForm delForm = FormUtils.createFormWithReqParams(DeletePrincipalsPluginForm.class);;
		try {
			String[] principalIds = delForm.getPrincipalIds();
			
			if(form.getSearchType().equalsIgnoreCase(PrincipalType.USER.name())) {
				userManagementService.deleteUsers(Arrays.asList(principalIds));
			} else if(form.getSearchType().equalsIgnoreCase(PrincipalType.GROUP.name())) {
				userManagementService.deleteGroups(Arrays.asList(principalIds));
			}
			
			RequestContextUtil.setRequestAttribute("msg", new Message(MsgType.INFO, "Users/Groups deleted successfully."));
		} catch(FrameworkException ex) {
			LOGGER.logException(ex);
			RequestContextUtil.setRequestAttribute("msg", new Message(MsgType.ERROR, "Error occured while deleting users/groups."));
			
			RequestContextUtil.setRequestAttribute(DeletePrincipalsPluginForm.FORM_NAME, form);
		}
		LOGGER.exitMethod("execute");
		return FrameworkConstants.PREFIX_FORWARD + SearchResultsPluginAction.ACTION_NAME;
	}

}
