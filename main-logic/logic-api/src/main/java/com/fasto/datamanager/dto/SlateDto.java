package com.fasto.datamanager.dto;

import java.io.Serializable;
import java.util.Objects;

public class SlateDto implements Serializable {
    private static final long serialVersionUID = 7594146921053737210L;

    private long slateId;
    private String name;
    private long lineupSize;
    private boolean enabled;
    private StockExchangeDto stockExchangeDto = new StockExchangeDto();

    public long getSlateId() {
        return slateId;
    }

    public void setSlateId(long slateId) {
        this.slateId = slateId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getLineupSize() {
        return lineupSize;
    }

    public void setLineupSize(long lineupSize) {
        this.lineupSize = lineupSize;
    }

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public StockExchangeDto getStockExchangeDto() {
        return stockExchangeDto;
    }

    public void setStockExchangeDto(StockExchangeDto stockExchangeDto) {
        this.stockExchangeDto = stockExchangeDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SlateDto slateDto = (SlateDto) o;
        return slateId == slateDto.slateId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(slateId);
    }
}
