package com.cmed.medic.auth.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.cmed.medic.auth.dto.UserDTO;
import com.cmed.medic.auth.dto.UserDTOwithPass;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public boolean existsByUsername(String username) {
        Integer count = jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM users WHERE username = ?", Integer.class, username);
        return count != null && count > 0;
    }

    @Override
    public void saveUser(String username, String passwordHash) {
        jdbcTemplate.update(
                "INSERT INTO users (username, password_hash) VALUES (?, ?)",
                username, passwordHash);
    }

    @Override
    public Optional<UserDTO> findByUsername(String username) {
        List<UserDTO> users = jdbcTemplate.query(
                "SELECT id, username, role FROM users WHERE username = ?",
                (rs, rowNum) -> new UserDTO(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("role")),
                username);
        if (users.isEmpty())
            return Optional.empty();
        return Optional.of(users.get(0));
    }

    @Override
    public Optional<UserDTO> findById(Long id) {
        List<UserDTO> users = jdbcTemplate.query(
                "SELECT id, username, role FROM users WHERE id = ?",
                (rs, rowNum) -> new UserDTO(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("role")),
                id);
        if (users.isEmpty())
            return Optional.empty();
        return Optional.of(users.get(0));
    }

    @Override
    public Optional<UserDTOwithPass> findByUsernameWithPass(String username) {
        List<UserDTOwithPass> users = jdbcTemplate.query(
                "SELECT id, username, role, password_hash FROM users WHERE username = ?",
                (rs, rowNum) -> new UserDTOwithPass(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password_hash"),
                        rs.getString("role")),
                username);
        if (users.isEmpty())
            return Optional.empty();
        return Optional.of(users.get(0));
    }
}
