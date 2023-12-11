package com.greenfarm.service.impl;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.keygen.BytesKeyGenerator;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;

import com.greenfarm.entity.Securetoken;
import com.greenfarm.service.Securetokenservice;

@Service
public class Securetokenserviceimpl implements Securetokenservice {

	private static final BytesKeyGenerator DEFAULT_TOKEN_GENERATOR = KeyGenerators.secureRandom(15);
	private static final Charset US_ASCII = Charset.forName("US-ASCII");

//    @Value("${jdj.secure.token.validity}")
//    private int tokenValidityInSeconds;

	@Autowired
	com.greenfarm.dao.Securetokendao Securetokendao;

	@Override
	public Securetoken createSecureToken() {
		// String tokenValue = new
		// String(org.springframework.security.crypto.codec.Base64.encodeBase64URLSafe(DEFAULT_TOKEN_GENERATOR.generateKey()),
		// US_ASCII); // this is a sample, you can adapt as per your security need

		String tokenValue = new String(
				Base64.getUrlEncoder().withoutPadding().encode(DEFAULT_TOKEN_GENERATOR.generateKey()),
				StandardCharsets.US_ASCII);
		Securetoken secureToken = new Securetoken();
		secureToken.setToken(tokenValue);
		secureToken.setExpireAt(LocalDateTime.now().plusSeconds(43200));

		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());

		secureToken.setTimeStamp(currentTimestamp);
		// secureToken.setTimeStamp(System.currentTimeMillis());
		this.saveSecureToken(secureToken);
		return secureToken;
	}

	@Override
	public void saveSecureToken(Securetoken token) {
		Securetokendao.save(token);
	}

	@Override
	public Securetoken findByToken(String token) {
		return Securetokendao.findByToken(token);
	}

	@Override
	public void removeToken(Securetoken token) {
		Securetokendao.delete(token);
	}

	@Override
	public void removeTokenByToken(String token) {
		Securetokendao.removeByToken(token);
	}

//	public int getTokenValidityInSeconds() {
//        return tokenValidityInSeconds;
//    }

}
