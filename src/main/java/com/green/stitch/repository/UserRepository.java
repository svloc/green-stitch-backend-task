package com.green.stitch.repository;
import com.green.stitch.model.Login;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Login,Integer> {
    Login findByUsername(String username);
}