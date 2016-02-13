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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.koyad.piston.core.model.Group;
import org.koyad.piston.core.model.Principal;
import org.koyad.piston.core.model.User;
import org.koyad.piston.core.model.enums.PrincipalType;

import in.koyad.piston.app.userMgmt.service.utils.DBConstants;
import in.koyad.piston.common.bo.Attribute;
import in.koyad.piston.common.constants.UserExternalAttributes;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.StringUtil;
import in.koyad.piston.core.dao.GlobalDAO;
import in.koyad.piston.core.sdk.api.UserStoreService;

public class DBUserStoreServiceImpl implements UserStoreService {
	
	private static final LogUtil LOGGER = LogUtil.getLogger(DBUserStoreServiceImpl.class);
	
	private final GlobalDAO globalDAO = new GlobalDAO(DBConstants.PERSISTENT_UNIT_USERMGMT);
	
	/**
	 * This will return all (in)direct groups which a user or group belongs to.
	 * 
	 * @param userExternalId - user id
	 * @return - all (in)direct groups which a user belongs to.
	 * @throws FrameworkException
	 */
	@Override
	public List<Group> getMembership(String userExternalId) throws FrameworkException {
		LOGGER.enterMethod("getMembership");

		List<Group> internalGroups = new ArrayList<>();
		
		Map<String, Object> conditions = new HashMap();
//		conditions.put("uid", userExternalId);
//		
//		String userId = globalDAO.getList(in.koyad.piston.app.userMgmt.sdk.model.User.class, conditions).get(0).getId();
//		conditions.clear();
		conditions.put("user_id", userExternalId);
		
		List<String> groupIds = globalDAO.getTableSingleColValues("USER_GROUP", "group_id", conditions);
		
		if(groupIds.size() > 0) {
			conditions.clear();
			conditions.put("id", groupIds);
			
			List<in.koyad.piston.app.userMgmt.sdk.model.Group> externalGroups = globalDAO.getList(in.koyad.piston.app.userMgmt.sdk.model.Group.class, conditions);
			externalGroups.forEach(externalGroup -> {
												Group internalGroup = new Group();
												internalGroup.setExternalId(externalGroup.getId());
												internalGroup.setName(externalGroup.getName());
												internalGroups.add(internalGroup);
											});
		}

		LOGGER.exitMethod("getMembership");
		return internalGroups;
	}
	
	@Override
	public List<Principal> searchPrincipals(List<Attribute> atts, PrincipalType type) throws FrameworkException {
		LOGGER.enterMethod("searchPrincipals");
		
		List<Principal> principals = new ArrayList<>();
		
		Map<String, Object> conditions = atts.stream().collect(Collectors.toMap(Attribute::getName, Attribute::getValue));
		if(type.equals(PrincipalType.USER)) {
			List<in.koyad.piston.app.userMgmt.sdk.model.User> externalUsers = globalDAO.getList(in.koyad.piston.app.userMgmt.sdk.model.User.class, conditions);
			for(in.koyad.piston.app.userMgmt.sdk.model.User externalUser : externalUsers) {
				User user = fromDBUser(externalUser);
				principals.add(user);
			}
		} else if(type.equals(PrincipalType.GROUP)) {
			List<in.koyad.piston.app.userMgmt.sdk.model.Group> externalGroups = globalDAO.getList(in.koyad.piston.app.userMgmt.sdk.model.Group.class, conditions);
			for(in.koyad.piston.app.userMgmt.sdk.model.Group externalGroup : externalGroups) {
				Group group = new Group();
				group.setExternalId(externalGroup.getId());
				group.setName(externalGroup.getName());
				
				principals.add(group);
			}
		}
		
		LOGGER.exitMethod("searchPrincipals");
		return principals;
	}
	
	@Override
	public User getUser(String uid) throws FrameworkException {
		LOGGER.enterMethod("getUser");

		User user = null;
		Map<String, Object> filters = new HashMap<>();
		filters.put("uid", uid);
		List<in.koyad.piston.app.userMgmt.sdk.model.User> externalUsers = globalDAO.getList(in.koyad.piston.app.userMgmt.sdk.model.User.class, filters);
		if(externalUsers.size() == 1) {
			in.koyad.piston.app.userMgmt.sdk.model.User externalUser = externalUsers.get(0);
			user = fromDBUser(externalUser);
		} else {
			throw new FrameworkException("User not found.");
		}
		
		LOGGER.exitMethod("getUser");
		return user;
	}
	
	private User fromDBUser(in.koyad.piston.app.userMgmt.sdk.model.User externalUser) {
		User user = new User();
		user.setExternalId(externalUser.getId());
		
		Map<String, String> externalAttributes = new HashMap<>();
		user.setExternalAttributes(externalAttributes);
		
		String name = externalUser.getFirstName();
		if(!StringUtil.isEmpty(externalUser.getLastName())) {
			name = name.concat(" ").concat(externalUser.getLastName());
		}
		externalAttributes.put(UserExternalAttributes.NAME, name);
		user.setEmail(externalUser.getEmail());
		
		return user;
	}

}
