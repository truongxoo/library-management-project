package study.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import study.demo.entity.Role;

@Service
public interface RoleService {

    List<Role> findAllRoles();
    
    Optional<Role> findByRoleName(String roleName);

}
