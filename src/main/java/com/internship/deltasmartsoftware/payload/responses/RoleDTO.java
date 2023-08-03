package com.internship.deltasmartsoftware.payload.responses;

import com.internship.deltasmartsoftware.model.Role;
import lombok.Data;

@Data
public class RoleDTO {
    private int id;
    private String name;

    public RoleDTO(Role role){
        this.id = role.getId();
        this.name = role.getName();
    }
}