package org.LPIntegrator.hibernate;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name="client_data")
@Data
public class ClientEntity {

	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="client_name")
	private String clientName;
	
	@Column(name="is_active")
	private int isActive;
	
	@Column(name="credentials")
	private String credentials;
	
	@Column(name="create_at")
	private Date createdAt;
	
	@Column(name="update_at")
	private Date updatedAt;
	
}
