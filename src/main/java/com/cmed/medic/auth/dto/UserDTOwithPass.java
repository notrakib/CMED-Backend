package com.cmed.medic.auth.dto;

public record UserDTOwithPass(
        Long id,
        String username,
        String passwordHash,
        String role) {
}
