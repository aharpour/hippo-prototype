/*
 *  Copyright 2012 Finalist B.V.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.tdclighthouse.prototype.support;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import com.tdclighthouse.prototype.rmi.RepositoryConnector;

/**
 * @author Ebrahim Aharpour
 * 
 */
public abstract class AbstractSessionTemplate {

	protected Session session;
	private RepositoryConnector repositoryConnector;

	public AbstractSessionTemplate(Session session) {
		this.session = session;
	}

	public AbstractSessionTemplate(Repository repository) throws RepositoryException {
		this.session = repository.login();
	}

	public AbstractSessionTemplate(RepositoryConnector repositoryConnector) {
		this.repositoryConnector = repositoryConnector;
		this.session = repositoryConnector.getSession();
	}

	public abstract <T> T execute(SessionCallBack<T> stub) throws RepositoryException;

	public interface SessionCallBack<T> {
		public T doInSession(Session session) throws RepositoryException;
	}

	public void logout() {
		if (repositoryConnector != null) {
			repositoryConnector.returnSession(session);
			session = null;
		} else {
			session.logout();
		}

	}

	@Override
	protected void finalize() throws Throwable {
		if (session != null && (repositoryConnector != null || session.isLive())) {
			logout();
		}
		super.finalize();
	}
}
