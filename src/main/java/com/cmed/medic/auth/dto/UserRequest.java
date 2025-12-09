package com.cmed.medic.auth.dto;

public record UserRequest(
    String username,
    String password
) {}
