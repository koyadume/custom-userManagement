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

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.ws.rs.core.MediaType;

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
	
	private static final String ROOT_RESOURCE = MessageFormat.format("http://{0}/userMgmt-service/{1}", 
														SERVICE_HOST.concat(":").concat(String.valueOf(SERVICE_PORT)),
														ServiceConstants.VERSION);
	
	@Override
	public List<User> searchUsers(List<Attribute> atts) throws FrameworkException {
		LOGGER.enterMethod("searchUsers");
		
		List<User> users = null;
		try {
//			String resource = MessageFormat.format("/userMgmt-service/{0}/users", ServiceConstants.VERSION);
			String query = "(".concat(
									Joiner.on('&').join(
														atts.stream().map(attr -> attr.getName().concat("=").concat(attr.getValue()))
														.collect(Collectors.toList())
													)
									).concat(")");
//			
//			users = get(resource, query, List.class);
			User[] users1 = getClient()
								.resource(ROOT_RESOURCE)
								.path("users")
								.queryParam("query", query)
								.get(User[].class);
			
			users = Arrays.asList(users1);
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
//			user = get(MessageFormat.format("/userMgmt-service/{0}/users/{1}", ServiceConstants.VERSION, internalId), User.class);
			
			user = getClient()
						.resource(ROOT_RESOURCE)
						.path("users").path(internalId)
						.get(User.class);
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
//				post(MessageFormat.format("/userMgmt-service/{0}/users", ServiceConstants.VERSION), user);
				
				String userId = getClient()
									.resource(ROOT_RESOURCE)
									.path("users")
									.type(MediaType.APPLICATION_JSON)
									.post(String.class, user);
				user.setId(userId);
			} else {
//				put(MessageFormat.format("/userMgmt-service/{0}/users/{1}", ServiceConstants.VERSION, user.getId()), user);
				
				getClient()
					.resource(ROOT_RESOURCE)
					.path("users").path(user.getId())
					.type(MediaType.APPLICATION_JSON)
					.put(user);
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
//			String query = MessageFormat.format("userIds={0}", Joiner.on(',').join(userIds));
//			delete(MessageFormat.format("/userMgmt-service/{0}/users", ServiceConstants.VERSION), query);
			
			getClient()
				.resource(ROOT_RESOURCE)
				.path("users")
				.queryParam("userIds", Joiner.on(',').join(userIds))
				.delete();
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
//			String resource = MessageFormat.format("/userMgmt-service/{0}/groups", ServiceConstants.VERSION);
//			String query = Joiner.on('&').join(atts.stream().map(attr -> attr.getName().concat("=").concat(attr.getValue())).collect(Collectors.toList()));
//			
//			groups = get(resource, query, List.class);
			
			String query = "(".concat(
									Joiner.on('&').join(
													atts.stream().map(attr -> attr.getName().concat("=").concat(attr.getValue()))
													.collect(Collectors.toList())
												)
								).concat(")");
			
			Group[] groups1 = getClient()
								.resource(ROOT_RESOURCE)
								.path("groups")
								.queryParam("query", query)
								.get(Group[].class);

			groups = Arrays.asList(groups1);
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
//			group = get(MessageFormat.format("/userMgmt-service/{0}/groups/{1}", ServiceConstants.VERSION, internalId), Group.class);
			
			group = getClient()
					.resource(ROOT_RESOURCE)
					.path("groups").path(internalId)
					.get(Group.class);
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
//				post(MessageFormat.format("/userMgmt-service/{0}/groups", ServiceConstants.VERSION), group);
				
				String groupId = getClient()
									.resource(ROOT_RESOURCE)
									.path("groups")
									.type(MediaType.APPLICATION_JSON)
									.post(String.class, group);
				group.setId(groupId);
			} else {
//				put(MessageFormat.format("/userMgmt-service/{0}/groups/{1}", ServiceConstants.VERSION, group.getId()), group);
				
				getClient()
					.resource(ROOT_RESOURCE)
					.path("groups").path(group.getId())
					.type(MediaType.APPLICATION_JSON)
					.put(group);
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
//			String query = MessageFormat.format("groupIds={0}", Joiner.on(',').join(groupIds));
//			delete(MessageFormat.format("/userMgmt-service/{0}/groups", ServiceConstants.VERSION), query);
			
			getClient()
				.resource(ROOT_RESOURCE)
				.path("groups")
				.queryParam("groupIds", Joiner.on(',').join(groupIds))
				.delete();
		} catch(Exception ex) {
			LOGGER.logException(ex);
			throw new FrameworkException(ex.getMessage());
		}
		
		LOGGER.exitMethod("deleteGroups");
	}
	
}
