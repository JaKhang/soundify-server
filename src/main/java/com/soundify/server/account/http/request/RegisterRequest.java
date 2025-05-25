package com.soundify.server.account.http.request;

import com.soundify.server.account.domain.models.Gender;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record RegisterRequest(
        @NotBlank(message = "Email cannot be null or empty")
        @Email(message = "Invalid email format")
        String email,

        @NotBlank(message = "Display name cannot be null or empty")
        @Size(max = 50, min = 8, message = "Display name cannot exceed 50 characters")
        String displayName,

        @NotNull(message = "Date of birth cannot be null")
        @Past(message = "Date of birth must be in the past")
        LocalDate dateOfBirth,

        @NotNull(message = "Gender cannot be null")
        Gender gender,

        @NotBlank(message = "Password cannot be null or empty")
        @Size(min = 8, max = 100, message = "Password must be at least 8 characters long")
        String password
) {
}
