package com.letscook.userdetails.controller;

import com.letscook.userdetails.model.UserDetails;
import com.letscook.userdetails.model.UserInput;
import com.letscook.userdetails.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserDetailsController {
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    @PostMapping("/createuser")
    public ResponseEntity<UserDetails> createUser(@RequestBody UserDetails userDetails) throws Exception {
        List<UserDetails> userDetailList = userDetailsRepository.findByEmail(userDetails.getEmail());
        if(!userDetailList.isEmpty()){
            throw new Exception("user already exists");
        }
        else {
            UserDetails createdUser = userDetailsRepository.save(userDetails);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        }

    }

    @PostMapping("/getuser")
    public ResponseEntity<UserDetails> getUser(@RequestBody UserInput userInput) throws Exception {
        System.out.println("From getUser");
        List<UserDetails> userDetailList =
                userDetailsRepository.findByEmailAndPassword(userInput.getEmail(),userInput.getPassword());
        if(!userDetailList.isEmpty()){
            return ResponseEntity.status(HttpStatus.CREATED).body(userDetailList.get(0));
        }
        else {
            throw new Exception("user doesn't exists");
        }

    }


}
