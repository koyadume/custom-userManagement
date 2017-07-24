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

import java.util.Arrays;

import in.koyad.piston.app.api.annotation.AnnoPluginAction;
import in.koyad.piston.app.api.model.Request;
import in.koyad.piston.app.api.model.Response;
import in.koyad.piston.app.api.plugin.BasePluginAction;
import in.koyad.piston.app.identityMgmt.client.impl.IdentityMgmtClientImpl;
import in.koyad.piston.app.identiyMgmt.sdk.api.IdentityMgmtService;
import in.koyad.piston.app.usermgmt.forms.DeletePrincipalsPluginForm;
import in.koyad.piston.app.usermgmt.forms.SearchForm;
import in.koyad.piston.common.basic.constant.FrameworkConstants;
import in.koyad.piston.common.basic.exception.FrameworkException;
import in.koyad.piston.common.basic.model.enums.PrincipalType;
import in.koyad.piston.common.constants.MsgType;
import in.koyad.piston.common.util.LogUtil;
import in.koyad.piston.common.util.Message;

@AnnoPluginAction(
	name = DeletePrincipalsPluginAction.ACTION_NAME
)
public class DeletePrincipalsPluginAction extends BasePluginAction {
	
	private final IdentityMgmtService identityMgmtClient = new IdentityMgmtClientImpl();
	
	public static final String ACTION_NAME = "deletePrincipals";	
	
	private static final LogUtil LOGGER = LogUtil.getLogger(DeletePrincipalsPluginAction.class);
	
	@Override
	public String execute(Request req, Response resp) throws FrameworkException {
		LOGGER.enterMethod("execute");
		
		SearchForm form = req.getPluginForm(SearchForm.class);
		DeletePrincipalsPluginForm delForm = req.getPluginForm(DeletePrincipalsPluginForm.class);;
		try {
			String[] principalIds = delForm.getPrincipalIds();
			
			if(form.getSearchType().equalsIgnoreCase(PrincipalType.USER.name())) {
				identityMgmtClient.deleteUsers(Arrays.asList(principalIds));
			} else if(form.getSearchType().equalsIgnoreCase(PrincipalType.GROUP.name())) {
				identityMgmtClient.deleteGroups(Arrays.asList(principalIds));
			}
			
			req.setAttribute("msg", new Message(MsgType.INFO, "Users/Groups deleted successfully."));
		} catch(FrameworkException ex) {
			LOGGER.logException(ex);
			req.setAttribute("msg", new Message(MsgType.ERROR, "Error occured while deleting users/groups."));
			
			req.setAttribute(DeletePrincipalsPluginForm.FORM_NAME, form);
		}
		LOGGER.exitMethod("execute");
		return FrameworkConstants.PREFIX_FORWARD + SearchResultsPluginAction.ACTION_NAME;
	}

}
