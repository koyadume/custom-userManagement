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

import java.net.URI;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

import org.koyad.piston.core.model.Group;
import org.koyad.piston.core.model.User;

import com.google.common.base.Joiner;

import in.koyad.piston.app.userMgmt.sdk.api.UserManagementService;
import in.koyad.piston.common.bo.Attribute;
import in.koyad.piston.common.bo.Result;
import in.koyad.piston.common.constants.RestContants;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.AbstractREST;
import in.koyad.piston.common.utils.JsonProcessor;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.RestServiceUtil;

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
		} catch(URISyntaxException ex) {
			LOGGER.logException(ex);
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
		} catch(URISyntaxException ex) {
			LOGGER.logException(ex);
		}
		
		LOGGER.exitMethod("fetchUser");
		return user;
	}
	
	@Override
	public void saveUser(User user) throws FrameworkException {
		LOGGER.enterMethod("saveUser");

		try {
			put(MessageFormat.format("/userMgmt-service/{0}/users", ServiceConstants.VERSION), user);
		} catch(URISyntaxException ex) {
			LOGGER.logException(ex);
		}
		
		LOGGER.exitMethod("saveUser");
	}
	
	@Override
	public List<Group> searchGroups(List<Attribute> atts) throws FrameworkException {
		LOGGER.enterMethod("searchGroups");
		
		List<Group> groups = null;
		try {
			String resource = MessageFormat.format("/userMgmt-service/{0}/groups", ServiceConstants.VERSION);
			String query = Joiner.on('&').join(atts.stream().map(attr -> attr.getName().concat("=").concat(attr.getValue())).collect(Collectors.toList()));
			
			groups = get(resource, query, List.class);
		} catch(URISyntaxException ex) {
			LOGGER.logException(ex);
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
		} catch(URISyntaxException ex) {
			LOGGER.logException(ex);
		}
		
		LOGGER.exitMethod("fetchGroup");
		return group;
	}
	
	@Override
	public void saveGroup(Group group) throws FrameworkException {
		LOGGER.enterMethod("saveGroup");
		
		try {
			put(MessageFormat.format("/userMgmt-service/{0}/groups", ServiceConstants.VERSION), group);
		} catch(URISyntaxException ex) {
			LOGGER.logException(ex);
		}

		LOGGER.exitMethod("saveGroup");
	}
	
}
