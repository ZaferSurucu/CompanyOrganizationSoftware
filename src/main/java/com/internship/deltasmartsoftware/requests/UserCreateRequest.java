package com.internship.deltasmartsoftware.requests;

import lombok.Data;

@Data
public class UserCreateRequest {
    String name;
    String surname;
    String email;
    int roleId;
    int departmentId;
    int companyId;
}
