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
package in.koyad.piston.app.identityMgmt.service.identityStore.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import in.koyad.piston.app.identityMgmt.sdk.model.Group;
import in.koyad.piston.app.identityMgmt.sdk.model.QueryAttribute;
import in.koyad.piston.app.identityMgmt.sdk.model.User;
import in.koyad.piston.app.identityMgmt.service.identityStore.api.IdentityStoreService;
import in.koyad.piston.app.identityMgmt.service.identityStore.model.GroupBO;
import in.koyad.piston.app.identityMgmt.service.identityStore.model.UserBO;
import in.koyad.piston.app.identityMgmt.service.identityStore.utils.DBConstants;
import in.koyad.piston.common.basic.StringUtil;
import in.koyad.piston.common.basic.exception.FrameworkException;
import in.koyad.piston.common.constants.UserExternalAttributes;
import in.koyad.piston.common.util.LogUtil;
import in.koyad.piston.dao.GlobalDAO;


public class DBIdentityStoreServiceImpl implements IdentityStoreService {
	
	private static final LogUtil LOGGER = LogUtil.getLogger(DBIdentityStoreServiceImpl.class);
	
	private final GlobalDAO globalDAO = new GlobalDAO(DBConstants.PERSISTENT_UNIT_USERMGMT);
	
	@Override
	public UserBO getUser(String username) throws FrameworkException {
		LOGGER.enterMethod("getUser");

		UserBO user = null;
		Map<String, Object> filters = new HashMap<>();
		filters.put("uid", username);
		List<User> externalUsers = globalDAO.getList(User.class, filters);
		if(externalUsers.size() == 1) {
			User externalUser = externalUsers.get(0);
			user = fromDBUser(externalUser);
		} else {
			throw new FrameworkException("User not found.");
		}
		
		LOGGER.exitMethod("getUser");
		return user;
	}
	
	private UserBO fromDBUser(User externalUser) {
		UserBO user = new UserBO();
		user.setExternalId(externalUser.getId());
		
		Map<String, String> attributes = new HashMap<>();
		user.setAttributes(attributes);
		
		String name = externalUser.getFirstName();
		if(!StringUtil.isEmpty(externalUser.getLastName())) {
			name = name.concat(" ").concat(externalUser.getLastName());
		}
		attributes.put(UserExternalAttributes.NAME, name);
		attributes.put(UserExternalAttributes.EMAIL, externalUser.getEmail());
		
		return user;
	}
	
	/**
	 * This will return all (in)direct groups which a user or group belongs to.
	 * 
	 * @param userExternalId - user id
	 * @return - all (in)direct groups which a user belongs to.
	 * @throws FrameworkException
	 */
	@Override
	public Set<GroupBO> getMembership(String userExternalId) throws FrameworkException {
		LOGGER.enterMethod("getMembership");

		Set<GroupBO> groups = new HashSet<>();
		
		Map<String, Object> conditions = new HashMap<>();
		conditions.put("user_id", userExternalId);
		
		List<String> groupIds = globalDAO.getTableSingleColValues("USER_GROUP", "group_id", conditions);
		
		if(groupIds.size() > 0) {
			conditions.clear();
			conditions.put("id", groupIds);
			
			List<Group> externalGroups = globalDAO.getList(Group.class, conditions);
			externalGroups.forEach(externalGroup -> {
												GroupBO internalGroup = new GroupBO();
												internalGroup.setExternalId(externalGroup.getId());
												internalGroup.setName(externalGroup.getName());
												groups.add(internalGroup);
											});
		}

		LOGGER.exitMethod("getMembership");
		return groups;
	}
	
	@Override
	public List<UserBO> searchUsers(List<QueryAttribute> atts) throws FrameworkException {
		LOGGER.enterMethod("searchUsers");
		
		List<UserBO> users = new ArrayList<>();
		
		Map<String, Object> conditions = atts.stream().collect(Collectors.toMap(QueryAttribute::getName, QueryAttribute::getValue));
		List<User> externalUsers = globalDAO.getList(User.class, conditions);
		for(User externalUser : externalUsers) {
			UserBO user = fromDBUser(externalUser);
			users.add(user);
		}
		
		LOGGER.exitMethod("searchUsers");
		return users;
	}
	
	@Override
	public List<GroupBO> searchGroups(List<QueryAttribute> atts) throws FrameworkException {
		LOGGER.enterMethod("searchGroups");
		
		List<GroupBO> groups = new ArrayList<>();
		
		Map<String, Object> conditions = atts.stream().collect(Collectors.toMap(QueryAttribute::getName, QueryAttribute::getValue));
		List<Group> externalGroups = globalDAO.getList(Group.class, conditions);
		for(Group externalGroup : externalGroups) {
			GroupBO group = new GroupBO();
			group.setExternalId(externalGroup.getId());
			group.setName(externalGroup.getName());
			
			groups.add(group);
		}
		
		LOGGER.exitMethod("searchGroups");
		return groups;
	}
	
}
