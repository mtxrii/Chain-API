package com.edavalos.Crypto;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Util {

    public enum HashType {
        SHA_256,
        SHA3_256
    }

    public static byte[] byteHash(String str, HashType type) {
        String hash = switch (type) {
            case SHA_256  -> "SHA-256";
            case SHA3_256 -> "SHA3-256";
        };
        MessageDigest digest;

        try {
            digest = MessageDigest.getInstance(hash);
        }
        catch (NoSuchAlgorithmException e) {
            return null;
        }

        return digest.digest(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static boolean save(String file, Chain<?> blockChain) {
        File myObj;
        try {
            myObj = new File("filename.txt");
            if (myObj.createNewFile()) {
                System.out.println("File created: " + myObj.getName() + "...");
            } else {
                System.out.println("File already exists. Writing on it...");
            }
        }
        catch (IOException e) {
            System.out.println("An error occurred opening " + file);
            e.printStackTrace();
        }
    }

    public static boolean load(String file) {

    }
}
