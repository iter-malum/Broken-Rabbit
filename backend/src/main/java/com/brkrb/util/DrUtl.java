// FILE: src/main/java/com/brkrb/util/DrUtl.java
package com.brkrb.util;

import java.io.*;
import java.util.Base64;

public class DrUtl {
    
    public static Object d64x(String data) throws Exception {
        byte[] bytes = Base64.getDecoder().decode(data);
        try (ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            return ois.readObject();
        }
    }
}