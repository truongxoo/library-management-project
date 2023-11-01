package study.demo;

import java.util.List;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import study.demo.entity.Admin;
import study.demo.entity.Member;
import study.demo.entity.Role;
import study.demo.entity.User;
import study.demo.enums.ERole;
import study.demo.enums.EUserStatus;
import study.demo.repository.RoleRepository;
import study.demo.repository.UserRepository;

@SpringBootApplication
public class LibraryManagementDemoApplication {
    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    public PasswordEncoder encode;

	public static void main(String[] args) {
		SpringApplication.run(LibraryManagementDemoApplication.class, args);
	}

//	 @Bean
//  CommandLineRunner Demo() {
//      return args -> {
//          Role role = Role.builder().roleName(ERole.ADMIN).build();
//          Role role1 = Role.builder().roleName(ERole.MEMBER).build();
//          Admin admin1 = new Admin();
//          admin1.setEmail("admin@gmail.com");
//          admin1.setPassword(encode.encode("admin"));
//          admin1.setUserStatus(EUserStatus.ACTIVATED);
//          Member member = new Member();
//          member.setEmail("member@gmail.com");
//          member.setPassword(encode.encode("member"));
//          member.setUserStatus(EUserStatus.ACTIVATED);
//
//          List<User> user1 = List.of(member);
//          List<User> user = List.of(admin1);
//          role.setUser(user);
//          role1.setUser(user1);
//          admin1.setRole(role);
//          member.setRole(role1);
//          roleRepository.save(role1);
//          roleRepository.save(role);
//          userRepository.save(member);
//          userRepository.save(admin1);
//      };
//  }

}
