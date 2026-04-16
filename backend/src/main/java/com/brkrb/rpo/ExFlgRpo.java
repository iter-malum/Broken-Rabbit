// FILE: src/main/java/com/brkrb/rpo/ExFlgRpo.java
package com.brkrb.rpo;

import com.brkrb.mdl.ExFlg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

public interface ExFlgRpo extends JpaRepository<ExFlg, Long> {
    Optional<ExFlg> findByVid(String vid);
    
    List<ExFlg> findBySolvedTrue();
    
    @Modifying
    @Transactional
    @Query("UPDATE ExFlg e SET e.solved = true, e.solvedAt = CURRENT_TIMESTAMP WHERE e.vid = :vid")
    void updateSolved(@Param("vid") String vid);
}