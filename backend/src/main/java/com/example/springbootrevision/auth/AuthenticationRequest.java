package com.example.springbootrevision.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AuthenticationRequest(@NotBlank @NotNull String email, @NotBlank @NotNull String password) {
}
