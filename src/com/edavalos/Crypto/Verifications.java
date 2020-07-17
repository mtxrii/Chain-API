package com.edavalos.Crypto;

import java.security.NoSuchAlgorithmException;

public final class Verifications {
    public static void main(String[] args) throws NoSuchAlgorithmException {

        System.out.println(Utility.bytesToHex(Utility.byteHash("Test")));

    }
}
