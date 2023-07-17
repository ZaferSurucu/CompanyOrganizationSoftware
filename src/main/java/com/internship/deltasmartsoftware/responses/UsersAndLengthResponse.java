package com.internship.deltasmartsoftware.responses;

import com.internship.deltasmartsoftware.model.User;
import lombok.Data;

import java.util.List;

@Data
public class UsersAndLengthResponse {
    private long length;
    private List<User> users;
}
