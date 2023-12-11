package com.greenfarm.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.api.client.util.Value;
import com.greenfarm.dao.RefreshTokenDAO;
import com.greenfarm.dao.UserDAO;
import com.greenfarm.entity.RefreshToken;
import com.greenfarm.exception.TokenRefreshException;

import jakarta.transaction.Transactional;

@Service
public class RefreshTokenService {
    @Value("${jwt.app.jwtRefreshExpirationMs}")
    private Long refreshTokenDurationMs;

    @Autowired
    private RefreshTokenDAO refreshTokenDAO;

    @Autowired
    private UserDAO userDAO;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenDAO.findByToken(token);
    }

    public RefreshToken createRefreshToken(Integer userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userDAO.findUserById(userId));
        refreshToken.setExpiryDate(Instant.now(null).plusMillis(refreshTokenDurationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenDAO.save(refreshToken);
        return refreshToken;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenDAO.delete(token);
            throw new TokenRefreshException(token.getToken(), "Đã hết hạn quyền truy cập. Vui lòng đăng nhập lại");
        }

        return token;
    }

    @Transactional
    public int deleteByUserId(Integer userId) {
        return refreshTokenDAO.deleteByUser(userDAO.findUserById(userId));
    }

}