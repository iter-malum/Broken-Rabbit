// FILE: src/main/java/com/brkrb/ctrl/RbCtrl.java
package com.brkrb.ctrl;

import com.brkrb.svc.CrSvc;
import com.brkrb.svc.ExSvc;
import com.brkrb.svc.ScSvc;
import com.brkrb.rpo.RbStRpo;
import com.brkrb.mdl.RbSt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@RestController
@RequestMapping("/api/rabbit")
public class RbCtrl {
    @Autowired private CrSvc cS;
    @Autowired private RbStRpo rR;
    @Autowired private JdbcTemplate j;
    @Autowired private ExSvc eS;
    @Autowired private ScSvc sS;
    @Value("${app.jwt.secret}") private String sK;
    
    private ConcurrentHashMap<Long, AtomicInteger> moodCounter = new ConcurrentHashMap<>();

    @GetMapping("/q")
    public ResponseEntity<?> s(@RequestParam String k, @RequestHeader(value="X-Cid", required=false) String uidHeader) {
        try {
            Long uid = uidHeader != null ? Long.parseLong(uidHeader) : 1L;
            String sql = "SELECT id::text, u_id::text, ear_dir::text, fur_c, h_ang::text, meta_json FROM rb_st WHERE fur_c LIKE '%" + k + "%'";
            List<Map<String, Object>> r = j.queryForList(sql);
            boolean passwordFound = false;
            String extractedPassword = null;
            for (Map<String, Object> row : r) {
                if (row.get("fur_c") != null) {
                    String furc = row.get("fur_c").toString();
                    if (!furc.equals("#8899AA")) {
                        passwordFound = true;
                        extractedPassword = furc;
                        row.put("message", "Password extracted successfully!");
                    }
                }
            }
            if (passwordFound && extractedPassword != null) {
                eS.t(uid, "v2_sqli", extractedPassword);
            }
            return ResponseEntity.ok(r);
        } catch (Exception x) {
            StringWriter sw = new StringWriter();
            x.printStackTrace(new PrintWriter(sw));
            return ResponseEntity.status(500)
                .contentType(MediaType.TEXT_PLAIN)
                .body("SQL Error:\n" + x.getMessage());
        }
    }

    @PostMapping("/mod")
    public ResponseEntity<?> m(@RequestBody Map<String,Object> d, @RequestHeader(value="Authorization", required=false) String auth) {
        Long targetUid = d.containsKey("uid") ? Long.parseLong(d.get("uid").toString()) : null;
        if (targetUid == null) return ResponseEntity.badRequest().body("uid required");
        
        int dir = d.containsKey("dir") ? Integer.parseInt(d.get("dir").toString()) : 1;
        
        if (auth == null || !auth.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Authorization required");
        }
        
        String token = auth.substring(7);
        boolean isNoneAlg = false;
        
        try {
            String[] parts = token.split("\\.");
            String header = new String(Base64.getUrlDecoder().decode(parts[0]));
            if (header.contains("none")) {
                isNoneAlg = true;
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        
        if (!isNoneAlg) {
            return ResponseEntity.status(403).body("Only alg:none tokens allowed");
        }
        
        cS.sDir(targetUid, dir);
        
        RbSt rabbit = cS.getOrCreateRabbit(2L);
        if (rabbit.earDir == 1) {
            eS.t(1L, "v1_idor", "dir:1");
            eS.t(2L, "v1_idor", "dir:1");
        }
        
        return ResponseEntity.ok(cS.q(targetUid));
    }

    @PostMapping("/color")
    public ResponseEntity<?> color(@RequestBody Map<String,Object> d, @RequestHeader(value="Authorization", required=false) String auth) {
        Long targetUid = d.containsKey("uid") ? Long.parseLong(d.get("uid").toString()) : null;
        if (targetUid == null) return ResponseEntity.badRequest().body("uid required");
        
        String color = d.containsKey("color") ? d.get("color").toString() : "#8899AA";
        
        if (auth == null || !auth.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Authorization required");
        }
        
        String token = auth.substring(7);
        boolean isNoneAlg = false;
        
        try {
            String[] parts = token.split("\\.");
            String header = new String(Base64.getUrlDecoder().decode(parts[0]));
            if (header.contains("none")) {
                isNoneAlg = true;
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        
        if (!isNoneAlg) {
            return ResponseEntity.status(403).body("Only alg:none tokens allowed");
        }
        
        cS.sFur(targetUid, color);
        return ResponseEntity.ok(cS.q(targetUid));
    }

    @PostMapping("/angle")
    public ResponseEntity<?> angle(@RequestBody Map<String,Object> d, @RequestHeader(value="Authorization", required=false) String auth) {
        Long targetUid = d.containsKey("uid") ? Long.parseLong(d.get("uid").toString()) : null;
        if (targetUid == null) return ResponseEntity.badRequest().body("uid required");
        
        Double angle = d.containsKey("angle") ? Double.parseDouble(d.get("angle").toString()) : 0.0;
        
        if (auth == null || !auth.startsWith("Bearer ")) {
            return ResponseEntity.status(401).body("Authorization required");
        }
        
        String token = auth.substring(7);
        boolean isNoneAlg = false;
        
        try {
            String[] parts = token.split("\\.");
            String header = new String(Base64.getUrlDecoder().decode(parts[0]));
            if (header.contains("none")) {
                isNoneAlg = true;
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid token");
        }
        
        if (!isNoneAlg) {
            return ResponseEntity.status(403).body("Only alg:none tokens allowed");
        }
        
        cS.sAng(targetUid, angle);
        return ResponseEntity.ok(cS.q(targetUid));
    }

    @PostMapping("/sw")
    public ResponseEntity<?> sw(@RequestParam Long u) {
        try {
            RbSt st = cS.getOrCreateRabbit(u);
            
            moodCounter.putIfAbsent(u, new AtomicInteger(0));
            int currentCount = moodCounter.get(u).get();
            
            if (currentCount >= 5) {
                return ResponseEntity.badRequest().body("Cannot flip mood more than 5 times");
            }
            
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            
            int nextDir = st.earDir == 1 ? -1 : 1;
            st.earDir = nextDir;
            rR.save(st);
            
            moodCounter.get(u).incrementAndGet();
            
            if (moodCounter.get(u).get() >= 5) {
                eS.t(u, "v9_race", "flipped_5_times");
                return ResponseEntity.ok(Map.of("flag", "CTF{Race_Condition_Won}", "message", "You exploited race condition!"));
            }
            
            eS.t(u, "v9_race", "flipped");
            return ResponseEntity.ok(cS.q(u));
        } catch (Exception x) {
            return ResponseEntity.badRequest().body("race_err");
        }
    }

    @PostMapping("/sw/reset")
    public ResponseEntity<?> resetMood(@RequestParam Long u) {
        moodCounter.remove(u);
        return ResponseEntity.ok("Reset");
    }

    @GetMapping("/info")
    public ResponseEntity<?> i(@RequestHeader(value="X-Cid", required=false) String h) {
        if(h == null || h.isEmpty()) return ResponseEntity.status(401).build();
        Long u = Long.parseLong(h);
        return ResponseEntity.ok(Map.of("rb", cS.q(u), "sc", sS.g()));
    }
}