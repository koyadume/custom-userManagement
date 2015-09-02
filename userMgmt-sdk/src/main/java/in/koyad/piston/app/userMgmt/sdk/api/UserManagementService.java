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
package in.koyad.piston.app.userMgmt.sdk.api;

import java.util.List;

import org.koyad.piston.core.model.Group;
import org.koyad.piston.core.model.User;

import in.koyad.piston.common.bo.Attribute;
import in.koyad.piston.common.exceptions.FrameworkException;

public interface UserManagementService {
	
	public List<User> searchUsers(List<Attribute> atts) throws FrameworkException;
	public User fetchUser(String internalId) throws FrameworkException;
	public void saveUser(User user) throws FrameworkException;
	public void deleteUsers(List<String> userIds) throws FrameworkException;
	
	public List<Group> searchGroups(List<Attribute> atts) throws FrameworkException;
	public Group fetchGroup(String internalId) throws FrameworkException;
	public void saveGroup(Group group) throws FrameworkException;
	public void deleteGroups(List<String> groupIds) throws FrameworkException;

}
