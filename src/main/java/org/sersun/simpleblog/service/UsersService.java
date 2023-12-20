package org.sersun.simpleblog.service;

import lombok.extern.slf4j.Slf4j;
import org.sersun.simpleblog.model.Users;
import org.sersun.simpleblog.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UsersService {
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    UserRepository userRepository;

    public void createUser(Users user){
        try {
            String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setPassword(encodedPassword);
            userRepository.save(user);
            log.info("User {} created", user.getUsername());
        }catch (Exception e){
            log.error("Error creating user {}", user.getUsername(), e);
        }
    }
}
