package com.brkrb.util;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HshUtl {
    public static String h(String in) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(in.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hash) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (Exception e) { return null; }
    }

    public static String genRabbitCfg(String un, String salt) {
        long seed = (un.hashCode() ^ salt.hashCode()) & 0xFFFFFFFFL;
        int dir = (seed % 2 == 0) ? 1 : -1;
        String col = String.format("#%06X", (int)(seed % 16777215));
        double ang = (seed % 360) / 1.0;
        return String.format("{\"d\":%d,\"c\":\"%s\",\"a\":%.2f}", dir, col, ang);
    }
}