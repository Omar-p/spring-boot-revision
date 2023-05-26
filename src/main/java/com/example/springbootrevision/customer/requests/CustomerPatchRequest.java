package com.example.springbootrevision.customer.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;

public record CustomerPatchRequest(
    String name,
    @Email String email,
    @Positive Integer age
) {
}
