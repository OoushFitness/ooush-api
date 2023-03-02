package com.ooush.api.service.email;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import com.ooush.api.entity.EmailTemplate;
import com.ooush.api.entity.Users;
import com.ooush.api.entity.enumerables.EmailTemplateName;
import com.ooush.api.repository.EmailTemplateRepository;
import com.ooush.api.service.appsettings.AppSettingsService;
import com.ooush.api.service.appsettings.AppSettingsServiceImpl;

class RegisterUserEmailServiceTest {

    private final static String NO_REPLY_EMAIL = "noreply@ooushfitness.com";
    private final static String EMAIL = "test_user_1@ooushfitness.com";
    private final static String FIRST_NAME = "Testy";
    private final static String LAST_NAME = "McTest";
    private final static String VERIFICATION_CODE = "660163a5-e141-4e86-9410-e27b2d0b41fe";
    private final static String HTML_EMAIL_TEMPLATE = "<!DOCTYPE html> <html lang=\"en\" xmlns=\"http://www.w3.org/1999/xhtml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\"> <head> <meta charset=\"UTF-8\"> <meta name=\"viewport\" content=\"width=device-width,initial-scale=1\"> <meta name=\"x-apple-disable-message-reformatting\"> <title></title>   <!--[if mso]>   <noscript>     <xml>       <o:OfficeDocumentSettings>       <o:PixelsPerInch>96</o:PixelsPerInch>       </o:OfficeDocumentSettings>     </xml>   </noscript>   <![endif]-->   <style>     table, td, div, h1, p {font-family: Trebuchet MS, sans-serif;}   </style> </head> <body style=\"margin:0;padding:0;\">   <table role=\"presentation\" style=\"width:100%;border-collapse:collapse;border:0;border-spacing:0;background:#ffffff;\">     <tr>       <td align=\"center\" style=\"padding:0;\">         <table role=\"presentation\" style=\"width:602px;border-collapse:collapse;border:1px solid #cccccc;border-spacing:0;text-align:left;\">           <tr>             <td align=\"center\" style=\"padding:100px 0 100px 0;background:#0984AB;color:#FFFFFF;font-weight:bold;font-size:60px;\">               Ooush <span style=\"color:#013545;\">Fitness</span>             </td>           </tr>           <tr>             <td style=\"padding:100px 30px 100px 30px;\">               <table role=\"presentation\" style=\"width:100%;border-collapse:collapse;border:0;border-spacing:0;color:#888888;\">                 <tr>                   <td style=\"padding:20px 0;font-size: 16px;\">                     {content}                   </td>                 </tr>                 <tr>                   <td style=\"padding:0;font-size: 16px;\">                     Best Regards                   </td>                 </tr>                 <tr>                   <td style=\"padding:0;font-size: 16px;\">                     The Ooush Team                   </td>                 </tr>               </table>             </td>           </tr>           <tr>             <td align=\"center\" style=\"padding:100px 0 100px 0;background:#0984AB; color:#FFFFFF; font-size: 16px;\">               Ooush Fitness &copy 2021             </td>           </tr>         </table>       </td>     </tr>   </table> </body> </html>";
    private final static String EMAIL_CONTENT = "<b>Dear Testy McTest</b><br/>"
            + "<br/>Thank you for registering an account with Ooush Fitness.<br/>"
            + "<br/>To complete your registration please verify your email address!<br/>"
            + "<br/>Click the link below to verify:"
            + "<br/>";
    private final static String WEB_BASE_URL = "http://localhost:3000";
    private final static String EMAIL_SUBJECT = "Welcome to Ooush Fitness";

    Users emailRecipient;
    EmailTemplate emailTemplate;
    MimeMessageHelper mimeMessageHelper;

    @Autowired
    private JavaMailSenderImpl mimeMailSender = new JavaMailSenderImpl();

    @Mock
    EmailTemplateRepository mockEmailTemplateRepository;

    @Mock
    AppSettingsServiceImpl mockAppSettingsService;

    @Mock
    JavaMailSenderImpl mockMimeMailSender;

    @Mock
    MimeMessage mockMimeMessage;

    @InjectMocks
    private RegisterUserEmailService target = new RegisterUserEmailService();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        EmailTemplate htmlEmailTemplate = new EmailTemplate();
        htmlEmailTemplate.setContent(HTML_EMAIL_TEMPLATE);

        emailRecipient = createTestUser();
        emailTemplate = htmlEmailTemplate;
        mockMimeMessage = mimeMailSender.createMimeMessage();
        mimeMessageHelper = new MimeMessageHelper(mockMimeMessage, "utf-8");

        when(mockAppSettingsService.constructAppBaseUrl()).thenReturn(WEB_BASE_URL);
        when(mockEmailTemplateRepository.getByTemplateName(EmailTemplateName.GENERIC_HTML_TEMPLATE)).thenReturn(htmlEmailTemplate);
        when(mockMimeMailSender.createMimeMessage()).thenReturn(new MimeMessage((Session) null));
    }

    @DisplayName("shouldSendRegistrationEmailSuccessfully")
    @Test
    void shouldSendRegistrationEmailSuccessfully() throws MessagingException, IOException {
        target.sendRegistrationEmail(emailRecipient);
    }

    @DisplayName("shouldReplaceTargetStringWithEmailContent")
    @Test
    void shouldReplaceTargetStringWithEmailContentAndAssignRecipientAndSubject() throws MessagingException, IOException {
        target.setEmailContent(mimeMessageHelper, emailTemplate, emailRecipient, EMAIL_SUBJECT, EMAIL_CONTENT);

        assertThat(mimeMessageHelper.getMimeMessage().getSubject(), Matchers.equalTo(EMAIL_SUBJECT));
        assertThat(((InternetAddress) Arrays.stream(
                mimeMessageHelper.getMimeMessage().getRecipients(Message.RecipientType.TO)).findFirst().get()).getAddress(), Matchers.equalTo(EMAIL));
        assertThat(((String) mimeMessageHelper.getMimeMessage().getContent()), Matchers.containsString(EMAIL_CONTENT));
        assertThat(((InternetAddress) Arrays.stream(
                mimeMessageHelper.getMimeMessage().getFrom()).findFirst().get()).getAddress(), Matchers.equalTo(NO_REPLY_EMAIL));
    }

    private Users createTestUser() {
        Users user = new Users();
        user.setEmail(EMAIL);
        user.setFirstName(FIRST_NAME);
        user.setLastName(LAST_NAME);
        user.setVerificationCode(VERIFICATION_CODE);

        return user;
    }
}