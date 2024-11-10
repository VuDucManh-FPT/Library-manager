package com.example.LibraryManagement.Service;

import com.example.LibraryManagement.Model.BorrowIndex;
import com.example.LibraryManagement.Model.Role;
import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.BorrowIndexRepository;
import com.example.LibraryManagement.Repository.RoleRepository;
import com.example.LibraryManagement.Repository.StaffRepository;
import com.example.LibraryManagement.Repository.StudentRepository;
import com.example.LibraryManagement.Request.ForgotPassRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final BorrowIndexRepository borrowIndexRepository;
    private StaffRepository staffRepository;
    private StudentRepository studentRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private ServiceImpl service;
    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Staff> getAllStaffs() {
        return staffRepository.findAll();
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public String saveStaff(Staff staff) {
        if (staffRepository.existsByStaffEmail(staff.getStaffEmail())) {
            return "Email already exists in staffs.";
        }
        if (studentRepository.existsStudentByStudentEmail(staff.getStaffEmail())) {
            return "Email already exists in students.";
        }
        String password = generateRandomString();
        staff.setStaffPassword(passwordEncoder.encode(password));
        // Kiểm tra avatar, nếu không có thì dùng ảnh mặc định
        if (staff.getAvatar() == null || staff.getAvatar().isEmpty()) {
            staff.setAvatar("user.png");
        }
        staff.setActive(true);
        Staff savedStaff = staffRepository.save(staff);

        // Gửi email xác nhận
        String subject = "Account Created Successfully";
        String htmlContent = "<p>Your account has been created successfully with the following details:</p>" +
                "<p>Email: " + savedStaff.getStaffEmail() + "</p>" +
                "<p>Password: " + password + "</p>" +
                "<p>Please log in and change your password immediately.</p>";
        ForgotPassRequest request = new ForgotPassRequest();
        request.setEmail(savedStaff.getStaffEmail());
        service.sendMail(request, htmlContent, subject);
        return "Added Successfully";
    }
    @Override
    public Staff findStaffById(int staffId) {
        return staffRepository.findById(staffId).orElse(null);
    }

    @Override
    public Staff banStaffById(int staffId) {
        Staff staff = staffRepository.findById(staffId).orElse(null);
        staff.setIsban(true);
        return staffRepository.save(staff);
    }
    @Override
    public Student banStudentById(int studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        student.setIsban(true);
        return studentRepository.save(student);
    }

    @Override
    public void deleteStudentById(int studentId) {
        Student student = studentRepository.findById(studentId).orElse(null);
        List<BorrowIndex> borrowIndices = borrowIndexRepository.findByStudent(student);
        if(!borrowIndices.isEmpty()) {
            borrowIndexRepository.deleteAll(borrowIndices);
        }
        studentRepository.delete(student);
    }

    @Override
    public Staff updateStaff(Staff staff) {
        return staffRepository.save(staff);
    }

    @Override
    public String saveStudent(Student student) {
        if (studentRepository.existsStudentByStudentEmail(student.getStudentEmail())) {
            return "Email already exists in students.";
        }
        if (staffRepository.existsByStaffEmail(student.getStudentEmail())) {
            return "Email already exists in staffs.";
        }
        String password = generateRandomString();
        student.setPassword(passwordEncoder.encode(password));
        // Kiểm tra avatar, nếu không có thì dùng ảnh mặc định
        if (student.getAvatar() == null || student.getAvatar().isEmpty()) {
            student.setAvatar("user.png");
        }
        student.setActive(true);
        Student save = studentRepository.save(student);

        // Gửi email xác nhận
        String subject = "Account Created Successfully";
        String htmlContent = "<p>Your account has been created successfully with the following details:</p>" +
                "<p>Email: " + save.getStudentEmail() + "</p>" +
                "<p>Password: " + password + "</p>" +
                "<p>Please log in and change your password immediately.</p>";
        ForgotPassRequest request = new ForgotPassRequest();
        request.setEmail(save.getStudentEmail());
        service.sendMail(request, htmlContent, subject);
        return "Added Successfully";
    }

    @Override
    public Student updateStudent(Student studentId) {
        return studentRepository.save(studentId);
    }

    @Override
    public Student findStudentById(int studentId) {
        return studentRepository.findById(studentId).orElse(null);
    }

    public static String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+{}:\"<>?|[];',./`~";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(8);

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }

        return sb.toString();
    }

}
