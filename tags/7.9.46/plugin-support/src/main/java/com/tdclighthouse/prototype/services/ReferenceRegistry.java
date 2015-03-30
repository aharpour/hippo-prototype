package com.tdclighthouse.prototype.services;

public interface ReferenceRegistry {

    /**
     * @param reference
     *            a reference to another resouce being imported. it can be a
     *            path or an Id in the old system.
     * @param uuid
     *            uuid of handle of the resource after being imported.
     */
    public void register(String reference, String uuid);

    /**
     * This method gets the old reference of a resource and return its handle
     * uuid in Hippo repository.
     *
     * @param reference
     *            a reference to another resouce being imported. it can be a
     *            path or an Id in the old system.
     * @return
     */
    public String lookup(String reference);

}
