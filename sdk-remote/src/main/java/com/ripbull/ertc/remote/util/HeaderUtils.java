package com.ripbull.ertc.remote.util;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HeaderUtils {

    public static final String SEPARATOR = "~";

    public static String getHeaderSignature(String apiKey, String packageName, long time) {
        String rawHeader = apiKey + SEPARATOR + packageName + SEPARATOR + time;
        return getHash(rawHeader);
    }

    public static String getHeaderTimestamp(long time) {
        String rawHeader = time + "";
        return rawHeader;
    }

    private static String getHash(String data){
        try {
            MessageDigest digest = null;
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            }
            digest.reset();
            return bin2hex(digest.digest(data.getBytes()));
        } catch (Exception ignored) {
            return null;
        }
    }

    private static String bin2hex(byte[] data) {
        StringBuilder hex = new StringBuilder(data.length * 2);
        for (byte b : data)
            hex.append(String.format("%02x", b & 0xFF));
        return hex.toString();
    }
}
