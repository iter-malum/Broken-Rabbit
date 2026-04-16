package com.brkrb.mdl;

import java.io.Serializable;

public class DtX implements Serializable {
    private static final long serialVersionUID = 42L;
    public String t;
    public Object p;
    public boolean v;

    public DtX() {}
    public DtX(String t, Object p) {
        this.t = t;
        this.p = p;
        this.v = false;
    }
}