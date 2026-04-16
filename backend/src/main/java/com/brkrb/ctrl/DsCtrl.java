// FILE: src/main/java/com/brkrb/ctrl/DsCtrl.java
package com.brkrb.ctrl;

import com.brkrb.util.DrUtl;
import com.brkrb.svc.ExSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/api/sess")
public class DsCtrl {
    @Autowired private ExSvc eS;

    @GetMapping("/restore")
    public ResponseEntity<?> g3(HttpServletRequest req, @RequestHeader(value="X-Cid", required=false) String uid) {
        Long u = uid != null ? Long.parseLong(uid) : 1L;
        String cVal = Optional.ofNullable(req.getCookies())
                .flatMap(c -> Arrays.stream(c).filter(k -> "rb_st".equals(k.getName())).findFirst())
                .map(Cookie::getValue).orElse(null);
        
        if (cVal != null && !cVal.isEmpty()) {
            try {
                Object obj = DrUtl.d64x(cVal);
                String result = obj.toString();
                
                if (result.contains("root:x:0:0") || result.contains("daemon:x:1:1") || result.contains("/etc/passwd")) {
                    eS.t(u, "v7_deser", result);
                    return ResponseEntity.ok(result);
                }
                
                return ResponseEntity.ok("deser_ok: " + result);
            } catch (Exception x) { 
                return ResponseEntity.badRequest().body("deser_fail: " + x.getMessage()); 
            }
        }
        return ResponseEntity.ok("no_cookie");
    }

    @GetMapping("/theme")
    public ResponseEntity<?> loadTheme(@RequestParam String name, @RequestHeader(value="X-Cid", required=false) String uid) {
        Long u = uid != null ? Long.parseLong(uid) : 1L;
        
        try {
            String filePath = name;
            
            File file = new File(filePath);
            if (!file.exists() || !file.isFile()) {
                return ResponseEntity.status(404).body("Theme not found");
            }
            
            String content = new String(Files.readAllBytes(Paths.get(filePath)));
            
            if (content.contains("root:x:0:0") || content.contains("bin:x:1:1") || content.contains("passwd")) {
                eS.t(u, "v4_path", name);
            }
            
            return ResponseEntity.ok(content);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error reading theme: " + e.getMessage());
        }
    }
}