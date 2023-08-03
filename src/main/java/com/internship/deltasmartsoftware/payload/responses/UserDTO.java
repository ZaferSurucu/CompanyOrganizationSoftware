package com.internship.deltasmartsoftware.payload.responses;

import com.internship.deltasmartsoftware.model.User;
import lombok.Data;

@Data
public class UserDTO {
    private int id;
    private String name;
    private String surname;
    private RoleDTO role;
    private String email;
    private DepartmentDTO department;
    private CompanyDTO company;

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.surname = user.getSurname();
        this.role = new RoleDTO(user.getRole());
        this.email = user.getEmail();
        this.department = new DepartmentDTO(user.getDepartment());
        this.company = new CompanyDTO(user.getDepartment().getCompany());
    }
}
