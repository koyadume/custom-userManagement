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
package in.koyad.piston.app.identityMgmt.service.resources;

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

import in.koyad.piston.app.identityMgmt.sdk.model.Group;
import in.koyad.piston.app.identityMgmt.sdk.model.QueryAttribute;
import in.koyad.piston.app.identityMgmt.service.impl.DBIdentityMgmtServiceImpl;
import in.koyad.piston.app.identiyMgmt.sdk.api.IdentityMgmtService;
import in.koyad.piston.common.basic.StringUtil;
import in.koyad.piston.common.basic.exception.FrameworkException;
import in.koyad.piston.common.util.LogUtil;

@Path("/groupMgmt/v1.0/groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupMgmtResource {
	
	private static final LogUtil LOGGER = LogUtil.getLogger(GroupMgmtResource.class);
	
	private static final IdentityMgmtService identityMgmtService = new DBIdentityMgmtServiceImpl();
	
	@GET
	public List<Group> searchGroups(@QueryParam("query") String query) throws FrameworkException {
		LOGGER.enterMethod("searchGroups");
		
		String[] tokens = StringUtil.split(query.substring(1, query.length() - 1), "&");
		
		List<QueryAttribute> atts = new ArrayList<>();
		for(String token : tokens) {
			String[] paramValue = StringUtil.split(token, "=");
			atts.add(QueryAttribute.builder().name(paramValue[0]).value(paramValue[1]).build());
		}
		List<Group> groups = identityMgmtService.searchGroups(atts);
		
		LOGGER.exitMethod("searchGroups");
		return groups;
	}
	
	@GET
	@Path("{id}")
	public Group getGroup(@PathParam("id") String id) throws FrameworkException {
		LOGGER.enterMethod("getGroup");
		
		Group group = identityMgmtService.fetchGroup(id);
		
		LOGGER.exitMethod("getGroup");
		return group;
	}

	@POST
	@Produces(MediaType.TEXT_PLAIN)
	public String createGroup(Group group) throws FrameworkException {
		LOGGER.enterMethod("createGroup");
		
		identityMgmtService.saveGroup(group);
		
		LOGGER.exitMethod("createGroup");
		return group.getId();
	}
	
	@PUT
	@Path("{id}")
	public void updateGroup(@PathParam("id") String id, Group group) throws FrameworkException {
		LOGGER.enterMethod("updateGroup");
		
		group.setId(id);
		identityMgmtService.saveGroup(group);
		
		LOGGER.exitMethod("updateGroup");
	}
	
	@DELETE
	public void deleteGroups(@QueryParam("groupIds") String groupIds) throws FrameworkException {
		LOGGER.enterMethod("deleteGroups");
		
		identityMgmtService.deleteGroups(Arrays.asList(StringUtil.split(groupIds, ",")));
		
		LOGGER.exitMethod("deleteGroups");
	}
}
