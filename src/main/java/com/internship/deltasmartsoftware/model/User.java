package com.internship.deltasmartsoftware.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="user")
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

    public User(){
        this.id = 0;
        this.name = "name";
        this.surname = "surname";
        this.email = "email";
        this.password = "password";
        this.enabled = true;
        this.active = true;
    }

    public boolean getEnabled() {
        return this.enabled;
    }
}
