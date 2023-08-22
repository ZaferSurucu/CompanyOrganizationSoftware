package com.internship.deltasmartsoftware.events.listeners;

import com.internship.deltasmartsoftware.events.model.RegistrationCompleteEvent;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.service.VerificationTokenService;
import com.internship.deltasmartsoftware.util.Translator;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

@Slf4j
@Component
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private VerificationTokenService userService;

    private final String frontUrl = "https://company-organization-software.vercel.app/";

    private JavaMailSender mailSender;

    public RegistrationCompleteEventListener(VerificationTokenService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    private User theUser;
    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {
        theUser = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        userService.saveUserVerificationToken(theUser, verificationToken);
        String url = frontUrl+"/setNewPassword/"+verificationToken;
        try {
            sendVerificationEmail(url);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        log.info("Click the link to verify your registration :  {}", url);
    }
    private void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
        String subject = Translator.toLocale("email.verification");
        String senderName = Translator.toLocale("email.urps");
        String mailContent = "<p> "+ Translator.toLocale("email.hi") + ", "+ theUser.getName()+ ", </p>"+
                "<p>" + Translator.toLocale("email.thankForRegistering")+","+
                Translator.toLocale("email.followLinkToVerify") +".</p>"+
                "<a href=\"" +url+ "\">" + Translator.toLocale("email.verifyEmail") + "</a>"+
                "<p>" + Translator.toLocale("email.thanks") + "<br>" + Translator.toLocale("email.urps");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("company@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
