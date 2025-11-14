package pl.xsware.domain.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import pl.xsware.domain.model.data.ContactForm;

@Slf4j
@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.mail.to}")
    private String mailTo;

    @Value("${spring.mail.username}")
    private String mailFrom;

    @Async
    public void sendContactMail(ContactForm req) {

        log.info("\nContact Form: \nto: {}, from: {}, phone: {}, company: {}, message: {}", mailTo, req.getEmail(), req.getPhone(), req.getCompany(), req.getMessage());

        var msg = new SimpleMailMessage();
        msg.setTo(mailTo);
        msg.setFrom(mailFrom);
        msg.setSubject("Nowa wiadomość z formularza kontaktowego");
        msg.setText(
            """
            Imię i nazwisko: %s
            Email: %s
            Telefon: %s
            Firma: %s
    
            Wiadomość:
                    %s
            """.formatted(
                req.getName(),
                req.getEmail(),
                nullToDash(req.getPhone()),
                nullToDash(req.getCompany()),
                req.getMessage()
            )
        );

        mailSender.send(msg);
    }

    private static String nullToDash(String v) {
        return (v == null || v.isBlank()) ? "-" : v;
    }

}
