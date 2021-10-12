package com.ooush.api.entity;

import com.ooush.api.entity.enumerables.EmailTemplateName;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "emailtemplate")
public class EmailTemplate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "TemplateID")
	private int templateId;

	@Enumerated(EnumType.STRING)
	@Column(name = "TemplateName")
	private EmailTemplateName templateName;

	@Column(name = "Subject")
	private String subject;

	@Column(name = "Content")
	private String content;

	public EmailTemplate() {
		// Empty constructor for hibernate
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public EmailTemplateName getTemplateName() {
		return templateName;
	}

	public void setTemplateName(EmailTemplateName templateName) {
		this.templateName = templateName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
