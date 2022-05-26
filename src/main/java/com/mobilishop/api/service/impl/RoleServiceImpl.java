package com.mobilishop.api.service.impl;

import com.mobilishop.api.model.Role;
import com.mobilishop.api.repository.RoleRepository;
import com.mobilishop.api.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role createOne(Role role)
    {
        return roleRepository.save(role);
    }

    public List<Role> findAllRoles()
    {
        return roleRepository.findAll();
    }

    public Role findRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role findRoleByName(String name) {
        return roleRepository.findByName(name);
    }
}
