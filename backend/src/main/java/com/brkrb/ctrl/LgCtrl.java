// FILE: src/main/java/com/brkrb/ctrl/LgCtrl.java
package com.brkrb.ctrl;

import com.brkrb.svc.ExSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@RequestMapping("/api/log")
public class LgCtrl {
    private static final Logger L = LogManager.getLogger(LgCtrl.class);
    @Autowired private ExSvc eS;

    @PostMapping("/v")
    public ResponseEntity<?> p4(@RequestBody String msg, @RequestHeader(value="X-Cid", required=false) String uid) {
        Long u = uid != null ? Long.parseLong(uid) : 1L;
        
        L.info("User message: " + msg);
        
        if (msg.contains("${jndi:") || msg.contains("JNDI") || msg.contains("exploit")) {
            try {
                String result = executeJndiLookup(msg);
                if (result.contains("root:x:0:0") || result.contains("/etc/passwd")) {
                    eS.t(u, "v8_log", result);
                    return ResponseEntity.ok("Exploit successful: " + result);
                }
            } catch (Exception e) {
                return ResponseEntity.ok("Logged but no exploit");
            }
        }
        
        return ResponseEntity.ok("Message logged");
    }
    
    private String executeJndiLookup(String payload) {
        try {
            if (payload.contains("ldap://") && payload.contains("read_passwd")) {
                return new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("/etc/passwd")));
            }
            if (payload.contains("${env:")) {
                String var = payload.substring(payload.indexOf("${env:") + 6, payload.indexOf("}"));
                return System.getenv(var);
            }
            if (payload.contains("${sys:")) {
                String var = payload.substring(payload.indexOf("${sys:") + 6, payload.indexOf("}"));
                return System.getProperty(var);
            }
            return "No exploit executed";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
}