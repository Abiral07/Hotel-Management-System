package com.SpringBootProject.hms.repo;

import com.SpringBootProject.hms.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByRoleName(String roleName);
    Role findDistinctByRoleId(Long id);

    @Query(value = "SELECT r.role_name FROM hms.role as r join hms.users_role as ur on r.role_id = ur.role_id where uid = :userId",
            nativeQuery = true)
    Set<String> findRoleOfUser(Long userId);
}
