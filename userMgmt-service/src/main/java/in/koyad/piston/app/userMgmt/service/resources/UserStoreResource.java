package in.koyad.piston.app.userMgmt.service.resources;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.koyad.piston.core.model.Group;
import org.koyad.piston.core.model.Principal;
import org.koyad.piston.core.model.User;
import org.koyad.piston.core.model.enums.PrincipalType;

import in.koyad.piston.app.userMgmt.service.impl.DBUserStoreServiceImpl;
import in.koyad.piston.common.bo.Attribute;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.common.utils.StringUtil;
import in.koyad.piston.core.sdk.api.UserStoreService;

@Path("userStore")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserStoreResource {
	
	private static final LogUtil LOGGER = LogUtil.getLogger(UserStoreResource.class);

	private static final UserStoreService userStoreService = new DBUserStoreServiceImpl();
	
	@GET
	@Path("users/{uid}/getMembership")
	public List<Group> getMembership(@PathParam("uid") String uid) throws FrameworkException {
		LOGGER.enterMethod("getMembership");
		
		List<Group> groups = userStoreService.getMembership(uid);
		
		LOGGER.exitMethod("getMembership");
		return groups;
	}
	
	@GET
	@Path("users")
	public List searchUsers(@QueryParam("query") String query) throws FrameworkException {
		LOGGER.enterMethod("searchUsers");
		
		String[] tokens = StringUtil.split(query.substring(1, query.length() - 1), "&"); 
		
		List<Attribute> atts = new ArrayList<>();
		for(String token : tokens) {
			String[] paramValue = StringUtil.split(token, "=");
			atts.add(new Attribute(paramValue[0], paramValue[1]));
		}
		List<Principal> users = userStoreService.searchPrincipals(atts, PrincipalType.USER);
		
		LOGGER.exitMethod("searchUsers");
		return users;
	}
	
	@GET
	@Path("groups")
	public List<Principal> searchGroups(@QueryParam("query") String query) throws FrameworkException {
		LOGGER.enterMethod("searchGroups");
		
		String[] tokens = StringUtil.split(query.substring(1, query.length() - 1), "&");
		
		List<Attribute> atts = new ArrayList<>();
		for(String token : tokens) {
			String[] paramValue = StringUtil.split(token, "=");
			atts.add(new Attribute(paramValue[0], paramValue[1]));
		}
		List<Principal> groups = userStoreService.searchPrincipals(atts, PrincipalType.GROUP);
		
		LOGGER.exitMethod("searchGroups");
		return groups;
	}
	
	@GET
	@Path("users/{uid}")
	public User getUser(@PathParam("uid") String uid) throws FrameworkException {
		LOGGER.enterMethod("getUserAttributes");
		
		User user = userStoreService.getUser(uid);
		
		LOGGER.exitMethod("getUserAttributes");
		return user;
	}
	
}
