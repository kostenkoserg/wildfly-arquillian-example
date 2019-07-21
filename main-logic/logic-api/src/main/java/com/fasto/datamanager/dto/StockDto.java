package com.fasto.datamanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class StockDto implements Serializable {
    private static final long serialVersionUID = 2190983241239190513L;

    private long stockId;
    private String url;
    private String symbol;
    private String name;
    private boolean enabled;
    private StockExchangeDto stockExchangeDto = new StockExchangeDto();
    private List<Long> stockToLineupMapIDsList = new ArrayList<>();
    private List<Long> stockToSlateMapIDsList = new ArrayList<>();
    private Map<Long, List<Integer>> playerIdToRankRangeMap = new HashMap();
    private float gain;
    private int position;

    public long getStockId() {
        return stockId;
    }

    public void setStockId(long stockId) {
        this.stockId = stockId;
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

    public StockExchangeDto getStockExchangeDto() {
        return stockExchangeDto;
    }

    public void setStockExchangeDto(StockExchangeDto stockExchangeDto) {
        this.stockExchangeDto = stockExchangeDto;
    }

    public List<Long> getStockToLineupMapIDsList() {
        return stockToLineupMapIDsList;
    }

    public void setStockToLineupMapIDsList(List<Long> stockToLineupMapIDsList) {
        this.stockToLineupMapIDsList = stockToLineupMapIDsList;
    }

    public List<Long> getStockToSlateMapIDsList() {
        return stockToSlateMapIDsList;
    }

    public void setStockToSlateMapIDsList(List<Long> stockToSlateMapIDsList) {
        this.stockToSlateMapIDsList = stockToSlateMapIDsList;
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

    public boolean isEnabled() {
        return enabled;
    }

    public float getGain() {
        return gain;
    }

    public void setGain(float gain) {
        this.gain = gain;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StockDto stockDto = (StockDto) o;
        return stockId == stockDto.stockId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(stockId);
    }
}
