package com.letscook.userdetails.controller;

import com.letscook.userdetails.model.UserInfo;
import com.letscook.userdetails.model.UserInput;
import com.letscook.userdetails.repository.UserDetailsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserDetailsController {
    @Autowired
    private UserDetailsRepository userDetailsRepository;

    private JwtEncoder jwtEncoder;

    public UserDetailsController(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }
    @PostMapping("/createuser")
    public ResponseEntity<JwtResponse> createUser(@RequestBody UserInfo userDetails) throws Exception {
        List<UserInfo> userDetailList = userDetailsRepository.findByEmail(userDetails.getEmail());
        if (!userDetailList.isEmpty()) {
            throw new Exception("user already exists");
        } else {
            userDetails.setPassword(passwordEncoder().encode(userDetails.getPassword()));
            UserInfo createdUser = userDetailsRepository.save(userDetails);
            String token = createToken(createdUser);
            JwtResponse jwtResponse = new JwtResponse(createdUser,token);
            return ResponseEntity.status(HttpStatus.CREATED).body(jwtResponse);
        }

    }

    @PostMapping("/getuser")
    public ResponseEntity<UserInfo> getUser(@RequestBody UserInput userInput) throws Exception {
        System.out.println("From getUser");
//        List<UserDetails> userDetailList =
//                userDetailsRepository.findByEmailAndPassword(userInput.getEmail(),userInput.getPassword());
        List<UserInfo> userDetailList = userDetailsRepository.findByEmail(userInput.getEmail());
        if (!userDetailList.isEmpty()) {
            if (passwordEncoder().matches(userInput.getPassword(), userDetailList.get(0).getPassword())) {
                return ResponseEntity.status(HttpStatus.CREATED).body(userDetailList.get(0));
            } else {
                throw new Exception("credentials doesn't match");
            }
        } else {
            throw new Exception("user doesn't exists");
        }

    }

    private String createToken(UserInfo userInfo) {
        var claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(60 * 30))
                .subject(userInfo.getName())
                .claim("scope", createScope(userInfo))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }

    private String createScope(UserInfo userInfo) {
        return userInfo.getAuthorities().stream()
                .map(a -> a.getAuthority())
                .collect(Collectors.joining(" "));
    }

    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}

record JwtResponse(UserInfo userInfo,String token) {}
