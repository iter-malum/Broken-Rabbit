// FILE: src/main/java/com/brkrb/ctrl/JsBk.java
package com.brkrb.ctrl;

import com.brkrb.svc.ExSvc;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Base64;

@RestController
@RequestMapping("/api/state")
public class JsBk {
    @Autowired private ExSvc eS;
    
    private Map<String, Object> config = new HashMap<>();
    
    public JsBk() {
        config.put("theme", "default");
        config.put("language", "en");
        config.put("debug", false);
        config.put("admin", false);
    }

    @PostMapping("/sync")
    public ResponseEntity<?> s5(@RequestBody JsonNode n, @RequestHeader(value="X-Cid", required=false) String uid) {
        Long u = uid != null ? Long.parseLong(uid) : 1L;
        String raw = n.toString();
        
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> updates = mapper.convertValue(n, Map.class);
            
            mergeConfig(updates);
            
            if (raw.contains("__proto__") || raw.contains("constructor") || raw.contains("prototype")) {
                if (config.containsKey("polluted") && config.get("polluted").equals(true)) {
                    eS.t(u, "v10_back", raw);
                    return ResponseEntity.ok(Map.of(
                        "polluted", true,
                        "flag", "CTF{Prototype_Pollution_Master}",
                        "message", "You successfully polluted the prototype!",
                        "config", config
                    ));
                }
                
                eS.t(u, "v10_back", raw);
                return ResponseEntity.ok(Map.of("polluted", true, "status", "detected"));
            }
            
            return ResponseEntity.ok(Map.of("sync", "ok", "config", config));
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
    
    private void mergeConfig(Map<String, Object> updates) {
        for (Map.Entry<String, Object> entry : updates.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            if (key.equals("__proto__") || key.equals("constructor") || key.equals("prototype")) {
                if (value instanceof Map) {
                    Map<String, Object> proto = (Map<String, Object>) value;
                    if (proto.containsKey("polluted") && proto.get("polluted").equals(true)) {
                        config.put("polluted", true);
                        config.put("injected_command", "read_passwd");
                        try {
                            String passwd = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get("/etc/passwd")));
                            config.put("passwd_content", passwd);
                        } catch (Exception e) {
                            config.put("passwd_content", "Error reading file");
                        }
                    }
                    for (Map.Entry<String, Object> pEntry : proto.entrySet()) {
                        config.put(pEntry.getKey(), pEntry.getValue());
                    }
                }
            } else {
                config.put(key, value);
            }
        }
    }
}