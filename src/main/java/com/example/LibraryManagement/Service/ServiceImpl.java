package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.Student;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ServiceImpl implements Service{
    @Autowired
    JavaMailSender mailSender;
    public String sendMail(Optional<Student> student, String htmlContent, String subject) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(student.get().getStudentEmail());
            helper.setSubject(subject);

            helper.setText("<p>Hello " + student.get().getStudentName() + ",</p>" + htmlContent + "<p>Regards,<br>Library<br>Books is life.</p>", true);
            mailSender.send(message);
            return "Success";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error";
        }
    }

    public int generateRandom8DigitNumber() {
        return ThreadLocalRandom.current().nextInt(10000000, 100000000);
    }

    @Override
    public String value() {
        return "";
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return null;
    }
}
