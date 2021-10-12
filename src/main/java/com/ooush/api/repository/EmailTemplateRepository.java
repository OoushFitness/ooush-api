package com.ooush.api.repository;


import com.ooush.api.entity.EmailTemplate;
import com.ooush.api.entity.enumerables.EmailTemplateName;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alex Green
 */
@Repository
public interface EmailTemplateRepository extends CrudRepository<EmailTemplate, Integer> {

	EmailTemplate getByTemplateName(EmailTemplateName templateName);

}
