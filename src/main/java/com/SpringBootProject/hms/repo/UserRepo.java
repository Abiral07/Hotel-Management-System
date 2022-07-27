package com.SpringBootProject.hms.repo;

import com.SpringBootProject.hms.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<Users, Long> {
//    @Query("select u from Users u where u.fullName= :name")
List<Users> findByFullNameContaining(String name);


    Users findByUserName(String userName);

}
