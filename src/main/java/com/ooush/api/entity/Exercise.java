package com.ooush.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import java.io.Serializable;

import com.ooush.api.constants.OoushConstants;
import com.ooush.api.dto.request.UpdateUserExerciseRequest;

@Entity
@Table(name = "exercise")
public class Exercise implements Serializable {

	private static final long serialVersionUID = 1L;

	public Exercise() {
		// Empty constructor for hibernate
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", nullable = false)
	private Integer id;

	@Column(name = "Name")
	private String name;

	@Column(name = "Bitmap")
	private Long bitmap;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "UserId")
	private Users user;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getBitmap() {
		return bitmap;
	}

	public void setBitmap(Long bitmap) {
		this.bitmap = bitmap;
	}

	public Users getUser() {
		return user;
	}

	public void setUser(Users user) {
		this.user = user;
	}

	@Transient
	public boolean isCustomExercise() {
		return bitmap >> (OoushConstants.BITMAP_POSITION_CUSTOM_EXERCISE - 1) > 0;
	}

}
