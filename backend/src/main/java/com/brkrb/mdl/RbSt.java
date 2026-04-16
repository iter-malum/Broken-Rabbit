package com.brkrb.mdl;

import javax.persistence.*;

@Entity
@Table(name = "rb_st")
@Access(AccessType.FIELD)
public class RbSt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    
    @Column(name = "u_id")
    public Long uid; 
    
    @Column(name = "ear_dir")
    public Integer earDir;
    
    @Column(name = "fur_c")
    public String furC;
    
    @Column(name = "h_ang")
    public Double hAng;
    
    @Column(name = "meta_json", columnDefinition = "text")
    public String metaJson;

    public RbSt() {}
    public RbSt(Long uid) {
        this.uid = uid;
        this.earDir = -1;
        this.furC = "#8899AA";
        this.hAng = 0.0;
        this.metaJson = "{\"dir\":-1,\"locked\":false}";
    }
}