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

import org.koyad.piston.core.model.Group;

import in.koyad.piston.app.userMgmt.sdk.api.UserManagementService;
import in.koyad.piston.common.bo.Attribute;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.ServiceManager;
import in.koyad.piston.common.utils.StringUtil;

@Path("/groups")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class GroupResource {
	
	private static final LogUtil LOGGER = LogUtil.getLogger(GroupResource.class);
	
	private static final UserManagementService userMgmtService = ServiceManager.getService(UserManagementService.class);
	
	@GET
	public List<Group> searchGroups(@QueryParam("query") String query) throws FrameworkException {
		LOGGER.enterMethod("searchGroups");
		
		String[] tokens = StringUtil.split(query.substring(1, query.length() - 1), "&");
		
		List<Attribute> atts = new ArrayList<>();
		for(String token : tokens) {
			String[] paramValue = StringUtil.split(token, "=");
			atts.add(new Attribute(paramValue[0], paramValue[1]));
		}
		List<Group> groups = userMgmtService.searchGroups(atts);
		
		LOGGER.exitMethod("searchGroups");
		return groups;
	}
	
	@GET
	@Path("{id}")
	public Group getGroup(@PathParam("id") String id) throws FrameworkException {
		LOGGER.enterMethod("getGroup");
		
		Group group = userMgmtService.fetchGroup(id);
		
		LOGGER.exitMethod("getGroup");
		return group;
	}

	@POST
	public void createGroup(Group group) throws FrameworkException {
		LOGGER.enterMethod("createGroup");
		
		userMgmtService.saveGroup(group);
		
		LOGGER.exitMethod("createGroup");
	}
	
	@PUT
	@Path("{id}")
	public void updateGroup(@PathParam("id") String id, Group group) throws FrameworkException {
		LOGGER.enterMethod("updateGroup");
		
		group.setId(id);
		userMgmtService.saveGroup(group);
		
		LOGGER.exitMethod("updateGroup");
	}
	
	@DELETE
	public void deleteGroups(@QueryParam("groupIds") String groupIds) throws FrameworkException {
		LOGGER.enterMethod("deleteGroups");
		
		userMgmtService.deleteGroups(Arrays.asList(StringUtil.split(groupIds, ",")));
		
		LOGGER.exitMethod("deleteGroups");
	}
}
