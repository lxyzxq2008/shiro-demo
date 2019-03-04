package com.sunroom.shiro.demo.security;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;

import java.io.Serializable;

public class CacheSessionDao extends EnterpriseCacheSessionDAO {
    @Override
    protected Serializable doCreate(Session session) {
        return super.doCreate(session);
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        return super.doReadSession(sessionId);
    }

    @Override
    protected void doUpdate(Session session) {
        super.doUpdate(session);
    }

    @Override
    protected void doDelete(Session session) {
        super.doDelete(session);
    }
}
