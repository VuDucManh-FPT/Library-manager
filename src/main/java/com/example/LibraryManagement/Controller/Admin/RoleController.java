package com.example.LibraryManagement.Controller.Admin;

import com.example.LibraryManagement.Model.Role;
import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Repository.RoleRepository;
import com.example.LibraryManagement.Repository.StaffRepository;
import com.example.LibraryManagement.Response.NavbarResponse;
import com.example.LibraryManagement.Service.ServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.boot.jackson.JsonMixinModuleEntries;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
@AllArgsConstructor
public class RoleController {
    private final RoleRepository roleRepository;
    private final StaffRepository staffRepository;
    private final ServiceImpl serviceImpl;
    private final JsonMixinModuleEntries jsonMixinModuleEntries;

    @GetMapping("roles")
    public String roles(Model model, HttpServletRequest request) {
        List<Role> roles = roleRepository.findAll();
        NavbarResponse navbarData = serviceImpl.getNavbarAdminData(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        model.addAttribute("roles", roles);
        return "Admin/roles";
    }
    @GetMapping("/role/add")
    public String showAddRoleForm(Model model, HttpServletRequest request) {
        model.addAttribute("role", new Role());
        NavbarResponse navbarData = serviceImpl.getNavbarAdminData(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        return "Admin/role-add";
    }

    @PostMapping("/role/add")
    public String addRole(@ModelAttribute Role role, Model model, RedirectAttributes redirectAttributes) {
        if (roleRepository.getRoleByRoleName(role.getRoleName()) != null) {
            model.addAttribute("error", "Role name already exists!");
            return "Admin/role-add";
        }
        redirectAttributes.addFlashAttribute("success", "Role added successfully!");
        roleRepository.save(role);
        return "redirect:/admin/roles";
    }
    @GetMapping("/role/edit/{id}")
    public String showEditRoleForm(@PathVariable int id, Model model, HttpServletRequest request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));
        NavbarResponse navbarData = serviceImpl.getNavbarAdminData(request);
        model.addAttribute("email", navbarData.email);
        model.addAttribute("borrowIndexResponses", navbarData.borrowIndexResponses);
        model.addAttribute("numberOfBorrowedIndexes", navbarData.numberOfBorrowedIndexes);
        model.addAttribute("role", role);
        return "Admin/role-edit";
    }
    @PostMapping("/role/edit/{id}")
    public String updateRole(@PathVariable int id,
                             @RequestParam String roleName,
                             @RequestParam String description,RedirectAttributes redirectAttributes) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));

        role.setRoleName(roleName);
        role.setDescription(description);
        roleRepository.save(role);
        redirectAttributes.addFlashAttribute("success", "Role edited successfully!");
        return "redirect:/admin/roles";
    }
    @GetMapping("/role/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));
        if (staffRepository.existsStaffByRoleRoleName(role.getRoleName())) {
            redirectAttributes.addFlashAttribute("error", "There are staff have assigned role to this role!");
            return "redirect:/admin/roles";
        }
        roleRepository.delete(role);
        redirectAttributes.addFlashAttribute("success", "Role deleted successfully!");
        return "redirect:/admin/roles";
    }

}
