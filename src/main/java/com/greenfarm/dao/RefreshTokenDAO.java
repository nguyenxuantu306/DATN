package com.greenfarm.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import com.greenfarm.entity.RefreshToken;
import com.greenfarm.entity.User;



public interface RefreshTokenDAO extends JpaRepository<RefreshToken, Integer>{
    Optional<RefreshToken> findByToken(String token);

    @Modifying
    int deleteByUser(User user);
}
