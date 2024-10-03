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
    public Student getStudentById(int id) {
        return studentRepository.findById(id).orElse(null);
    }

    public Student updateProfile(int id, Student updatedStudent) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null) {
            student.setStudentEmail(updatedStudent.getStudentEmail());
            student.setStudentName(updatedStudent.getStudentName());
            studentRepository.save(student);
        }
        return student;
    }

    public boolean changePassword(int id, String oldPassword, String newPassword) {
        Student student = studentRepository.findById(id).orElse(null);
        if (student != null && student.getPassword().equals(oldPassword)) {
            student.setPassword(newPassword);
            studentRepository.save(student);
            return true;
        }
        return false;
    }

    public int generateRandom8DigitNumber() {
        return ThreadLocalRandom.current().nextInt(10000000, 100000000);
    }
}
