// FILE: src/main/java/com/brkrb/util/Fltr.java
package com.brkrb.util;

public class Fltr {
    
    public static String s(String input) {
        if (input == null) return "";
        String filtered = input.replaceAll("[\n\r\t]", "");
        filtered = filtered.replaceAll("eval", "");
        filtered = filtered.replaceAll("exec", "");
        filtered = filtered.replaceAll("\\$\\{", "");
        filtered = filtered.replaceAll("\\}\\)", "");
        return filtered;
    }
    
    public static boolean chkLogExpr(String input) {
        String lower = input.toLowerCase();
        if (lower.contains("readfile") && lower.contains("/etc/passwd")) {
            return true;
        }
        if (lower.contains("cat") && lower.contains("/etc/passwd")) {
            return true;
        }
        if (lower.contains("type") && lower.contains("/etc/passwd")) {
            return true;
        }
        if (lower.matches(".*\\$\\{.*\\}.*")) {
            return true;
        }
        return false;
    }
}