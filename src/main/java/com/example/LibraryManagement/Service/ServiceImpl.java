package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.BorrowIndex;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.BorrowIndexRepository;
import com.example.LibraryManagement.Repository.StudentRepository;
import com.example.LibraryManagement.Request.ForgotPassRequest;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ServiceImpl implements com.example.LibraryManagement.Service.Service {
    JavaMailSender mailSender;
    private BorrowIndexService borrowIndexService;

    @Override
    public void sendProjectStartNotificationToStudents() {
        // Lấy danh sách BorrowIndex thỏa mãn điều kiện
        List<BorrowIndex> borrowIndexes = borrowIndexService.findBorrowIndexNearEstimateTime();

        String subject = "Project Start Notification";
        String htmlContent = "This is a reminder that the project has started. Please take necessary actions.";

        for (BorrowIndex borrowIndex : borrowIndexes) {
            Student student = borrowIndex.getStudent();

            if (student != null && student.getStudentEmail() != null) {
                ForgotPassRequest forgotPassRequest = new ForgotPassRequest();
                forgotPassRequest.setEmail(student.getStudentEmail());
                sendMail(forgotPassRequest, htmlContent, subject);
            }
        }
    }
    public String sendMail(ForgotPassRequest forgotPassRequest, String htmlContent, String subject) {
        try {
            MimeMessage message = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(forgotPassRequest.getEmail());
            helper.setSubject(subject);

            helper.setText("<p>Hello  </p>" + htmlContent + "<p>Regards,<br>Library<br>Books is life.</p>", true);
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

}
