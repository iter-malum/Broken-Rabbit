package com.brkrb.rpo;

import com.brkrb.mdl.Usr;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsrRpo extends JpaRepository<Usr, Long> {
    Optional<Usr> findByUn(String un);
}