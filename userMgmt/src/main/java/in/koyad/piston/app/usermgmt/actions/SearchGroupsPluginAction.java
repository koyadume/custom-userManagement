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
import java.util.Arrays;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;

import in.koyad.piston.common.bo.Attribute;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.StringUtil;
import in.koyad.piston.controller.plugin.PluginAction;
import in.koyad.piston.controller.plugin.annotations.AnnoPluginAction;
import in.koyad.piston.servicedelegate.model.UserGroupUtil;
import in.koyad.piston.ui.utils.RequestContextUtil;

@AnnoPluginAction(
		name = SearchGroupsPluginAction.ACTION_NAME
)
public class SearchGroupsPluginAction extends PluginAction {
	
	public static final String ACTION_NAME = "searchGroups";

	private static final LogUtil LOGGER = LogUtil.getLogger(SearchGroupsPluginAction.class);
	
	@Override
	public String execute() throws FrameworkException {
		LOGGER.enterMethod("execute");
	
		if(!StringUtil.isEmpty(RequestContextUtil.getParameter("searchAttr"))) {
			try {
				SearchGroupsForm form = new SearchGroupsForm();
				BeanUtils.populate(form, RequestContextUtil.getRequest().getParameterMap());
				
				Attribute attr = new Attribute(form.getSearchAttr(), form.getSearchAttrValue());
				List<Attribute> atts = new ArrayList<>();
				atts.add(attr);
				
				String[] names = UserGroupUtil.getGroupsByAttributes(atts);
				RequestContextUtil.setRequestAttribute("names", Arrays.asList(names));
			} catch(Exception ex) {
				LOGGER.logException(ex);
			}
		}
		
		LOGGER.exitMethod("execute");
		return "/pages/searchGroups.xml";
	}

}
