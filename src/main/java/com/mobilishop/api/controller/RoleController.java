package com.mobilishop.api.controller;

import com.mobilishop.api.model.Role;
import com.mobilishop.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping
    public Role createNewRole(@RequestBody Role role)
    {
        return roleService.createOne(role);
    }

    @GetMapping("/{id}")
    public Role getUserById(@PathVariable Long id) {
        return roleService.findRoleById(id);
    }

    @GetMapping
    public List<Role> getAllRoles()
    {
        return roleService.findAllRoles();
    }
}
