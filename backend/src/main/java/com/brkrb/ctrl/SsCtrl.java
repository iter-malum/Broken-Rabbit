// FILE: src/main/java/com/brkrb/ctrl/SsCtrl.java
package com.brkrb.ctrl;

import com.brkrb.svc.ExSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Map;

@RestController
@RequestMapping("/api/av")
public class SsCtrl {
    @Autowired private ExSvc eS;

    private boolean isBlocked(String url) {
        String lowerUrl = url.toLowerCase();
        if (lowerUrl.contains("localhost")) return true;
        if (lowerUrl.contains("127.0.0.1")) return true;
        if (lowerUrl.contains("0.0.0.0")) return true;
        if (lowerUrl.matches(".*\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}.*")) {
            String ip = lowerUrl.replaceAll(".*?(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}).*", "$1");
            if (ip.startsWith("10.") || ip.startsWith("172.") || ip.startsWith("192.168.") || ip.startsWith("169.254.")) {
                return true;
            }
        }
        return false;
    }

    @GetMapping("/fetch")
    public ResponseEntity<?> f2(@RequestParam String url, @RequestHeader(value="X-Cid", required=false) String uid) {
        Long u = uid != null ? Long.parseLong(uid) : 1L;
        try {
            String decodedUrl = URLDecoder.decode(url, "UTF-8");
            
            if (isBlocked(decodedUrl)) {
                return ResponseEntity.status(403).body("Access denied");
            }
            
            URL uUrl = new URL(decodedUrl);
            HttpURLConnection c = (HttpURLConnection) uUrl.openConnection();
            c.setRequestMethod("GET");
            c.setConnectTimeout(5000);
            
            BufferedReader r = new BufferedReader(new InputStreamReader(c.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String ln;
            while ((ln = r.readLine()) != null) sb.append(ln);
            
            String response = sb.toString();
            
            eS.t(u, "v6_ssrf", response);
            
            return ResponseEntity.ok(Map.of("data", response, "status", c.getResponseCode()));
        } catch (Exception x) { 
            return ResponseEntity.badRequest().body("fetch_err: " + x.getMessage()); 
        }
    }
}