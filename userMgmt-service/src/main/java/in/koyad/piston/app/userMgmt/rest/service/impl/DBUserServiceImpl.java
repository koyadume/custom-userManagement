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
package in.koyad.piston.app.userMgmt.rest.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.koyad.piston.app.userMgmt.model.Group;
import org.koyad.piston.app.userMgmt.model.User;
import org.koyad.piston.core.model.enums.PrincipalType;

import in.koyad.piston.app.userMgmt.utils.DBConstants;
import in.koyad.piston.common.bo.Attribute;
import in.koyad.piston.common.bo.UserMetadata;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.StringUtil;
import in.koyad.piston.core.dao.GlobalDAO;
import in.koyad.piston.core.service.user.api.ExternalStoreUserService;

public class DBUserServiceImpl implements ExternalStoreUserService {
	
	private static final LogUtil LOGGER = LogUtil.getLogger(DBUserServiceImpl.class);
	
	private final GlobalDAO globalDAO = new GlobalDAO(DBConstants.PERSISTENT_UNIT_AUTHENTICATION);
	
	/**
	 * This will return all (in)direct groups which a user or group belongs to.
	 * 
	 * @param uid - user id
	 * @return - all (in)direct groups which user belongs to
	 * @throws FrameworkException
	 */
	@Override
	public String[] getMembership(String uid) throws FrameworkException {
		LOGGER.enterMethod("getMembership");

		Map<String, Object> conditions = new HashMap();
		conditions.put("user_id", uid);
		
		List<String> groups = globalDAO.getTableSingleColValues("USER_GROUP", "group_id", conditions);

		LOGGER.exitMethod("getMembership");
		return groups.toArray(new String[groups.size()]);
	}
	
	@Override
	public String[] searchPrincipals(List<Attribute> atts, PrincipalType type) throws FrameworkException {
		LOGGER.enterMethod("searchPrincipals");
		
		String[] principalIds = null;
		
		Map<String, Object> conditions = atts.stream().collect(Collectors.toMap(Attribute::getName, Attribute::getValue));
		if(type.equals(PrincipalType.USER)) {
			List<User> users = globalDAO.getList(User.class, conditions);
			principalIds = users.stream().map(user -> user.getId()).collect(Collectors.toList()).toArray(new String[users.size()]);
		} else if(type.equals(PrincipalType.GROUP)) {
			List<Group> groups = globalDAO.getList(Group.class, conditions);
			principalIds = groups.stream().map(group -> group.getId()).collect(Collectors.toList()).toArray(new String[groups.size()]);
		}
		
		LOGGER.exitMethod("searchPrincipals");
		return principalIds;
	}
	
	@Override
	public UserMetadata getUserMetadata(String uid) throws FrameworkException {
		LOGGER.enterMethod("getUserMetadata");
		UserMetadata metadata = null;
		
		List<Attribute> atts = new ArrayList<>();
		atts.add(new Attribute("uid", uid));
		Map<String, Object> conditions = atts.stream().collect(Collectors.toMap(Attribute::getName, Attribute::getValue));
		List<User> users = globalDAO.getList(User.class, conditions);
		if(users.size() > 0) {
			User user = users.get(0);
			metadata = new UserMetadata();
			metadata.setUserDN(user.getId());
			String name = user.getFirstName();
			if(!StringUtil.isEmpty(user.getLastName())) {
				name.concat(" ").concat(user.getLastName());
			}
			metadata.setName(name);
		} else {
			throw new FrameworkException("User not found.");
		}
		
		LOGGER.exitMethod("getUserMetadata");
		return metadata;
	}

}
