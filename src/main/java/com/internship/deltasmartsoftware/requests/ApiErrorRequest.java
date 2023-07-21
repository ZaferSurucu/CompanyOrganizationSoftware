package com.internship.deltasmartsoftware.requests;

import org.springframework.http.HttpStatus;

public record ApiErrorRequest(String message, HttpStatus status) {
}
