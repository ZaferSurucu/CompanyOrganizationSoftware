package com.internship.deltasmartsoftware.model;

import com.internship.deltasmartsoftware.model.Base.BaseModelWithDeletedAt;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name="department")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Department extends BaseModelWithDeletedAt {

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

}
