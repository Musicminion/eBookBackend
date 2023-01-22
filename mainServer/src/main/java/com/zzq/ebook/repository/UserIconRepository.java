package com.zzq.ebook.repository;

import com.zzq.ebook.entity.UserIcon;
import org.springframework.data.mongodb.repository.MongoRepository;




public interface UserIconRepository extends MongoRepository<UserIcon, String> {

    UserIcon findUserIconByUsername(String username);
}
