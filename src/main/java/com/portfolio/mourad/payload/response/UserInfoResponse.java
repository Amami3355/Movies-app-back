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

	private String imageData;

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setImageData(String imageData) {
		this.imageData = imageData;
	}

	public String getImageData() {
		return imageData;
	}

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
							String imageData,
							String password) {
		this.id = id;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.roles = roles;
		this.jwtCookie = jwtCookie;
		this.password = password;
		this.imageData = imageData;
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
