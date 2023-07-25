package com.internship.deltasmartsoftware.payload.requests;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class ApiErrorRequest {
    private String message;
    private HttpStatus status;
}
