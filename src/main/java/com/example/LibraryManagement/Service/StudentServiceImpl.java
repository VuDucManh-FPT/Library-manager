package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.StudentRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

@Service
public class StudentServiceImpl {
    @Autowired
    JavaMailSender mailSender;
    @Autowired
    private StudentRepository studentRepository;
    public String sendMail(Student student, String htmlContent, String subject) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(student.getStudentEmail());
            helper.setSubject(subject);

            helper.setText("<p>Hello " + student.getStudentName() + ",</p>" + htmlContent + "<p>Regards,<br>ECoffee<br>CoffeeWithLove.</p>", true);
            mailSender.send(message);
            return "Success";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error";
        }
    }
    public Student getStudentByEmail(String email) {
        return studentRepository.findByStudentEmail(email);
    }
    public Student getStudentRequesting(HttpServletRequest request) {
        // Giả sử bạn lấy email từ session hoặc token request
        String email = (String) request.getSession().getAttribute("email");
        return getStudentByEmail(email);
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public int generateRandom8DigitNumber() {
        return ThreadLocalRandom.current().nextInt(10000000, 100000000);
    }
}
