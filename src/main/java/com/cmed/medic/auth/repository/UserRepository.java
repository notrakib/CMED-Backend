package com.cmed.medic.auth.repository;

import com.cmed.medic.auth.dto.UserDTO;
import com.cmed.medic.auth.dto.UserDTOwithPass;

import java.util.Optional;

public interface UserRepository {
    boolean existsByUsername(String username);
    void saveUser(String username, String passwordHash);
    Optional<UserDTO> findByUsername(String username);
    Optional<UserDTO> findById(Long id);
    Optional<UserDTOwithPass> findByUsernameWithPass(String username);
}
