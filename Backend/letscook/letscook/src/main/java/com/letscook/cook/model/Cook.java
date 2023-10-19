package com.letscook.cook.model;

import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "cooks")
public class Cook {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "business_name", nullable = true)
    private String businessName;

    @Column(name = "phone_number", nullable = true)
    private String phoneNumber;

    @Column(name = "status", nullable = true)
    private String status;

}
