package com.mobilishop.api.service;

import com.mobilishop.api.model.Role;
import java.util.List;

public interface RoleService {
    Role createOne(Role role);

    List<Role> findAllRoles();

    Role findRoleById(Long id);

    Role findRoleByName(String name);
}
