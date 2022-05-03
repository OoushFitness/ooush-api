package com.ooush.api.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "exercise")
public class Exercise implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "Id", nullable = false)
	private Integer id;

	@Column(name = "Name", nullable = true)
	private String name;

	@Column(name = "Bitmap")
	private Long bitmap;

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
}
