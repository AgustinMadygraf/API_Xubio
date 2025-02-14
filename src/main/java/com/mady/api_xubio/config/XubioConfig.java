package com.mady.api_xubio.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Component;

@Component
public class XubioConfig {
    private final Dotenv dotenv = Dotenv.load();
    private final String clientId;
    private final String clientSecret;
    private final String tokenEndpoint;
    private final String baseApiUrl;

    public XubioConfig() {
        this.clientId = dotenv.get("XUBIO_CLIENT_ID");
        this.clientSecret = dotenv.get("XUBIO_CLIENT_SECRET");
        this.tokenEndpoint = dotenv.get("XUBIO_TOKEN_ENDPOINT");
        this.baseApiUrl = dotenv.get("XUBIO_BASE_API_URL", "https://xubio.com/API/1.1/");
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public String getTokenEndpoint() {
        return tokenEndpoint;
    }

    public String getBaseApiUrl() {
        return baseApiUrl;
    }
} 