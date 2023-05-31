package com.example.springbootrevision.customer.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CustomerUpdateRequest(
    @NotNull @NotBlank String name,
    @NotNull @NotBlank @Email String email,
    @NotNull @Positive Integer age
) {
}
