package com.x8ing.thsensor.thserver.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.Properties;

@Service
public class MailSend {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${thserver.mail.sender}")
    private String mailSender = "patrick.heusser.raspberry@gmail.com";
    // private String mailSender = "raspberry@x8ing.com";

    @Value("${thserver.mail.password}")
    private String mailPassword;

    @Value("${thserver.mail.receivers}")
    private String receiverMailsDefault;


    public void init() {

    }

    public String send(String subject, String content) {
        return send(subject, content, receiverMailsDefault);
    }

    public String send(String subject, String content, String receiverMails) {

        if (StringUtils.isEmpty(mailSender) || StringUtils.isEmpty(mailPassword) || StringUtils.isEmpty(receiverMails)) {
            String msg = "Did not send email, as the configuration was invalid or no receiver was given.";
            log.error(msg);
            throw new ThException(msg);
        }

        Properties prop = new Properties();
        // Sending email from gmail
        // String host = "mail.your-server.de";
        String host = "smtp.gmail.com";

        // Port of SMTP
        String port = "587";

        prop.put("mail.smtp.socketFactory.port", port);
        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.host", host);
        prop.put("mail.smtp.port", port);
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(mailSender, mailPassword);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(mailSender));
            message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(receiverMails, true));
            message.setSubject(subject);
            message.setText(content);

            Transport.send(message);

            log.info("Sent mail to=" + receiverMails + " subject=" + subject + " content=" + content);

        } catch (MessagingException e0) {
            RuntimeException e1 = new RuntimeException("Could not send email to " + receiverMails, e0);
            log.error("Email not sent", e1);
            throw e1;
        }

        return "Email sent with success";

    }

    public static void main(String[] args) throws Exception {
        new MailSend().send("Raspi-Test", LocalDateTime.now().toString(), "patrick.heusser@gmail.com, patrick.heusser.raspberry@gmail.com");
    }
}
