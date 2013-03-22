package com.tdclighthouse.prototype.support;

import javax.jcr.Repository;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import com.tdclighthouse.prototype.rmi.RepositoryConnector;

public class TransactionalSessionTemplate extends AbstractSessionTemplate {

	public TransactionalSessionTemplate(Repository repository) throws RepositoryException {
		super(repository);
	}

	public TransactionalSessionTemplate(RepositoryConnector repositoryConnector) {
		super(repositoryConnector);
	}

	public TransactionalSessionTemplate(Session session) {
		super(session);
	}

	@Override
	public <T> T execute(SessionCallBack<T> stub) throws RepositoryException {
		T result;
		synchronized (session) {
			if (!session.isLive()) {
				session.refresh(false);
			}
			result = stub.doInSession(session);
		}
		return result;
	}

	public void save() throws RepositoryException {
		synchronized (session) {
			session.save();
		}
	}

	public void refresh(boolean keepChanges) throws RepositoryException {
		synchronized (session) {
			session.refresh(keepChanges);
		}
	}

}
