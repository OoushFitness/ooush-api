package com.ooush.api.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ooush.api.entity.enumerables.UserStatus;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import org.joda.time.DateTime;
import com.fasterxml.jackson.datatype.joda.JodaModule;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
public class Users implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "UsersID", nullable = false)
	private Integer usersId;

	@OneToMany(mappedBy = "users")
	@JsonIgnore
	private List<LoginToken> loginTokens;

	@NotEmpty
	@Length(max = 128)
	@Size(max = 128)
	@Column(name = "Email", nullable = true)
	private String email;

	@NotNull
	@Column(name = "Active", nullable = false)
	@JsonIgnore
	private boolean active;

	@NotNull
	@Column(name = "EmailConfirmed", nullable = false)
	@JsonIgnore
	private boolean emailConfirmed;

	@Column(name = "PasswordHash", nullable = true)
	@JsonIgnore
	private String passwordHash;

	@Column(name = "PhoneNumber", nullable = true)
	private String phoneNumber;

	@NotNull
	@Column(name = "PhoneNumberConfirmed", nullable = false)
	@JsonIgnore
	private Boolean phoneNumberConfirmed;

	@NotEmpty
	@Length(max = 128)
	@Size(max = 128)
	@Column(name = "UserName", nullable = true)
	private String userName;

	@NotEmpty
	@Length(max = 128)
	@Size(max = 128)
	@Column(name = "FirstName", nullable = true)
	private String firstName;

	@NotEmpty
	@Length(max = 128)
	@Size(max = 128)
	@Column(name = "LastName", nullable = true)
	private String lastName;

	@Enumerated(EnumType.STRING)
	@Column(name = "UserStatus", nullable = true)
	private UserStatus userStatus;

	@NotEmpty
	@Length(max = 128)
	@Size(max = 128)
	@Column(name = "VerificationCode", nullable = true)
	private String verificationCode;

	@Column(name = "CodeGenerationTime")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime codeGenerationTime;

	@Column(name = "PasswordResetVerificationCode")
	private String passwordResetVerificationCode;

	@Column(name = "PasswordResetCodeGenerationTime")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime passwordResetCodeGenerationTime;

	@Column(name = "Location")
	private String location;

	@Column(name = "IsTestUser")
	private Boolean isTestUser = false;

	@Column(name = "IdentityVerificationTime")
	@Type(type = "org.jadira.usertype.dateandtime.joda.PersistentDateTime")
	private DateTime identityVerificationTime;

	@NotEmpty
	@Length(max = 128)
	@Size(max = 128)
	@Column(name = "IdentityVerificationCode", nullable = true)
	private String identityVerificationCode;

	@Column(name = "IdentityConfirmed")
	private Boolean identityConfirmed;

	@Column(name = "LoginAttempts", nullable = false, columnDefinition = "int default 0")
	private int loginAttempts;

	public Users() {
		// Empty constructor for Hibernate
	}

	public Integer getUsersId() {
		return usersId;
	}

	public void setUsersId(Integer usersId) {
		this.usersId = usersId;
	}

	public List<LoginToken> getLoginTokens() {
		return loginTokens;
	}

	public void setLoginTokens(List<LoginToken> loginToken) {
		this.loginTokens = loginToken;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isEmailConfirmed() {
		return emailConfirmed;
	}

	public void setEmailConfirmed(boolean emailConfirmed) {
		this.emailConfirmed = emailConfirmed;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public Boolean getPhoneNumberConfirmed() {
		return phoneNumberConfirmed;
	}

	public void setPhoneNumberConfirmed(Boolean phoneNumberConfirmed) {
		this.phoneNumberConfirmed = phoneNumberConfirmed;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public UserStatus getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(UserStatus userStatus) {
		this.userStatus = userStatus;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	public DateTime getCodeGenerationTime() {
		return codeGenerationTime;
	}

	public void setCodeGenerationTime(DateTime codeGenerationTime) {
		this.codeGenerationTime = codeGenerationTime;
	}

	public String getPasswordResetVerificationCode() {
		return passwordResetVerificationCode;
	}

	public void setPasswordResetVerificationCode(String passwordResetVerificationCode) {
		this.passwordResetVerificationCode = passwordResetVerificationCode;
	}

	public DateTime getPasswordResetCodeGenerationTime() {
		return passwordResetCodeGenerationTime;
	}

	public void setPasswordResetCodeGenerationTime(DateTime passwordResetCodeGenerationTime) {
		this.passwordResetCodeGenerationTime = passwordResetCodeGenerationTime;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Boolean getTestUser() {
		return isTestUser;
	}

	public void setTestUser(Boolean testUser) {
		isTestUser = testUser;
	}

	public DateTime getIdentityVerificationTime() {
		return identityVerificationTime;
	}

	public void setIdentityVerificationTime(DateTime identityVerificationTime) {
		this.identityVerificationTime = identityVerificationTime;
	}

	public String getIdentityVerificationCode() {
		return identityVerificationCode;
	}

	public void setIdentityVerificationCode(String identityVerificationCode) {
		this.identityVerificationCode = identityVerificationCode;
	}

	public Boolean getIdentityConfirmed() {
		return identityConfirmed;
	}

	public void setIdentityConfirmed(Boolean identityConfirmed) {
		this.identityConfirmed = identityConfirmed;
	}

	public int getLoginAttempts() {
		return loginAttempts;
	}

	public void setLoginAttempts(int loginAttempts) {
		this.loginAttempts = loginAttempts;
	}

}
