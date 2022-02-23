package com.epam.project.spring.taxispring.utils;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.validator.routines.EmailValidator;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;


/**
 * Helper class for hashing password.
 * Also provides basic validation.
 */
public class Security {

    private static final int ITERATIONS = 200000;
    private static final int KEY_LENGTH = 512; // in bits
    private static final int SALT_LENGTH = 128; // in bits

    public static void main(String[] args) throws Exception {

        System.out.println(hashPassword("test2password"));
    }

    /**
     * Hashing password using Password-Based Key Derivation Function (PBKDF)
     *
     * @param password Password string to hash
     * @return Hexed password hash following by hexed salt
     * @throws Exception when password can`t be hashed by algorithm
     */
    public static String hashPassword(final String password) throws Exception {
        byte[] salt = new byte[SALT_LENGTH / 8];
        (new SecureRandom()).nextBytes(salt);
        byte[] hashedBytes = hashPassword(password.toCharArray(), salt);
        return Hex.encodeHexString(hashedBytes) + Hex.encodeHexString(salt);
    }

    /**
     * Hashing password using Password-Based Key Derivation Function (PBKDF)
     *
     * @return Hexed password as chars array
     * @throws Exception when password cant be hashed by algorithm
     */
    private static byte[] hashPassword(final char[] password, final byte[] salt) throws Exception {
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        SecretKey key = skf.generateSecret(spec);
        return key.getEncoded();
    }

    /**
     * Checks if given password being hashed equals to given hexed password string
     */
    public static boolean isPasswordCorrect(final String password, final String passwordSaltHexed) throws Exception {
        if (password == null || passwordSaltHexed == null)
            return false;
        String passwordHexed = passwordSaltHexed.substring(0, KEY_LENGTH / 8 * 2);
        byte[] salt = Hex.decodeHex(passwordSaltHexed.substring(KEY_LENGTH / 8 * 2).toCharArray());
        byte[] hashedBytes = hashPassword(password.toCharArray(), salt);
        return Hex.encodeHexString(hashedBytes).equals(passwordHexed);
    }

    /**
     * Email validation
     */
    public static boolean isEmailValid(final String email)  {
        return email != null && EmailValidator.getInstance().isValid(email);
    }

    /**
     * Password validation
     */
    public static boolean isPasswordValid(final String password)  {
        return password != null && password.length() >= 8 && password.length() <= 64;
    }

}
