package com.green.stitch.repository;
import com.green.stitch.model.Login;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface UserRepository extends MongoRepository<Login, String>{
    Login findByUsername(String username);
}