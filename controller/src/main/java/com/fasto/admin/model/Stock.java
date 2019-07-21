package com.fasto.admin.model;

/**
 * Stock
 */
public class Stock {

    private Long id;
    private String symbol;
    private String name;
    private String url;
    private Long stockExchangeId;
    private boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getStockExchangeId() {
        return stockExchangeId;
    }

    public void setStockExchangeId(Long stockExchangeId) {
        this.stockExchangeId = stockExchangeId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
