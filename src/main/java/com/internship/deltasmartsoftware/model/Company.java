package com.internship.deltasmartsoftware.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="company")
@Data
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String shortName;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_type_id")
    private CompanyType companyType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "town_id")
    private Town town;

    private String addressDetail;

    private boolean active;

    private String createdAt;

    private String deletedAt;
}
