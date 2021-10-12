package com.ooush.api.entity;

import org.joda.time.DateTime;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "logintoken")
public class LoginToken implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TokenID", nullable = false)
	private Integer tokenId;

	@Column(name = "UsersID", nullable = true)
	private Integer usersId;

	@Column(name = "Token", nullable = true)
	private String token;

	@Column(name = "Expiry", nullable = true)
	private DateTime expiry;

	public LoginToken() {
		// Empty constructor for Hibernate
	}

	public Integer getTokenId() {
		return tokenId;
	}

	public void setTokenId(Integer tokenId) {
		this.tokenId = tokenId;
	}

	public Integer getUsersId() {
		return usersId;
	}

	public void setUsersId(Integer usersId) {
		this.usersId = usersId;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public DateTime getExpiry() {
		return expiry;
	}

	public void setExpiry(DateTime expiry) {
		this.expiry = expiry;
	}
}
