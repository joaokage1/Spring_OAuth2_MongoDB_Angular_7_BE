package com.toddy.ws.config;

import com.toddy.ws.domain.Role;
import com.toddy.ws.domain.User;
import com.toddy.ws.repository.RoleRepository;
import com.toddy.ws.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@Configuration
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        userRepository.deleteAll();
        roleRepository.deleteAll();

        User joao = new User("João", "Souza", "joao@gmail.com");
        User maria = new User("Maria", "Teixeira", "maria@gmail.com");
        Role roleAdmin = createRoleIfNotFound("ROLE_ADMIN");
        Role roleUser = createRoleIfNotFound("ROLE_USER");

        joao.addRole(roleAdmin);
        joao.setPassword(passwordEncoder.encode("123"));
        joao.setEnabled(true);

        maria.addRole(roleUser);
        maria.setPassword(passwordEncoder.encode("456"));
        maria.setEnabled(true);

        createUserIfNotFound(joao);
        createUserIfNotFound(maria);

    }

    private User createUserIfNotFound(final User user){
        Optional<User> obj = userRepository.findByEmail(user.getEmail());
        if (obj.isPresent()){
            return obj.get();
        }

        return userRepository.save(user);
    }

    private Role createRoleIfNotFound(String name){
        Optional<Role> role = roleRepository.findByName(name);
        if (role.isPresent()){
            return role.get();
        }
        return roleRepository.save(new Role(name));
    }
}
