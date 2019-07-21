package com.fasto.datamanager.dto;

import java.io.Serializable;

public class ScorerDto implements Serializable {
    private static final long serialVersionUID = 6759144089434083996L;

    private long scorerId;
    private String name;

    public long getScorerId() {
        return scorerId;
    }

    public void setScorerId(long scorerId) {
        this.scorerId = scorerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
