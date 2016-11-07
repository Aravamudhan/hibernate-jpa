package com.amudhan.jpatest.model.filtering;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.hibernate.CallbackException;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Session;
import org.hibernate.type.Type;

import com.amudhan.jpatest.model.filtering.interceptors.AuditLogRecord;
import com.amudhan.jpatest.model.filtering.interceptors.Auditable;

@SuppressWarnings("serial")
public class AuditLogInterceptor extends EmptyInterceptor {

	private Session currentSession;
	private Long currentUserId;
	private Set<Auditable> inserts = new HashSet<Auditable>();
	private Set<Auditable> updates = new HashSet<Auditable>();

	public void setCurrentSession(Session session) {
		this.currentSession = session;
	}

	public void setCurrentUserId(Long currentUserId) {
		this.currentUserId = currentUserId;
	}

	@Override
	public boolean onSave(Object entity, Serializable id, Object[] state,
			String[] propertyNames, Type[] types) throws CallbackException {

		if (entity instanceof Auditable)
			inserts.add((Auditable) entity);

		return false;
	}

	@Override
	public boolean onFlushDirty(Object entity, Serializable id,
			Object[] currentState, Object[] previousState,
			String[] propertyNames, Type[] types) throws CallbackException {

		if (entity instanceof Auditable)
			updates.add((Auditable) entity);

		return false;
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void postFlush(Iterator iterator) throws CallbackException {

        Session tempSession =
            currentSession.sessionWithOptions()
                .connection()
                .openSession();

        try {
            for (Auditable entity : inserts) {
                tempSession.persist(
                    new AuditLogRecord("insert", entity, currentUserId)
                );
            }
            for (Auditable entity : updates) {
                tempSession.persist(
                    new AuditLogRecord("update", entity, currentUserId)
                );
            }

            tempSession.flush();
        } finally {
            tempSession.close();
            inserts.clear();
            updates.clear();
        }
    }

}
