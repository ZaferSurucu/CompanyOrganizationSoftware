package com.internship.deltasmartsoftware.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name="role")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String createdAt;

    public Role(String name){
        this.name = name;
        this.createdAt = new Date().toString();
    }

    public Role(){
        this.id = 0;
        this.name = "name";
        this.createdAt = new Date().toString();
    }
}
