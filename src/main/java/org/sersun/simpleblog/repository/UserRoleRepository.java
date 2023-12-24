package org.sersun.simpleblog.repository;

import org.sersun.simpleblog.model.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoles, Long> {

    UserRoles findByUserId(Long userId);
}
