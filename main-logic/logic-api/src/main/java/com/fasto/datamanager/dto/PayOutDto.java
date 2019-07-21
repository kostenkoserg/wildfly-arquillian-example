package com.fasto.datamanager.dto;

import java.io.Serializable;

/**
 *
 * @author kostenko
 */
public class PayOutDto implements Serializable {

    private long id;
    private String name;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
