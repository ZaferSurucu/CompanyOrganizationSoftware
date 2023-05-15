package com.internship.deltasmartsoftware.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="department_hierarchy")
@Data
public class DepartmentHierarchy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "child_department_id")
    private Department childDepartment;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "parent_department_id")
    private Department parentDepartment;
}