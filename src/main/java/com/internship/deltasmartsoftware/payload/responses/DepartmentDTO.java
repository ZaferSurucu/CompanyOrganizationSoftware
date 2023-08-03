package com.internship.deltasmartsoftware.payload.responses;

import com.internship.deltasmartsoftware.model.Department;
import lombok.Data;

@Data
public class DepartmentDTO {
    private int id;
    private String name;

    public DepartmentDTO(Department department){
        this.id = department.getId();
        this.name = department.getName();
    }
}
