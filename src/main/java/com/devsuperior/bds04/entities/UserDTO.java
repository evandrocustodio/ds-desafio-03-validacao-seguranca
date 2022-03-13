package com.devsuperior.bds04.entities;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;

	@Email(message = "Informe um email válido")
	private String email;

	private Set<RoleDTO> roles = new HashSet<>();

	public UserDTO() {
	}	

	public UserDTO(Long id, String email) {
		super();
		this.id = id;
		this.email = email;
	}

	public UserDTO(User entity) {
		id = entity.getId();
		email = entity.getEmail();
		entity.getRoles().forEach(r -> this.getRoles().add(new RoleDTO(r)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Set<RoleDTO> getRoles() {
		return roles;
	}

}
