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
package in.koyad.piston.app.userMgmt.service.resources;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import in.koyad.piston.app.userMgmt.sdk.api.UserManagementService;
import in.koyad.piston.app.userMgmt.sdk.model.User;
import in.koyad.piston.app.userMgmt.service.impl.DBUserManagementServiceImpl;
import in.koyad.piston.common.bo.Attribute;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.StringUtil;

/**
 * 
 *
 * @author Shailendra Singh
 * @since 1.0
 */
@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResource {
	
	private static final LogUtil LOGGER = LogUtil.getLogger(UserResource.class);

	private static final UserManagementService userMgmtService = new DBUserManagementServiceImpl();
	
	@GET
	public List<User> searchUsers(@QueryParam("query") String query) throws FrameworkException {
		LOGGER.enterMethod("searchUsers");
		
		String[] tokens = StringUtil.split(query.substring(1, query.length() - 1), "&");
		
		List<Attribute> atts = new ArrayList<>();
		for(String token : tokens) {
			String[] paramValue = StringUtil.split(token, "=");
			atts.add(new Attribute(paramValue[0], paramValue[1]));
		}
		List<User> users = userMgmtService.searchUsers(atts);
		
		LOGGER.exitMethod("searchUsers");
		return users;
	}
	
	@GET
	@Path("{id}")
	public User getUser(@PathParam("id") String id) throws FrameworkException {
		LOGGER.enterMethod("getUser");
		
		User user = userMgmtService.fetchUser(id);
		
		LOGGER.exitMethod("getUser");
		return user;
	}
	
	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String createUser(User user) throws FrameworkException {
		LOGGER.enterMethod("createUser");
		
		userMgmtService.saveUser(user);
		
		LOGGER.exitMethod("createUser");
		return user.getId();
	}
	
	@PUT
	@Path("{id}")
	public void updateUser(@PathParam("id") String id, User user) throws FrameworkException {
		LOGGER.enterMethod("updateUser");
		
		user.setId(id);
		userMgmtService.saveUser(user);
		
		LOGGER.exitMethod("updateUser");
	}
	
	@DELETE
	public void deleteUsers(@QueryParam("userIds") String userIds) throws FrameworkException {
		LOGGER.enterMethod("deleteUsers");
		
		userMgmtService.deleteUsers(Arrays.asList(StringUtil.split(userIds, ",")));
		
		LOGGER.exitMethod("deleteUsers");
	}
}
