package com.internship.deltasmartsoftware.model;

import com.internship.deltasmartsoftware.model.Base.BaseModelWithDeletedAt;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name="company")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Company extends BaseModelWithDeletedAt {

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
}
