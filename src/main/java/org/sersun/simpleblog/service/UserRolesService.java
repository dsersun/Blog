package org.sersun.simpleblog.service;

import lombok.extern.slf4j.Slf4j;
import org.sersun.simpleblog.model.RoleID;
import org.sersun.simpleblog.model.UserRoles;
import org.sersun.simpleblog.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@Slf4j
public class UserRolesService {

    @Autowired
    UserRoleRepository userRoleRepository;

    public void addReaderRole(Long userId){
        userRoleRepository.save(new UserRoles(null, userId, RoleID.READER.getId()));
    }

    public void assignRole(Long userId, RoleID roleID) {
        UserRoles userRoles = userRoleRepository.findByUserId(userId);
        if (userRoles == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        } else {
            userRoles.setRoleId(roleID.getId());
            userRoleRepository.save(userRoles);
        }
    }



}
