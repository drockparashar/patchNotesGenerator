package PatchNotes.patchNoteGenerator.service;

import PatchNotes.patchNoteGenerator.util.RecipientList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String[] to,String subject,String body){
        for (String recipient:to) {
            SimpleMailMessage message=  new SimpleMailMessage();
            message.setFrom("pranshu007parashar@gmail.com");
            message.setTo(recipient);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
        }
    }
}
