package com.ooush.api.service.email;

import com.ooush.api.entity.EmailTemplate;
import com.ooush.api.entity.Users;
import com.ooush.api.entity.enumerables.EmailTemplateName;
import com.ooush.api.repository.EmailTemplateRepository;
import com.ooush.api.service.appsettings.AppSettingsServiceImpl;
import com.ooush.api.utils.EmailUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Transactional
@Service("RegisterUserEmailService")
public class RegisterUserEmailService implements EmailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RegisterUserEmailService.class);

	@Autowired
	private EmailTemplateRepository emailTemplateRepository;

	@Autowired
	private AppSettingsServiceImpl appSettingsService;

	@Autowired
	private JavaMailSenderImpl mimeMailSender;

	public void sendRegistrationEmail(Users user) {
		MimeMessage mimeMessage = mimeMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

		EmailTemplate emailTemplate = emailTemplateRepository.getByTemplateName(EmailTemplateName.GENERIC_HTML_TEMPLATE);
		String verificationUrl = appSettingsService.constructAppBaseUrl() + "/users/verifyUser/" + user.getVerificationCode();

		String subject = "Welcome to Ooush Fitness!";
		String htmlMsg = "<b>Dear " + user.getFirstName() + " " + user.getLastName() + "</b><br/>"
				+ "<br/>Thank you for registering an account with Ooush Fitness.<br/>"
				+ "<br/>To complete your registration please verify your email address!<br/>"
				+ "<br/>Click the link below to verify:"
				+ "<br/>"
				+ EmailUtils.withCustomActionButton(verificationUrl, "Verify Your Account");

		setEmailContent(helper, emailTemplate, user, subject, htmlMsg);

		try {
			this.mimeMailSender.send(mimeMessage);
			LOGGER.info("Service sendRegistrationEmail() from RegisterUserEmailService called");
			LOGGER.debug("Registration email sent to new user {} with subject {}", user.getEmail(), subject);
		} catch (MailException ex) {
			System.err.println(ex.getMessage());
		}
	}

	public void setEmailContent(MimeMessageHelper helper, EmailTemplate emailTemplate, Users user, String subject, String htmlMsg) {
		try {
			helper.setTo(user.getEmail());
			helper.setSubject(subject);
			helper.setFrom("noreply@ooushfitness.com");
			helper.setCc(new String[]{});

			String injectedTemplate = EmailUtils.injectContent(emailTemplate.getContent(), "content", htmlMsg);

			helper.setText(injectedTemplate, true);
		}
		catch(MessagingException ex) {
			System.err.println(ex.getMessage());
		}
	}

}
