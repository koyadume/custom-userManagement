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
package in.koyad.piston.app.userMgmt.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import in.koyad.piston.app.userMgmt.sdk.api.UserManagementService;
import in.koyad.piston.app.userMgmt.sdk.model.Group;
import in.koyad.piston.app.userMgmt.sdk.model.User;
import in.koyad.piston.app.userMgmt.service.utils.DBConstants;
import in.koyad.piston.common.bo.Attribute;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.StringUtil;
import in.koyad.piston.core.dao.GlobalDAO;

public class DBUserManagementServiceImpl implements UserManagementService {
	
	private static final LogUtil LOGGER = LogUtil.getLogger(DBUserManagementServiceImpl.class);
	
	private final GlobalDAO globalDAO = new GlobalDAO(DBConstants.PERSISTENT_UNIT_USERMGMT);
	
	@Override
	public List<User> searchUsers(List<Attribute> atts) throws FrameworkException {
		LOGGER.enterMethod("searchUsers");
		
		Map<String, Object> conditions = atts.stream().collect(Collectors.toMap(Attribute::getName, Attribute::getValue));
		List<User> users = globalDAO.getList(User.class, conditions);
		
		LOGGER.exitMethod("searchUsers");
		return users;
	}
	
	@Override
	public void saveUser(User user) throws FrameworkException {
		LOGGER.enterMethod("saveUser");

		if(StringUtil.isEmpty(user.getId())) {
			globalDAO.insert(user);
		} else {
			globalDAO.update(user);
		}

		LOGGER.exitMethod("saveUser");
	}
	
	@Override
	public User fetchUser(String internalId) throws FrameworkException {
		LOGGER.enterMethod("fetchUser");

		User user = globalDAO.get(User.class, internalId);

		LOGGER.exitMethod("fetchUser");
		return user;
	}
	
	@Override
	public void deleteUsers(List<String> userIds) throws FrameworkException {
		LOGGER.enterMethod("deleteUsers");
		
		Map<String, Object> conditions = new HashMap<>();
		conditions.put("id", userIds);
		globalDAO.deleteEntities(User.class, conditions);
		
		LOGGER.exitMethod("deleteUsers");
	}

	@Override
	public List<Group> searchGroups(List<Attribute> atts) throws FrameworkException {
		LOGGER.enterMethod("searchGroups");
		
		Map<String, Object> conditions = atts.stream().collect(Collectors.toMap(Attribute::getName, Attribute::getValue));
		List<Group> groups = globalDAO.getList(Group.class, conditions);
		
		LOGGER.exitMethod("searchGroups");
		return groups;
	}
	
	@Override
	public Group fetchGroup(String internalId) throws FrameworkException {
		LOGGER.enterMethod("fetchGroup");

		Group group = globalDAO.get(Group.class, internalId);
		
		LOGGER.exitMethod("fetchGroup");
		return group;
	}
	
	@Override
	public void saveGroup(Group group) throws FrameworkException {
		LOGGER.enterMethod("saveGroup");

		if(StringUtil.isEmpty(group.getId())) {
			globalDAO.insert(group);
		} else {
			globalDAO.update(group);
		}

		LOGGER.exitMethod("saveGroup");
	}

	@Override
	public void deleteGroups(List<String> groupIds) throws FrameworkException {
		LOGGER.enterMethod("deleteGroups");
		
		Map<String, Object> conditions = new HashMap<>();
		conditions.put("id", groupIds);
		globalDAO.deleteEntities(Group.class, conditions);
		
		LOGGER.exitMethod("deleteGroups");
	}
	
}
