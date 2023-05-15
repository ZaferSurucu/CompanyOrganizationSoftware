package com.internship.deltasmartsoftware.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="city")
@Data
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String createdAt;
}