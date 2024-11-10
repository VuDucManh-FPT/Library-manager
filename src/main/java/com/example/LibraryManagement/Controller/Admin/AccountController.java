package com.example.LibraryManagement.Controller.Admin;

import com.example.LibraryManagement.Model.BorrowIndex;
import com.example.LibraryManagement.Model.Role;
import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.BorrowIndexRepository;
import com.example.LibraryManagement.Repository.StaffRepository;
import com.example.LibraryManagement.Repository.StudentRepository;
import com.example.LibraryManagement.Response.BorrowIndexNotifyResponse;
import com.example.LibraryManagement.Response.NavbarResponse;
import com.example.LibraryManagement.Security.JwtProvider;
import com.example.LibraryManagement.Service.AdminService;
import com.example.LibraryManagement.Service.BorrowIndexService;
import com.example.LibraryManagement.Service.ServiceImpl;
import com.example.LibraryManagement.Utils.FileUploadUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AccountController {
    private final AdminService adminService;
    private final StaffRepository staffRepository;
    private final JwtProvider jwtProvider;
    private final BorrowIndexService borrowIndexService;
    private final ServiceImpl serviceImpl;
    private final BorrowIndexRepository borrowIndexRepository;
    private final StudentRepository studentRepository;

    @GetMapping("students")
    public String getAllStudentAccount(Model model, HttpServletRequest request) {
        List<Student> students = adminService.getAllStudents();
        NavbarResponse navbarData = serviceImpl.getNavbarAdminData(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        model.addAttribute("students", students);
        return "Admin/students";
    }
    @GetMapping("staffs")
    public String getAllStaffAccount(Model model, HttpServletRequest request) {
        List<Staff> staffs = adminService.getAllStaffs();
        NavbarResponse navbarData = serviceImpl.getNavbarAdminData(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        model.addAttribute("staffs", staffs);
        return "Admin/staffs";
    }
    @GetMapping("/staff/register")
    public String registerStaff(Model model, HttpServletRequest request) {
        List<Role> roles = adminService.getAllRoles();
        NavbarResponse navbarData = serviceImpl.getNavbarAdminData(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        model.addAttribute("roles", roles);
        model.addAttribute("user", new Staff());
        return "Admin/staff-add";
    }
    @GetMapping("/student/register")
    public String registerStudent(Model model, HttpServletRequest request) {
        NavbarResponse navbarData = serviceImpl.getNavbarAdminData(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        model.addAttribute("user", new Student());
        return "Admin/student-add";
    }
    @PostMapping("/student/register")
    public String registerStudent(@Valid @ModelAttribute("student") Student student, RedirectAttributes redirectAttributes, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "Admin/student-add";
        }
        String saveStatus =  adminService.saveStudent(student);
        if ("Email already exists in students.".equals(saveStatus)) {
            redirectAttributes.addFlashAttribute("error", "Email already exists in students.");
            return "redirect:/admin/student/register";
        }
        if ("Email already exists in staffs.".equals(saveStatus)) {
            redirectAttributes.addFlashAttribute("error", "Email already exists in staffs.");
            return "redirect:/admin/student/register";
        }
        return "redirect:/admin/students";
    }
    @PostMapping("/staff/register")
    public String registerStaff(@Valid @ModelAttribute("staff") Staff staff, RedirectAttributes redirectAttributes, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "Admin/staff-add";
        }
        String saveStatus =  adminService.saveStaff(staff);
        if ("Email already exists in students.".equals(saveStatus)) {
            model.addAttribute("error", "Email already exists in students.");
            return "Admin/staff-add";
        }
        if ("Email already exists in staffs.".equals(saveStatus)) {
            model.addAttribute("error", "Email already exists in staffs.");
            return "Admin/staff-add";
        }
        redirectAttributes.addFlashAttribute("success", "Staff register successfully.");
        return "redirect:/admin/staffs";
    }
    @GetMapping("/staff/ban/{id}")
    public String banStaff(@PathVariable("id") int staffId, RedirectAttributes redirectAttributes) {
        adminService.banStaffById(staffId);
        redirectAttributes.addFlashAttribute("success", "Staff been ban successfully.");
        return "redirect:/admin/staffs";
    }
    @GetMapping("/staff/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer staffId, Model model, HttpServletRequest request) {
        Staff staff = adminService.findStaffById(staffId);
        List<Role> roles = adminService.getAllRoles();
        NavbarResponse navbarData = serviceImpl.getNavbarAdminData(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        model.addAttribute("roles", roles);
        model.addAttribute("staff", staff);
        return "Admin/staff-update";
    }
    @PostMapping("/staff/update/{id}")
    public String updateStaff(@PathVariable("id") Integer staffId,
                              @ModelAttribute("staff") Staff staff,
                              BindingResult result,
                              RedirectAttributes redirectAttributes, Model model ,@RequestParam("file") MultipartFile file) {
        if (result.hasErrors()) {
            return "Admin/staff-update";
        }

        Staff existingStaff = adminService.findStaffById(staffId);
        if (existingStaff == null) {
            redirectAttributes.addFlashAttribute("error", "Staff not found.");
            return "redirect:/admin/staffs";
        }
        String uploadDir = "src/main/resources/static/BookSto/assets/images/avatars";
        try {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                FileUploadUtil.saveFile(uploadDir, fileName, file);
                existingStaff.setAvatar(fileName);
            }
        existingStaff.setRole(staff.getRole());
        existingStaff.setStaffName(staff.getStaffName());
        existingStaff.setStaffEmail(staff.getStaffEmail());
        existingStaff.setPhoneNumber(staff.getPhoneNumber());
        existingStaff.setAddress(staff.getAddress());
        existingStaff.setGender(staff.getGender());
        existingStaff.setDob(staff.getDob());
        existingStaff.setAge(staff.getAge());
        existingStaff.setActive(staff.isActive());
        existingStaff.setIsban(staff.isIsban());

        adminService.updateStaff(existingStaff);

        redirectAttributes.addFlashAttribute("success", "Staff information updated successfully.");
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error occurred while storing the image!");
            return "Admin/staff-update";
        }
        return "redirect:/admin/staffs";
    }
    @GetMapping("/student/update/{id}")
    public String studentUpdate(@PathVariable("id") Integer studentId, Model model, HttpServletRequest request) {
        Student student = adminService.findStudentById(studentId);
        NavbarResponse navbarData = serviceImpl.getNavbarAdminData(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        model.addAttribute("student", student);
        return "Admin/student-update";
    }
    @PostMapping("/student/update/{id}")
    public String updateStaff(@PathVariable("id") Integer studentId,
                              @ModelAttribute("student") Student student,
                              BindingResult result,
                              RedirectAttributes redirectAttributes,@RequestParam("file") MultipartFile file, Model model) {
        if (result.hasErrors()) {
            return "Admin/student-update";
        }

        Student existingStudent = adminService.findStudentById(studentId);
        if (existingStudent == null) {
            redirectAttributes.addFlashAttribute("error", "Student not found.");
            return "redirect:/admin/students";
        }
        String uploadDir = "src/main/resources/static/BookSto/assets/images/avatars";
        try {
            if (!file.isEmpty()) {
                String fileName = file.getOriginalFilename();
                FileUploadUtil.saveFile(uploadDir, fileName, file);
                existingStudent.setAvatar(fileName);
            }

        existingStudent.setStudentName(student.getStudentName());
        existingStudent.setStudentEmail(student.getStudentEmail());
        existingStudent.setPhoneNumber(student.getPhoneNumber());
        existingStudent.setAddress(student.getAddress());
        existingStudent.setGender(student.getGender());
        existingStudent.setDob(student.getDob());
        existingStudent.setAge(student.getAge());
        existingStudent.setActive(student.isActive());
        existingStudent.setIsban(student.isIsban());

        adminService.updateStudent(existingStudent);
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("error", "Error occurred while storing the image!");
            return "Admin/student-update";
        }
        redirectAttributes.addFlashAttribute("success", "Student information updated successfully.");
        return "redirect:/admin/students";
    }
    @GetMapping("/student/ban/{id}")
    public String banStudent(@PathVariable("id") int studentId, RedirectAttributes redirectAttributes) {
        adminService.banStudentById(studentId);
        redirectAttributes.addFlashAttribute("success", "Student been ban successfully.");
        return "redirect:/admin/students";
    }
    @GetMapping("/student/delete/{id}")
    public String deleteStudent(@PathVariable("id") int studentId, RedirectAttributes redirectAttributes) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            List<BorrowIndex> borrowIndices = borrowIndexRepository.findByStudent(student.get());
            if (!borrowIndices.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Student had borrowed index, cannot delete student.");

            }
            redirectAttributes.addFlashAttribute("success", "Student been delete successfully.");
            adminService.deleteStudentById(studentId);
        }
        return "redirect:/admin/students";
    }





}
