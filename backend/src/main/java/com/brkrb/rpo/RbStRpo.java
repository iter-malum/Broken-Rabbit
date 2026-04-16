package com.brkrb.rpo;

import com.brkrb.mdl.RbSt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface RbStRpo extends JpaRepository<RbSt, Long> {
    Optional<RbSt> findByUid(Long uid);
    
    List<RbSt> findAllByUid(Long uid); 
    
    @Modifying
    @Query("update RbSt r set r.earDir = ?1 where r.uid = ?2")
    int setEarDirByUid(int dir, Long uid);
}