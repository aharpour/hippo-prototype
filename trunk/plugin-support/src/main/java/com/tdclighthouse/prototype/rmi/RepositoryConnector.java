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
package com.tdclighthouse.prototype.rmi;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;

import org.apache.commons.pool.ObjectPool;
import org.apache.commons.pool.PoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.apache.commons.pool.impl.GenericObjectPool.Config;
import org.apache.jackrabbit.commons.JcrUtils;

/**
 * @author Ebrahim Aharopur
 *
 */
public class RepositoryConnector {

	private String username;
	private String password;
	private Repository repository;
	private ObjectPool<Session> sessionPool;
	private Config config;

	public RepositoryConnector(String username, String password, String repositoryUrl)
			throws RepositoryException {
		this(username, password, repositoryUrl, null);
	}

	public RepositoryConnector(String username, String password, String repositoryUrl, Config config)
			throws RepositoryException {
		this.username = username;
		this.password = password;
		this.repository = JcrUtils.getRepository(repositoryUrl);
		this.config = config;
		sessionPool = new GenericObjectPool<Session>(new SessionFactory(), getPoolConfig());
	}

	private Config getPoolConfig() {
		if (config == null) {
			config = new Config();
			config.maxActive = 10;
			config.maxIdle = 1;
			config.maxWait = 5000L;
			config.minIdle = 1;
			config.softMinEvictableIdleTimeMillis = 300000L;
			config.testOnBorrow = true;
			config.testOnReturn = true;
			config.whenExhaustedAction = GenericObjectPool.WHEN_EXHAUSTED_BLOCK;
		}
		return config;
	}

	public Session getSession() {
		try {
			return sessionPool.borrowObject();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void returnSession(Session session) {
		try {
			sessionPool.returnObject(session);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) throws RepositoryException, MalformedURLException,
			ClassCastException, RemoteException, NotBoundException {
		RepositoryConnector repositoryConnector = new RepositoryConnector("admin", "admin", "rmi://127.0.0.1:1099/hipporepository");
		Session session = repositoryConnector.getSession();
		System.out.println(session.getRootNode().getProperties());
		repositoryConnector.returnSession(session);
		session = null;
	}

	private class SessionFactory implements PoolableObjectFactory<Session> {

		@Override
		public void activateObject(Session session) throws Exception {

		}

		@Override
		public void destroyObject(Session session) throws Exception {
			session.logout();
		}

		@Override
		public Session makeObject() throws Exception {
			return repository.login(new SimpleCredentials(username, password.toCharArray()));
		}

		@Override
		public void passivateObject(Session session) throws Exception {
			session.refresh(false);
		}

		@Override
		public boolean validateObject(Session session) {
			boolean result = true;
			try {
				if (session.isLive()) {
					session.getRootNode().getProperties();
				} else {
					result = false;
				}
			} catch (Exception e) {
				result = false;
			}
			return result;
		}
	}

}
