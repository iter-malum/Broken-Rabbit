// FILE: src/main/java/com/brkrb/svc/ExSvc.java
package com.brkrb.svc;

import com.brkrb.mdl.ExFlg;
import com.brkrb.rpo.ExFlgRpo;
import com.brkrb.rpo.RbStRpo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class ExSvc {
    @Autowired private ExFlgRpo exFlgRpo;
    @Autowired private RbStRpo rbStRpo;
    @Autowired private SimpMessagingTemplate ws;
    @Autowired private CrSvc cr;

    public boolean t(Long u, String vid, Object p) {
        Optional<ExFlg> existing = exFlgRpo.findByVid(vid);
        if (existing.isPresent() && existing.get().isSolved()) return true;

        boolean ok = false;
        String payload = p.toString();
        
        switch (vid) {
            case "v1_idor": ok = payload.contains("dir:1"); break;
            case "v2_sqli": ok = payload.contains("admin123") || payload.contains("password"); break;
            case "v3_jwt": ok = payload.contains("XSS") || payload.contains("alert"); break;
            case "v4_path": ok = payload.contains("../../") || payload.contains("/etc/passwd"); break;
            case "v5_xxe": ok = payload.contains("<!ENTITY") || payload.contains("/etc/passwd"); break;
            case "v6_ssrf": ok = payload.contains("secret_key") || payload.contains("CTF{SSRF"); break;
            case "v7_deser": ok = payload.contains("root:x:0:0") || payload.contains("/etc/passwd"); break;
            case "v8_log": ok = payload.contains("root:x:0:0") || payload.contains("/etc/passwd") || payload.contains("ldap"); break;
            case "v9_race": ok = payload.contains("flipped_5_times"); break;
            case "v10_back": ok = payload.contains("__proto__") || payload.contains("polluted"); break;
            default: break;
        }

        if (ok) {
            if (existing.isPresent()) {
                exFlgRpo.updateSolved(vid);
            } else {
                ExFlg newFlg = new ExFlg();
                newFlg.setVid(vid);
                newFlg.setSolved(true);
                newFlg.setSolvedAt(LocalDateTime.now());
                exFlgRpo.save(newFlg);
            }
            cr.sDir(u, vid.contains("idor") || vid.contains("jwt") ? 1 : -1);
            if (vid.equals("v5_xxe")) cr.sFur(u, "#FF5733");
            ws.convertAndSendToUser(String.valueOf(u), "/topic/progress", vid);
            return true;
        }
        return false;
    }
}