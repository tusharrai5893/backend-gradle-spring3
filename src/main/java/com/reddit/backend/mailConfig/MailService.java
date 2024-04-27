package com.reddit.backend.mailConfig;

import com.reddit.backend.exceptions.RedditCustomException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class MailService {

    private final CustomMailContentBuilder CustomMailContentBuilder;
    private final JavaMailSender javaMailSender;

    @Async
    public void sendMail(NotificationEmail notificationEmail) {
        MimeMessagePreparator preparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom("customMail@gmail.com");
            mimeMessageHelper.setTo(notificationEmail.getRecipient());
            mimeMessageHelper.setSubject(notificationEmail.getSubject());

            mimeMessageHelper.setText(
                    CustomMailContentBuilder.buildMail(notificationEmail.getBody(), notificationEmail.getLink(), notificationEmail.getMsg()),
                    true);
        };

        try {
            javaMailSender.send(preparator);
            log.info("Activation Email Has been sent to your Mail, Please verify");

        } catch (MailException e) {
            throw new RedditCustomException("Exception occured while sending mail to " + notificationEmail.getRecipient());
        }

    }
}
