// FILE: src/main/java/com/brkrb/ctrl/AtCtrl.java
package com.brkrb.ctrl;

import com.brkrb.mdl.Usr;
import com.brkrb.rpo.UsrRpo;
import com.brkrb.svc.ExSvc;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/auth")
public class AtCtrl {
    @Autowired private UsrRpo uR;
    @Autowired @Qualifier("pE") private PasswordEncoder pE;
    @Value("${app.jwt.secret}") private String sK;
    @Value("${app.jwt.expiration}") private long eX;
    @Autowired private ExSvc eS;

    @PostMapping("/reg")
    public ResponseEntity<?> r(@RequestBody Map<String, String> p) {
        String username = p.get("un");
        if (uR.findByUn(username).isPresent()) return ResponseEntity.badRequest().body("taken");
        
        Usr newUser = uR.save(new Usr(username, pE.encode(p.get("ph")), "x7k9_p@ss"));
        String tk = Jwts.builder()
            .setSubject(String.valueOf(newUser.id))
            .setExpiration(new Date(System.currentTimeMillis() + eX))
            .signWith(SignatureAlgorithm.HS256, sK)
            .compact();
        
        eS.t(newUser.id, "v1_idor", "auto_fix"); 
        return ResponseEntity.ok(Map.of("t", tk, "uid", newUser.id));
    }

    @PostMapping("/login")
    public ResponseEntity<?> l(@RequestBody Map<String, String> p) {
        Optional<Usr> o = uR.findByUn(p.get("un"));
        if (o.isPresent() && pE.matches(p.get("ph"), o.get().ph)) {
            String tk = Jwts.builder()
                .setSubject(String.valueOf(o.get().id))
                .setExpiration(new Date(System.currentTimeMillis() + eX))
                .signWith(SignatureAlgorithm.HS256, sK)
                .compact();
            return ResponseEntity.ok(Map.of("t", tk, "uid", o.get().id));
        }
        return ResponseEntity.status(401).body("bad_creds");
    }

    @GetMapping("/user/{uid}")
    public ResponseEntity<?> getUser(@PathVariable Long uid) {
        Usr user = uR.findById(uid).orElse(null);
        if (user == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(Map.of("id", user.id, "name", user.un));
    }
}