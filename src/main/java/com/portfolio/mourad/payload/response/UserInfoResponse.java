package com.portfolio.mourad.payload.response;

import java.util.List;

public class UserInfoResponse {
	private Integer id;
	private String username;
	private String firstName;
	private String lastName;
	private String email;
	private List<String> roles;

	private String jwtCookie;

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getPassword() {
		return password;
	}

	private String password;

	public UserInfoResponse(Integer id,
							String username,
							String email,
							String firstName,
							String lastName,
							List<String> roles,
							String jwtCookie,
							String password) {
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.roles = roles;
		this.jwtCookie = jwtCookie;
		this.password = password;
	}

	public String getJwtCookie() {
		return this.jwtCookie;
	}

	public void setJwtCookie(String id) {
		this.jwtCookie = jwtCookie;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<String> getRoles() {
		return roles;
	}
}
