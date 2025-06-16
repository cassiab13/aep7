package com.plast.cash.password;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class Password {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public static String encode(final String password) {
        return encoder.encode(password);
    }

    public static boolean matches(final String rawPassword, final String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

}