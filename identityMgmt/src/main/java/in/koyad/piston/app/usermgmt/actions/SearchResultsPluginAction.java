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
import java.util.List;

import in.koyad.piston.app.api.annotation.AnnoPluginAction;
import in.koyad.piston.app.api.model.Request;
import in.koyad.piston.app.api.plugin.BasePluginAction;
import in.koyad.piston.app.identityMgmt.client.impl.IdentityMgmtClientImpl;
import in.koyad.piston.app.identityMgmt.sdk.model.Group;
import in.koyad.piston.app.identityMgmt.sdk.model.QueryAttribute;
import in.koyad.piston.app.identityMgmt.sdk.model.User;
import in.koyad.piston.app.identiyMgmt.sdk.api.IdentityMgmtService;
import in.koyad.piston.app.usermgmt.forms.SearchForm;
import in.koyad.piston.common.basic.exception.FrameworkException;
import in.koyad.piston.common.basic.model.enums.PrincipalType;
import in.koyad.piston.common.util.LogUtil;

@AnnoPluginAction(
		name = SearchResultsPluginAction.ACTION_NAME
)
public class SearchResultsPluginAction extends BasePluginAction {
	
	public static final String ACTION_NAME = "searchResults";

	private static final LogUtil LOGGER = LogUtil.getLogger(SearchResultsPluginAction.class);
	
	private final IdentityMgmtService identityMgmtClient = new IdentityMgmtClientImpl();
	
	@Override
	public String execute(Request req) throws FrameworkException {
		LOGGER.enterMethod("execute");
	
		SearchForm form = req.getPluginForm(SearchForm.class);
		try {
			QueryAttribute attr = QueryAttribute.builder().name(form.getSearchAttr()).value(form.getSearchAttrValue()).build();
			List<QueryAttribute> atts = new ArrayList<>();
			atts.add(attr);
			
			form = req.getPluginForm(SearchForm.class);
			if(form.getSearchType().equalsIgnoreCase(PrincipalType.USER.name())) {
				List<User> users = identityMgmtClient.searchUsers(atts);
				req.setAttribute("users", users);
			} else if(form.getSearchType().equalsIgnoreCase(PrincipalType.GROUP.name())) {
				List<Group> groups = identityMgmtClient.searchGroups(atts);
				req.setAttribute("groups", groups);
			}
			
			req.setAttribute("searchType", form.getSearchType());
		} catch(Exception ex) {
			LOGGER.logException(ex);
		}
		
		LOGGER.exitMethod("execute");
		return "/ajax/principals.xml";
	}

}
