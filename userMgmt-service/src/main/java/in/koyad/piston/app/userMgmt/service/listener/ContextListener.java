/*
 * Copyright (C) 2012-2015 Shailendra Singh <shailendra_01@outlook.com>
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
package in.koyad.piston.app.userMgmt.service.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.google.common.base.Joiner;

import in.koyad.piston.app.userMgmt.utils.DBConstants;
import in.koyad.piston.common.utils.LogUtil;
import in.koyad.piston.core.dao.utils.JPAEMFactory;

/**
 * 
 * 
 * @author Shailendra
 */

@WebListener
public class ContextListener implements ServletContextListener {

	private static final LogUtil LOGGER = LogUtil.getLogger(ContextListener.class);

	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		LOGGER.enterMethod("contextInitialized");
			
		//initialize JPA EntityManagerFactory
		JPAEMFactory.initialize(Joiner.on(',').join(new String[]{
														DBConstants.PERSISTENT_UNIT_AUTHENTICATION
													}));
		
		LOGGER.exitMethod("contextInitialized");
	}

	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		LOGGER.enterMethod("contextDestroyed");
		
		JPAEMFactory.close();

		LOGGER.exitMethod("contextDestroyed");
	}

}
