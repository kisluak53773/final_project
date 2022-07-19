package com.example.webproject.service.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class PasswordCryptor {
    private static final PasswordCryptor instance = new PasswordCryptor();
    private static final int DEFAULT_CRYPT_COST = 10;

    private PasswordCryptor() {
    }

    public static PasswordCryptor getInstance() {
        return instance;
    }

    public String encrypt(char[] password) {
        return BCrypt.withDefaults().hashToString(DEFAULT_CRYPT_COST, password);
    }

    public boolean verify(char[] password, char[] hash) {
        BCrypt.Result result = BCrypt.verifyer().verify(password, hash);
        return result.verified;
    }
}
