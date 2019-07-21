package com.fasto.admin.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Slate
 */
public class Slate {

    private Long id;
    private String name;
    private Boolean enabled;
    private Long stockExchangeId;
    private List<Long> stockIds = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Long getStockExchangeId() {
        return stockExchangeId;
    }

    public void setStockExchangeId(Long stockExchangeId) {
        this.stockExchangeId = stockExchangeId;
    }

    public List<Long> getStockIds() {
        return stockIds;
    }

    public void setStockIds(List<Long> stockIds) {
        this.stockIds = stockIds;
    }

}
