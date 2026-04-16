// FILE: src/main/java/com/brkrb/ctrl/ProgressCtrl.java
package com.brkrb.ctrl;

import com.brkrb.svc.ExSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/progress")
public class ProgressCtrl {
    @Autowired private ExSvc eS;

    @PostMapping("/check")
    public ResponseEntity<?> check(@RequestParam String type, @RequestHeader(value="X-Cid", required=false) String uid) {
        if ("xss".equals(type)) {
            eS.t(0L, "v3_jwt", "XSS_EXECUTED");
        }
        return ResponseEntity.ok().build();
    }
}