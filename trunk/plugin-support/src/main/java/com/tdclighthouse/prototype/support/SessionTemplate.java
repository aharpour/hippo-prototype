package com.tdclighthouse.prototype.support;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

public class SessionTemplate {

	private final Session session;

	public SessionTemplate(Session session) {
		this.session = session;
	}

	public SessionTemplate(Repository repository) throws RepositoryException {
		this.session = repository.login();
	}

	public <T> T execute(SessionCallBack<T> stub) throws RepositoryException {
		T result;
		synchronized (session) {
			if (!session.isLive()) {
				session.refresh(false);
			}
			result = stub.doInSession(session);
			session.save();
		}
		return result;
	}

	public interface SessionCallBack<T> {
		public T doInSession(Session session) throws RepositoryException;
	}

	public void logout() {
		session.logout();
	}

}
