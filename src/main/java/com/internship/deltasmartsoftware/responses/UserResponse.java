package com.internship.deltasmartsoftware.responses;

import com.internship.deltasmartsoftware.model.User;
import lombok.Data;

@Data
public class UserResponse {
    String name;
    String surname;
    String email;
    String roleName;
    String departmentName;
    String companyName;
    int roleId;
    int departmentId;
    int companyId;

    // create userResponse with User
    public UserResponse(User user){
        this.name = user.getName();
        this.surname = user.getSurname();
        this.email = user.getEmail();
        this.roleName = user.getRole().getName();
        this.departmentName = user.getDepartment().getName();
        this.roleId = user.getRole().getId();
        this.departmentId = user.getDepartment().getId();
    }
}

