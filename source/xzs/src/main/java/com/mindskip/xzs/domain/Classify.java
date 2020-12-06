package com.mindskip.xzs.domain;

import java.io.Serializable;

public class Classify implements Serializable {

    private static final long serialVersionUID = 870464022338336062L;

    private Integer id;

    private String name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }
}