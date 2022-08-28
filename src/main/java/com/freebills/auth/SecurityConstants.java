package com.freebills.auth;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityConstants {

    public static final String SECRET = "temp_secret";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String LOGIN = "/login";
    public static final Long EXPIRATION_TIME = 3600000L;

}
