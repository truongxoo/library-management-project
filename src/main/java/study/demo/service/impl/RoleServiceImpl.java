package study.demo.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import study.demo.entity.Role;
import study.demo.repository.RoleRepository;
import study.demo.service.RoleService;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
	
	private final RoleRepository roleRepository;
	
	@Override
	public List<Role> findAllRoles() {
		return roleRepository.findAll();
	}

    @Override
    public Optional<Role> findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

}
