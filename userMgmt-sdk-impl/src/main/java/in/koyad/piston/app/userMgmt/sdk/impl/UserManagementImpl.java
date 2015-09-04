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
package in.koyad.piston.app.userMgmt.sdk.impl;

import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.koyad.piston.core.model.Group;
import org.koyad.piston.core.model.User;

import com.google.common.base.Joiner;

import in.koyad.piston.app.userMgmt.sdk.api.UserManagementService;
import in.koyad.piston.common.bo.Attribute;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.AbstractREST;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.StringUtil;

public class UserManagementImpl extends AbstractREST implements UserManagementService {
	
	private static final LogUtil LOGGER = LogUtil.getLogger(UserManagementImpl.class);
	
	@Override
	public List<User> searchUsers(List<Attribute> atts) throws FrameworkException {
		LOGGER.enterMethod("searchUsers");
		
		List<User> users = null;
		try {
			String resource = MessageFormat.format("/userMgmt-service/{0}/users", ServiceConstants.VERSION);
			String query = Joiner.on('&').join(atts.stream().map(attr -> attr.getName().concat("=").concat(attr.getValue())).collect(Collectors.toList()));
			
			users = get(resource, query, List.class);
		} catch(Exception ex) {
			LOGGER.logException(ex);
			throw new FrameworkException(ex.getMessage());
		}
		
		LOGGER.exitMethod("searchUsers");
		return users;
	}

	@Override
	public User fetchUser(String internalId) throws FrameworkException {
		LOGGER.enterMethod("fetchUser");

		User user = null;
		try {
			user = get(MessageFormat.format("/userMgmt-service/{0}/users/{1}", ServiceConstants.VERSION, internalId), User.class);
		} catch(Exception ex) {
			LOGGER.logException(ex);
			throw new FrameworkException(ex.getMessage());
		}
		
		LOGGER.exitMethod("fetchUser");
		return user;
	}
	
	@Override
	public void saveUser(User user) throws FrameworkException {
		LOGGER.enterMethod("saveUser");

		try {
			if(StringUtil.isEmpty(user.getId())) {
				post(MessageFormat.format("/userMgmt-service/{0}/users", ServiceConstants.VERSION), user);
			} else {
				put(MessageFormat.format("/userMgmt-service/{0}/users/{1}", ServiceConstants.VERSION, user.getId()), user);
			}
		} catch(Exception ex) {
			LOGGER.logException(ex);
			throw new FrameworkException(ex.getMessage());
		}
		
		LOGGER.exitMethod("saveUser");
	}
	
	@Override
	public void deleteUsers(List<String> userIds) throws FrameworkException {
		LOGGER.enterMethod("deleteUsers");
		
		try {
			String query = MessageFormat.format("userIds={0}", Joiner.on(',').join(userIds));
			delete(MessageFormat.format("/userMgmt-service/{0}/users", ServiceConstants.VERSION), query);
		} catch(Exception ex) {
			LOGGER.logException(ex);
			throw new FrameworkException(ex.getMessage());
		}
		
		LOGGER.exitMethod("deleteUsers");
	}
	
	@Override
	public List<Group> searchGroups(List<Attribute> atts) throws FrameworkException {
		LOGGER.enterMethod("searchGroups");
		
		List<Group> groups = null;
		try {
			String resource = MessageFormat.format("/userMgmt-service/{0}/groups", ServiceConstants.VERSION);
			String query = Joiner.on('&').join(atts.stream().map(attr -> attr.getName().concat("=").concat(attr.getValue())).collect(Collectors.toList()));
			
			groups = get(resource, query, List.class);
		} catch(Exception ex) {
			LOGGER.logException(ex);
			throw new FrameworkException(ex.getMessage());
		}
		
		LOGGER.exitMethod("searchGroups");
		return groups;
	}
	
	@Override
	public Group fetchGroup(String internalId) throws FrameworkException {
		LOGGER.enterMethod("fetchGroup");

		Group group = null;
		try {
			group = get(MessageFormat.format("/userMgmt-service/{0}/groups/{1}", ServiceConstants.VERSION, internalId), Group.class);
		} catch(Exception ex) {
			LOGGER.logException(ex);
			throw new FrameworkException(ex.getMessage());
		}
		
		LOGGER.exitMethod("fetchGroup");
		return group;
	}
	
	@Override
	public void saveGroup(Group group) throws FrameworkException {
		LOGGER.enterMethod("saveGroup");
		
		try {
			if(StringUtil.isEmpty(group.getId())) {
				post(MessageFormat.format("/userMgmt-service/{0}/groups", ServiceConstants.VERSION), group);
			} else {
				put(MessageFormat.format("/userMgmt-service/{0}/groups/{1}", ServiceConstants.VERSION, group.getId()), group);
			}
		} catch(Exception ex) {
			LOGGER.logException(ex);
			throw new FrameworkException(ex.getMessage());
		}

		LOGGER.exitMethod("saveGroup");
	}

	@Override
	public void deleteGroups(List<String> groupIds) throws FrameworkException {
		LOGGER.enterMethod("deleteGroups");
		
		try {
			String query = MessageFormat.format("groupIds={0}", Joiner.on(',').join(groupIds));
			delete(MessageFormat.format("/userMgmt-service/{0}/groups", ServiceConstants.VERSION), query);
		} catch(Exception ex) {
			LOGGER.logException(ex);
			throw new FrameworkException(ex.getMessage());
		}
		
		LOGGER.exitMethod("deleteGroups");
	}
	
}
