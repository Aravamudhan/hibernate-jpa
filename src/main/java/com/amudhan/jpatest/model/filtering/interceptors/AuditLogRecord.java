package com.amudhan.jpatest.model.filtering.interceptors;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity(name = "FILTERING_INTERCEPTORS_AUDITLOGRECORD")
@SuppressWarnings({ "rawtypes", "serial" })
public class AuditLogRecord implements Serializable {

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
	
	@NotNull
    private String message;

    @NotNull
    private Long entityId;

	@NotNull
    private Class entityClass;

    @NotNull
    private Long userId;
    
    @NotNull
    private LocalDateTime createdOn = LocalDateTime.now();
    
    public AuditLogRecord(){}
    
    public AuditLogRecord(String message, 
    						Auditable entityInstance,
    						Long userId){
    	this.message = message;
    	this.entityId = entityInstance.getId();
    	this.entityClass = entityInstance.getClass();
    	this.userId = userId;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Long getEntityId() {
		return entityId;
	}

	public void setEntityId(Long entityId) {
		this.entityId = entityId;
	}

	public Class getEntityClass() {
		return entityClass;
	}

	public void setEntityClass(Class entityClass) {
		this.entityClass = entityClass;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public LocalDateTime getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(LocalDateTime createdOn) {
		this.createdOn = createdOn;
	}

	@Override
	public String toString() {
		return "AuditLogRecord [id=" + id + ", message=" + message
				+ ", entityId=" + entityId + ", entityClass=" + entityClass
				+ ", userId=" + userId + ", createdOn=" + createdOn + "]";
	}
    
}
