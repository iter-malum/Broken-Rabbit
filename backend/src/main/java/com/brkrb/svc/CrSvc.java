// FILE: src/main/java/com/brkrb/svc/CrSvc.java
package com.brkrb.svc;

import com.brkrb.mdl.RbSt;
import com.brkrb.rpo.RbStRpo;
import com.brkrb.util.HshUtl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class CrSvc {
    private static final Logger log = Logger.getLogger(CrSvc.class.getName());
    
    @Autowired private RbStRpo rR;
    @Autowired @Qualifier("rT") private RedisTemplate<String, Object> rT;
    @Value("${app.rabbit.salt}") private String slt;

    @Transactional
    public RbSt getOrCreateRabbit(Long uid) {
        RbSt st = rR.findByUid(uid).orElse(null);
        if (st == null) {
            log.info("Creating new rabbit for uid: " + uid);
            st = new RbSt(uid);
            st = rR.save(st);
        }
        return st;
    }

    @Transactional
    public void sDir(Long u, int d) {
        RbSt st = getOrCreateRabbit(u);
        st.earDir = d;
        rR.save(st);
        rT.opsForValue().set("rb:dir:" + u, String.valueOf(d));
        log.info("Updated ear_dir for uid " + u + " to " + d);
    }

    @Transactional
    public void sFur(Long u, String c) {
        RbSt st = getOrCreateRabbit(u);
        st.furC = c;
        rR.save(st);
        rT.opsForValue().set("rb:col:" + u, c);
    }

    @Transactional
    public void sAng(Long u, Double a) {
        RbSt st = getOrCreateRabbit(u);
        st.hAng = a;
        rR.save(st);
        rT.opsForValue().set("rb:ang:" + u, String.valueOf(a));
    }

    public Map<String, Object> q(Long u) {
        RbSt st = getOrCreateRabbit(u);
        Map<String, Object> m = new HashMap<>();
        m.put("d", st.earDir);
        m.put("c", st.furC);
        m.put("a", st.hAng);
        m.put("cfg", HshUtl.genRabbitCfg(String.valueOf(u), slt));
        return m;
    }
}