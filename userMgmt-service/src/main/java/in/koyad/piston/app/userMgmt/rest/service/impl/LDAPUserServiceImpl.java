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
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.koyad.piston.core.model.enums.PrincipalType;

import com.google.common.base.Joiner;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPException;
import com.unboundid.ldap.sdk.SearchResult;
import com.unboundid.ldap.sdk.SearchResultEntry;
import com.unboundid.ldap.sdk.SearchScope;

import in.koyad.piston.common.bo.Attribute;
import in.koyad.piston.common.bo.UserMetadata;
import in.koyad.piston.common.exceptions.ErrorCodes;
import in.koyad.piston.common.exceptions.FrameworkException;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.core.service.user.api.ExternalStoreUserService;

public class LDAPUserServiceImpl implements ExternalStoreUserService {
	
	private static final LogUtil LOGGER = LogUtil.getLogger(LDAPUserServiceImpl.class);
	
	private final String HOST_NAME = "localhost";
	private final int PORT = 389;
	private final String BIND_DN = "uid=admin,ou=system";
	private final String BIND_PWD = "secret";
	private final String USER_SEARCH_BASE = "ou=users,ou=piston,o=koyad.in";
	private final String GROUP_SEARCH_BASE = "ou=groups,ou=piston,o=koyad.in";
	private final String USER_OBJECT_CLASS = "inetOrgPerson";
	private final String GROUP_OBJECT_CLASS = "groupOfUniqueNames";
	
	private String getQuery(List<Attribute> atts) {
		List<String> args = atts.stream().map(x -> "(" + x.getName() + "=" + x.getValue() + ")").collect(Collectors.toList());
		return Joiner.on("").join(args);
	}
	
	private LDAPConnection getConnection() throws FrameworkException {
		LDAPConnection ldapConnection = null;
		try {
			ldapConnection = new LDAPConnection(HOST_NAME, PORT, BIND_DN, BIND_PWD);
		} catch (LDAPException ex) {
			LOGGER.logException(ex);
			throw new FrameworkException(ErrorCodes.LDAP_CONNECTION_ERROR);
		}
		return ldapConnection;
	}
	
	/**
	 * This will return all (in)direct groups which a user or group belongs to.
	 * 
	 * @param uid - user id
	 * @return - all (in)direct groups which user belongs to
	 * @throws FrameworkException
	 */
	@Override
	public String[] getMembership(String userDN) throws FrameworkException {
		LOGGER.enterMethod("getMembership");
		
		//get all direct groups
		List<Attribute> atts = new ArrayList<>();
		atts.add(new Attribute("uniquemember", userDN));
		Set<String> groupIds = new HashSet<String>(Arrays.asList(searchPrincipals(atts, PrincipalType.GROUP)));
		
		Set<String> newGroupIds = groupIds;
		
		do {
			Set<String>  newGroupIds1 = new HashSet<String>();
			
			for(String groupId : newGroupIds) {
				atts = new ArrayList<>();
				atts.add(new Attribute("uniquemember", groupId));
				Set<String> indirectGrpIds = new HashSet<String>(Arrays.asList(searchPrincipals(atts, PrincipalType.GROUP)));
				
				//remove already found groups
				indirectGrpIds.removeAll(groupIds);
				
				//add newly found groups
				newGroupIds1.addAll(indirectGrpIds);
			}
			
			//add newly found groups to master list
			groupIds.addAll(newGroupIds1);
			
			//reset intermediate list
			newGroupIds = newGroupIds1;
		} while(newGroupIds.size() > 0);	
		
		LOGGER.exitMethod("getMembership");
		return groupIds.toArray(new String[groupIds.size()]);
	}

	@Override
	public String[] searchPrincipals(List<Attribute> atts, PrincipalType type) throws FrameworkException {
		LOGGER.enterMethod("searchPrincipals");
		
		LDAPConnection conn = getConnection();
		
		List<String> principalIds = new ArrayList<>();
		try {
			String attrsQuery = getQuery(atts);
			String searchQuery = getSearchQueryAttrs(attrsQuery, type);
			
			String searchBase = null;
			if(type.equals(PrincipalType.USER)) {
				searchBase = USER_SEARCH_BASE;
			} else if(type.equals(PrincipalType.GROUP)) {
				searchBase = GROUP_SEARCH_BASE;
			}
			
			LOGGER.info("searchBase : " + searchBase);
			
			SearchResult result = conn.search(searchBase, SearchScope.ONE, searchQuery);
			
			for(SearchResultEntry entry : result.getSearchEntries()) {
				principalIds.add(entry.getDN());
			}
		} catch (LDAPException ex) {
			LOGGER.logException(ex);
			throw new FrameworkException(ErrorCodes.LDAP_SEARCH_ERROR);
		} finally {
			conn.close();
		}
		
		LOGGER.info("Search Results : " + principalIds);
		
		LOGGER.exitMethod("searchPrincipals");
		return principalIds.toArray(new String[principalIds.size()]);
	}
	
	private String getSearchQueryAttrs(String attrsQuery, PrincipalType type) {
		StringBuilder builder = new StringBuilder("(&(objectClass=");
		
		if(type.equals(PrincipalType.USER)) {
			builder.append(USER_OBJECT_CLASS);
		} else if(type.equals(PrincipalType.GROUP)) {
			builder.append(GROUP_OBJECT_CLASS);
		}
		
		builder.append(")");
		builder.append(attrsQuery);
		builder.append(")");
		
		String query = builder.toString();
		LOGGER.info("Search query : " + query);
		return query;
	}

	/**
	 *  This retrievs user information from ldap.
	 *  
	 *  This is NOT User DN.
	 */ 
	@Override
	public UserMetadata getUserMetadata(String uid) throws FrameworkException {
		LOGGER.enterMethod("getUserMetadata");
		
		LDAPConnection conn = getConnection();
		UserMetadata metadata = null;
		try {
			List<Attribute> atts = new ArrayList<>();
			atts.add(new Attribute("uid", uid));
			String attrsQuery = getQuery(atts);
			String searchQuery = getSearchQueryAttrs(attrsQuery, PrincipalType.USER);
			
			String searchBase = USER_SEARCH_BASE;
			LOGGER.info("searchBase : " + searchBase);
			SearchResult result = conn.search(searchBase, SearchScope.ONE, searchQuery);
			
			SearchResultEntry entry = result.getSearchEntries().get(0);
			metadata = new UserMetadata();
			metadata.setUserDN(entry.getDN());
			metadata.setName(entry.getAttribute("givenName").getValue() + " " + entry.getAttribute("sn").getValue());
		} catch (LDAPException ex) {
			LOGGER.logException(ex);
			throw new FrameworkException(ErrorCodes.LDAP_SEARCH_ERROR);
		} finally {
			conn.close();
		}
		
		LOGGER.exitMethod("getUserMetadata");
		return metadata;
	}

}
