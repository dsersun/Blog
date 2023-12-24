package org.sersun.simpleblog.service;

import lombok.extern.slf4j.Slf4j;
import org.sersun.simpleblog.model.UserRoles;
import org.sersun.simpleblog.model.Users;
import org.sersun.simpleblog.repository.UserRepository;
import org.sersun.simpleblog.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class UsersService {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserRoleRepository userRoleRepository;

    public void createUser(Users user){
        try {
            String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setPassword(encodedPassword);
            userRepository.save(user);
            log.info("User {} created", user.getUsername());

            // assign reader role to new user
            userRoleRepository.save(new UserRoles(null, user.getUserId(), 4));
            
        }catch (Exception e){
            log.error("Error creating user {}", user.getUsername(), e);
        }
    }

}
