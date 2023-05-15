package com.internship.deltasmartsoftware.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="department")
@Data
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "department_type_id")
    private DepartmentType departmentType;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "town_id")
    private Town town;

    private String addressDetail;

    private boolean active;

    private String createdAt;

    private String deletedAt;

}
