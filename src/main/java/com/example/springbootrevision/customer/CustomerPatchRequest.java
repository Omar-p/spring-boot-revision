package com.example.springbootrevision.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CustomerPatchRequest(
    String name,
    @Email String email,
    @Positive Integer age
) {
}
