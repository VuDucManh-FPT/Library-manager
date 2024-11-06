package com.example.LibraryManagement.Controller.Admin;

import com.example.LibraryManagement.Model.Role;
import com.example.LibraryManagement.Model.Staff;
import com.example.LibraryManagement.Repository.RoleRepository;
import com.example.LibraryManagement.Repository.StaffRepository;
import lombok.AllArgsConstructor;
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
    @GetMapping("roles")
    public String roles(Model model) {
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "Admin/roles";
    }
    @GetMapping("/role/add")
    public String showAddRoleForm(Model model) {
        model.addAttribute("role", new Role());
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
    public String showEditRoleForm(@PathVariable int id, Model model) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));
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
    @PostMapping("/role/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid role Id:" + id));
        if (staffRepository.existsStaffByRoleRoleName(role.getRoleName())) {
            redirectAttributes.addAttribute("error", "A staff have assigned role to this role!");
            return "redirect:/admin/roles";
        }
        roleRepository.delete(role);
        redirectAttributes.addFlashAttribute("success", "Role deleted successfully!");
        return "redirect:/admin/roles";
    }

}
