package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.AdminRepository;
import com.example.LibraryManagement.Repository.StaffRepository;
import com.example.LibraryManagement.Repository.StudentRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ProfileService {
    private final StudentRepository studentRepository;
    private final StaffRepository staffRepository;
    private final AdminRepository adminRepository;
    private static final String UPLOAD_DIR = "uploads/";

    public Optional<Object> getUserByEmail(String email) {
        return studentRepository.findByStudentEmail(email)
                .map(user -> (Object) user)
                .or(() -> staffRepository.findByStaffEmail(email).map(user -> (Object) user))
                .or(() -> adminRepository.findAdminByEmail(email).map(user -> (Object) user));
    }
    public String getUserEmailFromCookie(HttpServletRequest request) {
        for (Cookie cookie : request.getCookies()) {
            if ("userEmail".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
    public String handleAvatarUpload(MultipartFile avatarFile) {
//        if (!avatarFile.isEmpty()) {
//            try {
//                String fileName = Paths.get(avatarFile.getOriginalFilename()).getFileName().toString();
//                Path filePath = Paths.get(UPLOAD_DIR, fileName);
//                Files.createDirectories(filePath.getParent());
//                Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//                return filePath.toString();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
        if (!avatarFile.isEmpty()) {
            try {
                String fileName = Paths.get(avatarFile.getOriginalFilename()).getFileName().toString();
                String uploadDir = "/uploads";
                Files.createDirectories(Paths.get(uploadDir));
                Path filePath = Paths.get("uploads/", fileName);

                Files.copy(avatarFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                // Trả về đường dẫn tương đối của file
                System.out.println("File uploaded successfully"+filePath.toString());
                return filePath.toString();
            } catch (IOException e) {
                System.out.println(e.getMessage());
                System.out.println("failed upload");
                e.printStackTrace();
            }
        }
        return null;
    }
    public void updateStudentProfile(Student student, String fullName, LocalDate dob, String phoneNumber, String address, int age, String gender, String avatarUrl) {
        student.setStudentName(Optional.ofNullable(fullName).filter(name -> !name.trim().isEmpty()).orElse(student.getStudentName()));
        student.setDob(Optional.ofNullable(dob).orElse(student.getDob()));
        student.setPhoneNumber(Optional.ofNullable(phoneNumber).filter(phone -> !phone.trim().isEmpty()).orElse(student.getPhoneNumber()));
        student.setAddress(Optional.ofNullable(address).filter(addr -> !addr.trim().isEmpty()).orElse(student.getAddress()));
        student.setAge(age > 0 ? age : student.getAge());
        student.setGender(Optional.ofNullable(gender).filter(g -> !g.trim().isEmpty()).orElse(student.getGender()));
        if (avatarUrl != null) {
            student.setAvatar(avatarUrl);
        }
        studentRepository.save(student);
    }
    public void updateStaffProfile(Staff staff, String fullName, LocalDate dob, String phoneNumber, String address, int age, String gender, String avatarUrl) {
        staff.setStaffName(Optional.ofNullable(fullName).filter(name -> !name.trim().isEmpty()).orElse(staff.getStaffName()));
        staff.setDob(Optional.ofNullable(dob).orElse(staff.getDob()));
        staff.setPhoneNumber(Optional.ofNullable(phoneNumber).filter(phone -> !phone.trim().isEmpty()).orElse(staff.getPhoneNumber()));
        staff.setAddress(Optional.ofNullable(address).filter(addr -> !addr.trim().isEmpty()).orElse(staff.getAddress()));
        staff.setAge(age > 0 ? age : staff.getAge());
        staff.setGender(Optional.ofNullable(gender).filter(g -> !g.trim().isEmpty()).orElse(staff.getGender()));
        if (avatarUrl != null) {
            staff.setAvatar(avatarUrl);
        }
        staffRepository.save(staff);
    }

}
