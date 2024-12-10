package com.eshop.app.service.impl;

import com.eshop.app.service.MailService;
import com.eshop.app.config.MessageConfig;
import com.eshop.app.service.MailService;
import com.eshop.app.service.OneTimePasswordService;
import com.eshop.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;
    private final OneTimePasswordService oneTimePasswordService;
    private final UserService userService;
    private final MessageConfig messages;
    private final ResourceLoader resourceLoader;
    @Value("${site.url}")
    String siteUrl;

    @Override
    public void send(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(body, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("admin@" + siteUrl, "Support");
            mailSender.send(mimeMessage);
        } catch (Exception e) {
            log.error("failed to send email. {}", e.getMessage());
            throw new IllegalStateException("failed to send email." + e.getMessage());
        }
    }

    @SneakyThrows
    @Override
    public void sendOTP(String to, String subject) {
        var entity = userService.findByEmail(to);
        var otp = oneTimePasswordService.create(entity.getId());
        String appName = messages.getMessage("siteName");
        Resource emailTemplateResource = resourceLoader.getResource("classpath:templates/otp-email.html");
        String emailContent = new String(Files.readAllBytes(Paths.get(emailTemplateResource.getURI())));
        emailContent = emailContent.replace("[user_first_name]", entity.getFirstName());
        emailContent = emailContent.replace("[YourAppName]", appName);
        emailContent = emailContent.replace("[otp_code]", otp);

        send(to, subject, emailContent);
    }

    @Override
    @SneakyThrows
    public void sendVerification(String to, String subject) {
        var user = userService.findByEmail(to);
        var otp = oneTimePasswordService.create(user.getId());
        String appName = messages.getMessage("siteName");
        String link = String.format("%s/api/v1/user/verify-email/%s",siteUrl,user.getId().toString(), otp);
        Resource emailTemplateResource = resourceLoader.getResource("classpath:templates/verification-email.html");
        String emailContent = new String(Files.readAllBytes(Paths.get(emailTemplateResource.getURI())));
        emailContent = emailContent.replace("[user_first_name]", user.getFirstName());
        emailContent = emailContent.replace("[YourAppName]", appName);
        emailContent = emailContent.replace("[verification_code]", link);
        emailContent = emailContent.replace("[verification_link]", link);

        send(to, subject, emailContent);
    }
}
