package com.example.springbootrevision.exception;

import java.time.LocalDateTime;

public record ApiError(
    Object message,
    String path,
    int status,
    LocalDateTime timestamp
) {
}
