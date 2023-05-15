package com.internship.deltasmartsoftware.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="company_type")
@Data
public class CompanyType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private boolean active;

    private String createdAt;

    private String deletedAt;
}