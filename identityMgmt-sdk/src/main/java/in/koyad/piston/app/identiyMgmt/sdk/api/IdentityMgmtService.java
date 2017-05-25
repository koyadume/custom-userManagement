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
package in.koyad.piston.app.identiyMgmt.sdk.api;

import java.util.List;

import in.koyad.piston.app.identityMgmt.sdk.model.Group;
import in.koyad.piston.app.identityMgmt.sdk.model.QueryAttribute;
import in.koyad.piston.app.identityMgmt.sdk.model.User;
import in.koyad.piston.common.basic.exception.FrameworkException;

public interface IdentityMgmtService {
	
	public List<User> searchUsers(List<QueryAttribute> atts) throws FrameworkException;
	public User getUser(String id) throws FrameworkException;
	public void saveUser(User user) throws FrameworkException;
	public void deleteUsers(List<String> userIds) throws FrameworkException;
	
	public List<Group> searchGroups(List<QueryAttribute> atts) throws FrameworkException;
	public Group fetchGroup(String internalId) throws FrameworkException;
	public void saveGroup(Group group) throws FrameworkException;
	public void deleteGroups(List<String> groupIds) throws FrameworkException;
	
}
