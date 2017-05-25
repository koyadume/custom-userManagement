package in.koyad.piston.app.identityMgmt.service.identityStore.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import in.koyad.piston.app.identityMgmt.sdk.model.QueryAttribute;
import in.koyad.piston.app.identityMgmt.service.identityStore.api.IdentityStoreService;
import in.koyad.piston.app.identityMgmt.service.identityStore.impl.DBIdentityStoreServiceImpl;
import in.koyad.piston.app.identityMgmt.service.identityStore.model.GroupBO;
import in.koyad.piston.app.identityMgmt.service.identityStore.model.UserBO;
import in.koyad.piston.common.basic.StringUtil;
import in.koyad.piston.common.basic.exception.FrameworkException;
import in.koyad.piston.common.util.LogUtil;

@Produces(MediaType.APPLICATION_JSON)
@Path("identity")
public class IdentityStoreResource {
	
	private static final LogUtil LOGGER = LogUtil.getLogger(IdentityStoreResource.class);
	private static final IdentityStoreService identityStoreService = new DBIdentityStoreServiceImpl();

	@GET
	@Path("users/{loginId}")
	public UserBO getUser(@PathParam("loginId") String loginId) throws FrameworkException {
		LOGGER.enterMethod("getUserAttributes");
		
		UserBO user = identityStoreService.getUser(loginId);
		
		LOGGER.exitMethod("getUserAttributes");
		return user;
	}
	
	@GET
	@Path("users/{id}/membership")
	public Set<GroupBO> getMembership(@PathParam("id") String id) throws FrameworkException {
		LOGGER.enterMethod("getMembership");
		
		Set<GroupBO> groups = identityStoreService.getMembership(id);
		
		LOGGER.exitMethod("getMembership");
		return groups;
	}
	
	@GET
	@Path("users")
	public List<UserBO> searchUsers(@QueryParam("query") String query) throws FrameworkException {
		LOGGER.enterMethod("searchUsers");
		
		String[] tokens = StringUtil.split(query.substring(1, query.length() - 1), "&"); 
		
		List<QueryAttribute> atts = new ArrayList<>();
		for(String token : tokens) {
			String[] paramValue = StringUtil.split(token, "=");
			atts.add(QueryAttribute.builder().name(paramValue[0]).value(paramValue[1]).build());
		}
		List<UserBO> users = identityStoreService.searchUsers(atts);
		
		LOGGER.exitMethod("searchUsers");
		return users;
	}
	
	@GET
	@Path("groups")
	public List<GroupBO> searchGroups(@QueryParam("query") String query) throws FrameworkException {
		LOGGER.enterMethod("searchGroups");
		
		String[] tokens = StringUtil.split(query.substring(1, query.length() - 1), "&");
		
		List<QueryAttribute> atts = new ArrayList<>();
		for(String token : tokens) {
			String[] paramValue = StringUtil.split(token, "=");
			atts.add(QueryAttribute.builder().name(paramValue[0]).value(paramValue[1]).build());
		}
		List<GroupBO> groups = identityStoreService.searchGroups(atts);
		
		LOGGER.exitMethod("searchGroups");
		return groups;
	}
	
}
