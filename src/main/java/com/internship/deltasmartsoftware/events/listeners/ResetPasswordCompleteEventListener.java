package com.internship.deltasmartsoftware.events.listeners;

import com.internship.deltasmartsoftware.events.model.ResetPasswordCompleteEvent;
import com.internship.deltasmartsoftware.model.User;
import com.internship.deltasmartsoftware.service.VerificationTokenService;
import com.internship.deltasmartsoftware.util.Translator;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class ResetPasswordCompleteEventListener implements ApplicationListener<ResetPasswordCompleteEvent> {

    private VerificationTokenService service;

    private final String frontUrl = "https://company-organization-software-three.vercel.app";

    private JavaMailSender mailSender;

    private User theUser;

    public ResetPasswordCompleteEventListener(VerificationTokenService service, JavaMailSender mailSender) {
        this.service = service;
        this.mailSender = mailSender;
    }

    @Override
    public void onApplicationEvent(ResetPasswordCompleteEvent event) {
        theUser = event.getUser();
        String verificationToken = UUID.randomUUID().toString();
        service.saveUserVerificationToken(theUser, verificationToken);
        String url = frontUrl+"/resetPassword/"+verificationToken;
        try {
            sendResetPasswordEmail(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        log.info("Click the link to verify your registration :  {}", url);
    }

    private void sendResetPasswordEmail(String url) throws Exception {
        String subject = Translator.toLocale("email.resetPassword");
        String senderName = Translator.toLocale("email.rpps");
        String mailContent = "<p> Hi, "+ theUser.getName()+
                " " + Translator.toLocale("email.clickHereToVerify") + ".</p>"+
                "<a href=\"" +url+ "\">" + Translator.toLocale("email.resetYourPassword") + "</a>"+
                "<p>" + Translator.toLocale("email.thanks") + "<br>" + Translator.toLocale("email.rpps");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        messageHelper.setFrom("company@gmail.com", senderName);
        messageHelper.setTo(theUser.getEmail());
        messageHelper.setSubject(subject);
        messageHelper.setText(mailContent, true);
        mailSender.send(message);
    }
}
