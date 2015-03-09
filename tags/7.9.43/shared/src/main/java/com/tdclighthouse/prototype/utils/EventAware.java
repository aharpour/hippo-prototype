package com.tdclighthouse.prototype.utils;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.jcr.observation.Event;
import javax.jcr.observation.EventIterator;
import javax.jcr.observation.EventListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class EventAware<T> implements EventListener {

    private static final Logger LOG = LoggerFactory.getLogger(EventAware.class);
    protected volatile boolean changed;
    private volatile T cached;

    public EventAware(Session session, String absPath, String nodeTypeName) throws RepositoryException {
        this(session, absPath, nodeTypeName, true);
    }

    public EventAware(Session session, String absPath, String nodeTypeName, boolean isDeep) throws RepositoryException {
        this(session, absPath, new String[] { nodeTypeName }, isDeep);
    }

    public EventAware(Session session, String absPath, String[] nodeTypes, boolean isDeep) throws RepositoryException {
        session.getWorkspace().getObservationManager()
                .addEventListener(this, 0x3F, absPath, isDeep, null, nodeTypes, false);
    }

    protected T getCached() throws RepositoryException {
        if (changed || cached == null) {
            synchronized (this) {
                if (changed || cached == null) {
                    LOG.debug("Rebuilding the cached item.");
                    cached = createCached();
                    changed = false;
                }

            }
        }
        return cached;
    }

    protected abstract T createCached() throws RepositoryException;

    @Override
    public void onEvent(EventIterator events) {
        changed = true;
        try {
            if (LOG.isDebugEnabled()) {
                LOG.debug("A number of events have been detected on elasticsearch configuration nodes");
                while (events.hasNext()) {
                    Event event = events.nextEvent();
                    LOG.debug("Event at '{} ({})' of type {} occurred at {} ", event.getPath(), event.getIdentifier(),
                            event.getType(), event.getDate());
                }

            }
        } catch (RepositoryException e) {
            LOG.error(e.getMessage(), e);
        }

    }

}
