package com.edavalos.Crypto;

import com.edavalos.Crypto.Chains.StringChain;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class Util {

    public enum HashType {
        SHA_256,
        SHA3_256
    }

    public enum Token {
        BORDER("==================================================\n" +
                     "==================================================\n"),
        SERIAL_BORDER("-+-+-+-+-+-+-+-+-\n"),
        BLOCK_ID("BLOCK ID: "),
        TIMESTAMP("TIMESTAMP: "),
        PRIOR_HASH("PRIOR HASH: "),
        BLOCK_HASH("BLOCK HASH: "),
        CONTENTS("CONTENTS:");


        public final String cont;

        Token(String cont) {
            this.cont = cont;
        }
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

    public static boolean save(String fileName, Chain<?> blockChain) {
        FileWriter fileObj;
        try {
            fileObj = new FileWriter(fileName);
            fileObj.write(blockChain.serialize());
            fileObj.close();
            return true;
        }
        catch (IOException e) {
            return false;
        }
    }

//    public static StringChain load(String file) {
//        try {
//            BufferedReader lineReader = new BufferedReader(new FileReader(file));
//            String line = lineReader.readLine();
//
//            StringChain chain = new StringChain()
//            while (line != null) {
//                // do stuff
//
//
//
//                line = lineReader.readLine();
//            }
//        }
//        catch (IOException e) {
//            return null;
//        }
//    }
}
