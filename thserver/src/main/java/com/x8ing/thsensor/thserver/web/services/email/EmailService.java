package com.x8ing.thsensor.thserver.web.services.email;

import com.x8ing.thsensor.thserver.utils.MailSend;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/email")
public class EmailService {

    private final MailSend mailSend;

    public EmailService(MailSend mailSend) {
        this.mailSend = mailSend;
    }

    @RequestMapping("/send")
    @ResponseBody
    public String send(
            @RequestParam(defaultValue = "Test from raspberry") String subject,
            @RequestParam(defaultValue = "This is a test only") String content) {
        return mailSend.send(subject, content);
    }
}
