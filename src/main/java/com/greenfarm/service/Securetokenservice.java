package com.greenfarm.service;

import com.greenfarm.entity.Securetoken;

public interface Securetokenservice {
	Securetoken createSecureToken();
    void saveSecureToken(final Securetoken token);
    Securetoken findByToken(final String token);
    void removeToken(final Securetoken token);
    void removeTokenByToken(final String token);
}
