package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.BorrowIndex;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.BorrowIndexRepository;
import com.example.LibraryManagement.Repository.StudentRepository;
import com.example.LibraryManagement.Request.ForgotPassRequest;
import com.example.LibraryManagement.Response.BorrowIndexNotifyResponse;
import com.example.LibraryManagement.Response.NavbarResponse;
import com.example.LibraryManagement.Security.JwtProvider;
import jakarta.annotation.PostConstruct;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ServiceImpl implements com.example.LibraryManagement.Service.Service {
    JavaMailSender mailSender;
    private BorrowIndexService borrowIndexService;
    private JwtProvider jwtProvider;

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

            String finalContent = "<p>Hello,</p>" + htmlContent + "<p>Regards,<br>Library<br>Books is life.</p>";
            helper.setText(finalContent, true);
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

    public NavbarResponse getNavbarAdminData(HttpServletRequest request) {
        String token = jwtProvider.getJwtFromCookies(request);
        String email = jwtProvider.getEmail(token);
        List<BorrowIndex> borrowIndices = borrowIndexService.findBorrowIndexNearEstimateTime();
        List<BorrowIndexNotifyResponse> borrowIndexResponses = borrowIndices.stream()
                .map(borrowIndex -> {
                    Date estimatedDate = borrowIndex.getEstimateDate();
                    Date currentDate = new Date();
                    long timeDifference = currentDate.getTime() - estimatedDate.getTime();
                    long daysDifference = TimeUnit.MILLISECONDS.toDays(timeDifference);
                    if (currentDate.after(estimatedDate)) {
                        daysDifference = -daysDifference;
                    }
                    return BorrowIndexNotifyResponse.builder()
                            .estimatedReturnDate(estimatedDate)
                            .daysRemaining(daysDifference)
                            .bookName(borrowIndex.getBook().getBookName())
                            .studentName(borrowIndex.getStudent().getStudentName())
                            .studentEmail(borrowIndex.getStudent().getStudentEmail())
                            .build();
                })
                .collect(Collectors.toList());

        int numberOfBorrowedIndexes = borrowIndexResponses.size();

        return new NavbarResponse(email,borrowIndexResponses, numberOfBorrowedIndexes);
    }
    public NavbarResponse getNavbarStudent(HttpServletRequest request) {
        String token = jwtProvider.getJwtFromCookies(request);
        String email = jwtProvider.getEmail(token);
        List<BorrowIndex> borrowIndices = borrowIndexService.findBorrowIndexNearEstimateTimeByStudent(email);
        List<BorrowIndexNotifyResponse> borrowIndexResponses = borrowIndices.stream()
                .map(borrowIndex -> {
                    Date estimatedDate = borrowIndex.getEstimateDate();
                    Date currentDate = new Date();
                    long timeDifference = currentDate.getTime() - estimatedDate.getTime();
                    long daysDifference = TimeUnit.MILLISECONDS.toDays(timeDifference);
                    if (currentDate.after(estimatedDate)) {
                        daysDifference = -daysDifference;
                    }
                    return BorrowIndexNotifyResponse.builder()
                            .estimatedReturnDate(estimatedDate)
                            .daysRemaining(daysDifference)
                            .bookName(borrowIndex.getBook().getBookName())
                            .studentName(borrowIndex.getStudent().getStudentName())
                            .studentEmail(borrowIndex.getStudent().getStudentEmail())
                            .build();
                })
                .collect(Collectors.toList());

        int numberOfBorrowedIndexes = borrowIndexResponses.size();

        return new NavbarResponse(email,borrowIndexResponses, numberOfBorrowedIndexes);
    }
}
