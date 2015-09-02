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
import java.util.List;

import org.koyad.piston.core.model.Group;
import org.koyad.piston.core.model.User;
import org.koyad.piston.core.model.enums.PrincipalType;

import in.koyad.piston.app.userMgmt.sdk.api.UserManagementService;
import in.koyad.piston.app.usermgmt.forms.SearchForm;
import in.koyad.piston.common.bo.Attribute;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.ServiceManager;
import in.koyad.piston.controller.plugin.PluginAction;
import in.koyad.piston.controller.plugin.annotations.AnnoPluginAction;
import in.koyad.piston.ui.utils.FormUtils;
import in.koyad.piston.ui.utils.RequestContextUtil;

@AnnoPluginAction(
		name = SearchResultsPluginAction.ACTION_NAME
)
public class SearchResultsPluginAction extends PluginAction {
	
	public static final String ACTION_NAME = "searchResults";

	private static final LogUtil LOGGER = LogUtil.getLogger(SearchResultsPluginAction.class);
	
	private final UserManagementService userManagementService = ServiceManager.getService(UserManagementService.class);
	
	@Override
	public String execute() throws FrameworkException {
		LOGGER.enterMethod("execute");
	
		SearchForm form = null;
		try {
			Attribute attr = new Attribute(form.getSearchAttr(), form.getSearchAttrValue());
			List<Attribute> atts = new ArrayList<>();
			atts.add(attr);
			
			form = FormUtils.createFormWithReqParams(SearchForm.class);
			if(form.getSearchType().equalsIgnoreCase(PrincipalType.USER.name())) {
				List<User> users = userManagementService.searchUsers(atts);
				RequestContextUtil.setRequestAttribute("users", users);
			} else if(form.getSearchType().equalsIgnoreCase(PrincipalType.GROUP.name())) {
				List<Group> groups = userManagementService.searchGroups(atts);
				RequestContextUtil.setRequestAttribute("groups", groups);
			}
			
			RequestContextUtil.setRequestAttribute("searchType", form.getSearchType());
		} catch(Exception ex) {
			LOGGER.logException(ex);
		}
		
		LOGGER.exitMethod("execute");
		return "/pages/principals.xml";
	}

}
