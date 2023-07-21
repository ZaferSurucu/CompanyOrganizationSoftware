package com.internship.deltasmartsoftware.responses;

import org.springframework.http.HttpStatus;

public record ApiResponse(String message, HttpStatus status) {
}
