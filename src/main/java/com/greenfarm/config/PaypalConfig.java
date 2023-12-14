package com.greenfarm.config;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.paypal.base.rest.APIContext;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;

@Configuration
@ConfigurationProperties(prefix = "paypal")
public class PaypalConfig {

	@Value("${paypal.client.app}")
	private String clientId;

	@Value("${paypal.client.secret}")
	private String clientSecret;

	@Value("${paypal.mode}")
	private String mode;

	@SuppressWarnings("deprecation")
	@Bean
	public APIContext apiContext() throws PayPalRESTException {
		Map<String, String> sdkConfig = new HashMap<>();
		sdkConfig.put("mode", mode);

		String accessToken = new OAuthTokenCredential(clientId, clientSecret, sdkConfig).getAccessToken();

		APIContext apiContext = new APIContext(accessToken);
		apiContext.setConfigurationMap(sdkConfig);

		return apiContext;
	}
}
