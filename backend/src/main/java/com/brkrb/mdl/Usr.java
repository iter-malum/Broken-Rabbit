package com.brkrb.mdl;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "usr")
@Access(AccessType.FIELD)
public class Usr {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    
    @Column(name = "un", nullable = false, unique = true)
    public String un;
    
    @Column(name = "ph", nullable = false)
    public String ph;
    
    @Column(name = "s", nullable = false)
    public String s;
    
    @Column(name = "cr_ts")
    public LocalDateTime crTs;

    public Usr() {}
    public Usr(String un, String ph, String s) {
        this.un = un; this.ph = ph; this.s = s;
        this.crTs = LocalDateTime.now();
    }
}