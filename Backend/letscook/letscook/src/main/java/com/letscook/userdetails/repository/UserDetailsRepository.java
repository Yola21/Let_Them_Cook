package com.letscook.userdetails.repository;

import com.letscook.userdetails.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDetailsRepository extends JpaRepository<UserDetails, Long> {

    List<UserDetails> findByEmail(String email);
    List<UserDetails> findByEmailAndPassword(String email,String password);
}
