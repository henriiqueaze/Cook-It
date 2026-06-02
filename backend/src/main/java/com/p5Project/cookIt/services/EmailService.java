package com.p5Project.cookIt.services;

import com.p5Project.cookIt.exceptions.ExternalIntegrationException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String mailFromAddress;

    private static final String MAIL_FROM_NAME = "Cook It";

    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false, "UTF-8");

            helper.setFrom(new InternetAddress(mailFromAddress, MAIL_FROM_NAME));
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new ExternalIntegrationException("Erro ao enviar e-mail", e);
        }
    }

    public void sendEmailWithInlineLogo(String to, String subject, String bodyHtml) {
        sendEmail(to, subject, bodyHtml);
    }
}
