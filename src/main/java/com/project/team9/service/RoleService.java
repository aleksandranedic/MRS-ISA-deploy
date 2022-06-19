package com.project.team9.service;

import com.project.team9.model.user.Role;
import com.project.team9.repo.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> findAll() {
        return this.roleRepository.findAll();
    }

    public Role findOne(Long id) {
        return this.roleRepository.findById(id).orElse(null);
    }

    public Role findRoleByName(String roleName){
        for (Role role: this.roleRepository.findAll()) {
            if(role.getName().equals(roleName)){
                return role;
            }
        }
        return null;
    }

    public Role save(Role role) {
        return this.roleRepository.save(role);
    }
}
