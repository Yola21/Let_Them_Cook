package com.letscook.cook.model;

import javax.persistence.*;

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

    @Column(name = "business_name", nullable = false)
    private String businessName;

    @Column(name = "phoneNumber", nullable = false)
    private String phoneNumber;

    @Column(name = "status", nullable = false)
    private String status;
}
