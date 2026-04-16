// FILE: src/main/java/com/brkrb/ctrl/InternalCtrl.java
package com.brkrb.ctrl;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/internal")
public class InternalCtrl {
    
    @GetMapping("/secret")
    public ResponseEntity<?> getSecret(HttpServletRequest request) {
        String remoteAddr = request.getRemoteAddr();
        
        return ResponseEntity.ok(Map.of(
            "flag", "CTF{SSRF_1s_h4rd_but_y0u_d1d_1t}",
            "secret_key", "x7k9_p@ss_internal_123",
            "message", "Congratulations! You exploited SSRF!"
        ));
    }
}