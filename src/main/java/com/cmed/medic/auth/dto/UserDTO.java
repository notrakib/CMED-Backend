package com.cmed.medic.auth.dto;

public record UserDTO(
        Long id,
        String username,
        String role) {
}
