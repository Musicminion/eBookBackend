package com.zzq.ebook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.zzq.ebook.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User,String> {

    @Query(value = "from User where username = :username and password = :password")
    User checkUser(@Param("username") String username, @Param("password") String password);


    @Query(value = "from User where username = :username")
    User getOne(String username);
}
