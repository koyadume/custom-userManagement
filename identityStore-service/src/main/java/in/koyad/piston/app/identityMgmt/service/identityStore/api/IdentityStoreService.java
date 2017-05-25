package in.koyad.piston.app.identityMgmt.service.identityStore.api;

import java.util.List;
import java.util.Set;

import in.koyad.piston.app.identityMgmt.sdk.model.QueryAttribute;
import in.koyad.piston.app.identityMgmt.service.identityStore.model.GroupBO;
import in.koyad.piston.app.identityMgmt.service.identityStore.model.UserBO;
import in.koyad.piston.common.basic.exception.FrameworkException;

public interface IdentityStoreService {
	
	/**
	 *  Here loginId is what user will use to logon to piston. It could be username,
	 *  email id etc.
	 */  
	public UserBO getUser(String loginId) throws FrameworkException;
	
	/**
	 * 
	 * @param userExternalId - This will be User DN in case of LDAP and ID in case of DB.
	 * @return Array of (in)direct groups' external identifiers (Group DN for LDAP, ID for DB) which a user belongs to.
	 */
	public Set<GroupBO> getMembership(String externalId) throws FrameworkException;
	
	/**
	 * 
	 * @param attrs
	 * @param type
	 * 
	 * @return Array of principals' external identifiers (User/Group DN for LDAP, ID for DB) for given type of principal (USER, GROUP).
	 * @throws FrameworkException
	 */
	public List<UserBO> searchUsers(List<QueryAttribute> attrs) throws FrameworkException;
	
	/**
	 * 
	 * @param attrs
	 * @param type
	 * 
	 * @return Array of principals' external identifiers (User/Group DN for LDAP, ID for DB) for given type of principal (USER, GROUP).
	 * @throws FrameworkException
	 */
	public List<GroupBO> searchGroups(List<QueryAttribute> attrs) throws FrameworkException;
}
