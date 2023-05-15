package com.internship.deltasmartsoftware.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="user")
@Data
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id")
    private Role role;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_id")
    private Department department;

    private String name;

    private String surname;

    private String email;

    private String password;

    private boolean enabled;

    private boolean active;

    private String createdAt;

    private String deletedAt;

}
