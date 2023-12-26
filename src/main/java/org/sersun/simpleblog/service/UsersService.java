package org.sersun.simpleblog.service;

import lombok.extern.slf4j.Slf4j;
import org.sersun.simpleblog.model.RoleID;
import org.sersun.simpleblog.model.Users;
import org.sersun.simpleblog.repository.UserRepository;
import org.sersun.simpleblog.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
    @Autowired
    UserRolesService userRolesService;
    @Value("${account.request.email}")
    private String accountRequestEmail;
    AsyncEmailService asyncEmailService = new AsyncEmailService();

    public void createUser(Users user){
        try {
            // encode password
            String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            user.setPassword(encodedPassword);

            userRepository.save(user);
            log.info("User {} created", user.getUsername());

            // assign reader role to new user
            userRolesService.addReaderRole(user.getUserId());
            log.info("User {} assigned reader role", user.getUsername());
        }catch (Exception e){
            log.error("Error creating user {}", user.getUsername(), e);
        }
    }

    // assign role to User by userId and by RoleID
    public void addRole(Long userId, RoleID roleID){
        userRolesService.assignRole(userId, roleID); // roleId = ADMIN, MODERATOR, READER, BLOGGER
        log.info("User {} assigned role {}", userId, roleID);
        asyncEmailService.sendEmail(userRepository.findByUserId(userId).getEmail(),
                "Role assigned",
                "Role " + roleID + " assigned to your account "
        );
        log.info("Email notification sent to {}", userRepository.findByUserId(userId).getEmail());
    }


    // modify user
    public void updateUser(Long userId, Users user) {
        try {
            Users existingUser = userRepository.findById(userId)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found"));
            user.setUserId(existingUser.getUserId());
            userRepository.save(user);
            log.info("User {} modified", user.getUsername());
        } catch (Exception e) {
            log.error("Error modifying user {}", user.getUsername(), e);
            throw e;
        }
    }

    // request blogger access
    public void requestBloggerAccess(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User with id " + userId + " not found"));

        String message = String.format("User %d requested blogger access! Username %s, email %s",
                userId, user.getUsername(), user.getEmail());

        asyncEmailService.sendEmail(accountRequestEmail,
                "Blogger access request for user " + userId,
                message);
    }

}
