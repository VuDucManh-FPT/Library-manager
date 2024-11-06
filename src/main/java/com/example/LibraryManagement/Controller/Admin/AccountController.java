package com.example.LibraryManagement.Controller.Admin;

import com.example.LibraryManagement.Model.Role;
import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Model.Student;
import com.example.LibraryManagement.Repository.StaffRepository;
import com.example.LibraryManagement.Service.AdminService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class AccountController {
    private final AdminService adminService;
    private final StaffRepository staffRepository;

    @GetMapping("students")
    public String getAllStudentAccount(Model model) {
        List<Student> students = adminService.getAllStudents();
        model.addAttribute("students", students);
        return "Admin/students";
    }
    @GetMapping("staffs")
    public String getAllStaffAccount(Model model) {
        List<Staff> staffs = adminService.getAllStaffs();
        model.addAttribute("staffs", staffs);
        return "Admin/staffs";
    }
    @GetMapping("/staff/register")
    public String registerStaff(Model model) {
        List<Role> roles = adminService.getAllRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("user", new Staff());
        return "Admin/staff-add";
    }
    @GetMapping("/student/register")
    public String registerStudent(Model model) {
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
            redirectAttributes.addFlashAttribute("error", "Email already exists in students.");
            return "redirect:/admin/staff/register";
        }
        if ("Email already exists in staffs.".equals(saveStatus)) {
            redirectAttributes.addFlashAttribute("error", "Email already exists in staffs.");
            return "redirect:/admin/staff/register";
        }
        return "redirect:/admin/staffs";
    }
    @GetMapping("/staff/ban/{id}")
    public String banStaff(@PathVariable("id") int staffId, RedirectAttributes redirectAttributes) {
        adminService.banStaffById(staffId);
        redirectAttributes.addFlashAttribute("success", "Staff been ban successfully.");
        return "redirect:/admin/staffs";
    }
    @GetMapping("/staff/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer staffId, Model model) {
        Staff staff = adminService.findStaffById(staffId);
        List<Role> roles = adminService.getAllRoles();
        model.addAttribute("roles", roles);
        model.addAttribute("staff", staff);
        return "Admin/staff-update";
    }
    @PostMapping("/staff/update/{id}")
    public String updateStaff(@PathVariable("id") Integer staffId,
                              @ModelAttribute("staff") Staff staff,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "Admin/staff-update";
        }

        Staff existingStaff = adminService.findStaffById(staffId);
        if (existingStaff == null) {
            redirectAttributes.addFlashAttribute("error", "Staff not found.");
            return "redirect:/admin/staffs";
        }
        staff.setAvatar("user.png");

        existingStaff.setStaffName(staff.getStaffName());
        existingStaff.setStaffEmail(staff.getStaffEmail());
        existingStaff.setPhoneNumber(staff.getPhoneNumber());
        existingStaff.setAddress(staff.getAddress());
        existingStaff.setGender(staff.getGender());
        existingStaff.setDob(staff.getDob());
        existingStaff.setAge(staff.getAge());
        existingStaff.setActive(staff.isActive());
        existingStaff.setIsban(staff.isIsban());
        existingStaff.setAvatar(staff.getAvatar());

        adminService.updateStaff(existingStaff);

        redirectAttributes.addFlashAttribute("success", "Staff information updated successfully.");
        return "redirect:/admin/staffs";
    }
    @GetMapping("/student/update/{id}")
    public String studentUpdate(@PathVariable("id") Integer studentId, Model model) {
        Student student = adminService.findStudentById(studentId);
        model.addAttribute("student", student);
        return "Admin/student-update";
    }
    @PostMapping("/student/update/{id}")
    public String updateStaff(@PathVariable("id") Integer studentId,
                              @ModelAttribute("student") Student student,
                              BindingResult result,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "Admin/student-update";
        }

        Student existingStudent = adminService.findStudentById(studentId);
        if (existingStudent == null) {
            redirectAttributes.addFlashAttribute("error", "Student not found.");
            return "redirect:/admin/students";
        }
        student.setAvatar("user.png");

        existingStudent.setStudentName(student.getStudentName());
        existingStudent.setStudentEmail(student.getStudentEmail());
        existingStudent.setPhoneNumber(student.getPhoneNumber());
        existingStudent.setAddress(student.getAddress());
        existingStudent.setGender(student.getGender());
        existingStudent.setDob(student.getDob());
        existingStudent.setAge(student.getAge());
        existingStudent.setActive(student.isActive());
        existingStudent.setIsban(student.isIsban());
        existingStudent.setAvatar(student.getAvatar());

        adminService.updateStudent(existingStudent);

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
        adminService.deleteStudentById(studentId);
        redirectAttributes.addFlashAttribute("success", "Student been delete successfully.");
        return "redirect:/admin/students";
    }





}
