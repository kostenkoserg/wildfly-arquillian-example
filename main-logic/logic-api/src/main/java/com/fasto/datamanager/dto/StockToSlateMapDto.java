package com.fasto.datamanager.dto;

import java.io.Serializable;

public class StockToSlateMapDto implements Serializable {
    private static final long serialVersionUID = -8459236167998408301L;

    private long stockToSlateMapId;
    private StockDto stockDto = new StockDto();
    private SlateDto slateDto = new SlateDto();

    public long getStockToSlateMapId() {
        return stockToSlateMapId;
    }

    public void setStockToSlateMapId(long stockToSlateMapId) {
        this.stockToSlateMapId = stockToSlateMapId;
    }

    public StockDto getStockDto() {
        return stockDto;
    }

    public void setStockDto(StockDto stockDto) {
        this.stockDto = stockDto;
    }

    public SlateDto getSlateDto() {
        return slateDto;
    }

    public void setSlateDto(SlateDto slateDto) {
        this.slateDto = slateDto;
    }
}
