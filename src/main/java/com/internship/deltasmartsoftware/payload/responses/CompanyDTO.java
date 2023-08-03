package com.internship.deltasmartsoftware.payload.responses;

import com.internship.deltasmartsoftware.model.Company;
import lombok.Data;

@Data
public class CompanyDTO {
    private int id;
    private String name;

    public CompanyDTO(Company company){
        this.id = company.getId();
        this.name = company.getName();
    }
}
