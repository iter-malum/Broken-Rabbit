// FILE: src/main/java/com/brkrb/svc/ScSvc.java
package com.brkrb.svc;

import com.brkrb.mdl.ExFlg;
import com.brkrb.rpo.ExFlgRpo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScSvc {
    @Autowired private ExFlgRpo exFlgRpo;

    private static final Map<String, Integer> W = new HashMap<>();
    static {
        W.put("v1_idor", 2);
        W.put("v2_sqli", 9);
        W.put("v3_jwt", 4);
        W.put("v4_path", 2);
        W.put("v5_xxe", 2);
        W.put("v6_ssrf", 6);
        W.put("v7_deser", 8);
        W.put("v8_log", 2);
        W.put("v9_race", 2);
        W.put("v10_back", 3);
    }

    public int c() {
        List<ExFlg> solved = exFlgRpo.findBySolvedTrue();
        int total = 0;
        for (ExFlg flg : solved) {
            total += W.getOrDefault(flg.getVid(), 0);
        }
        return total;
    }

    public Map<String, Object> g() {
        List<ExFlg> solvedFlags = exFlgRpo.findBySolvedTrue();
        Map<String, Boolean> solved = new HashMap<>();
        for (String vid : W.keySet()) {
            solved.put(vid, false);
        }
        for (ExFlg flg : solvedFlags) {
            solved.put(flg.getVid(), true);
        }
        int score = c();
        String grade = score >= 35 ? "S" : score >= 28 ? "A" : score >= 20 ? "B" : score >= 12 ? "C" : "F";
        return Map.of("sc", score, "mx", 40, "gr", grade, "ws", solved);
    }
}