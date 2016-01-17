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
package in.koyad.piston.app.usermgmt.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.koyad.piston.core.model.Group;
import org.koyad.piston.core.model.User;

import in.koyad.piston.app.usermgmt.forms.GroupDetailsPluginForm;
import in.koyad.piston.common.utils.BeanPropertyUtils;
import in.koyad.piston.common.utils.LogUtil;

public class PopulateFormUtil {

	private static final LogUtil LOGGER = LogUtil.getLogger(PopulateFormUtil.class);
	
	public static void populateGroupDetails(GroupDetailsPluginForm form, Group group) {
		//copy id, name etc.
		BeanPropertyUtils.copyProperties(form, group);
		
		//copy permissions
		copyAcls(group.getMembers(), form);
	}
	
	private static void copyAcls(Set<User> users, GroupDetailsPluginForm form) {
		List<String> principals = new ArrayList<>();
		for(User user :  users) {
			principals.add("user:" + user.getUid()); 
		}
		
		form.setMembers(principals.toArray(new String[principals.size()]));
	}
	
}
