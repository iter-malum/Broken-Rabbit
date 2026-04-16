// FILE: src/main/java/com/brkrb/mdl/ExFlg.java
package com.brkrb.mdl;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "ex_flgs")
public class ExFlg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "v_id", unique = true)
    private String vid;
    
    @Column(name = "is_solved")
    private boolean solved;
    
    @Column(name = "solved_at")
    private LocalDateTime solvedAt;

    public ExFlg() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getVid() { return vid; }
    public void setVid(String vid) { this.vid = vid; }
    public boolean isSolved() { return solved; }
    public void setSolved(boolean solved) { this.solved = solved; }
    public LocalDateTime getSolvedAt() { return solvedAt; }
    public void setSolvedAt(LocalDateTime solvedAt) { this.solvedAt = solvedAt; }
}