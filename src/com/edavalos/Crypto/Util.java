package com.edavalos.Crypto;

import com.edavalos.Crypto.Chains.StringChain;
import com.edavalos.Crypto.Components.Block;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static StringChain load(String file) {
        List<Block<String>> blocks = new ArrayList<>();
        Block<String> current = null;

        Util.HashType hashType = HashType.SHA_256;
        String genesisHash = "";
        boolean onFirst = true;

        String id = "";
        String ts = "";
        String ph = "";
        String bh = "";

        try {
            BufferedReader lineReader = new BufferedReader(new FileReader(file));
            String line = lineReader.readLine();

            while (line != null) {
                if (line.strip().equals("")) {
                    line = lineReader.readLine();
                    continue;
                }

                String[] parts = line.strip().split(" ");
                String tag = parts[0];
                switch (tag) {
                    case "HT" -> {
                        if (parts.length != 3) return null;
                        else if (parts[1].equals("SHA3_256ID")) hashType = HashType.SHA3_256;
                        else if (parts[1].equals("SHA_256ID"))  hashType = HashType.SHA_256;

                        if (!isNumeric(parts[2])) return null;
                        id = parts[2];
                    }
                    case "ID" -> {
                        if (parts.length != 2) return null;
                        if (!isNumeric(parts[1])) return null;
                        id = parts[1];
                    }
                    case "TS" -> {
                        if (parts.length != 7) return null;
                        ts = line.strip().replace(tag + " ", "");
                    }
                    case "PH" -> {
                        if (parts.length != 2) return null;
                        ph = parts[1];

                        if (onFirst) {
                            genesisHash = parts[1];
                            onFirst = false;
                        }
                    }
                    case "BH" -> {
                        if (parts.length < 2) return null;
                        bh = line.strip().replace(tag + " ", "");
                    }
                    case "CONTENTS:" -> {
                        if (parts.length != 1) return null;
                        if (current != null) return null;
                        if (bh == null) return null;
                        current = new Block<>(id, ts, ph, bh);

                    }
                    case "-" -> {
                        Matcher matcher = Pattern.compile("\"(.*?)\"").matcher(line);
                        if (!matcher.find()) return null;
                        if (current == null) return null;
                        current.addItem(matcher.group(1));
                    }
                    case "-+-+-+-+-+-+-+-+-" -> {
                        if (current == null) return null;
                        blocks.add(current);
                        current = null;
                        id = null;
                        ts = null;
                        ph = null;
                        bh = null;
                    }
                }

                line = lineReader.readLine();
            }
        }
        catch (IOException e) {
            return null;
        }

        return new StringChain(genesisHash, hashType, blocks);
    }

    private static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch(NumberFormatException e){
            return false;
        }
    }
}
