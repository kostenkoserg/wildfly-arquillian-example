package com.fasto.datamanager.dto;

import java.io.Serializable;

public class StockToLineupMapDto implements Serializable {
    private static final long serialVersionUID = 3819340858281195152L;

    private long id;
    private LineupDto lineupDto = new LineupDto();
    private StockDto stockDto = new StockDto();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LineupDto getLineupDto() {
        return lineupDto;
    }

    public void setLineupDto(LineupDto lineupDto) {
        this.lineupDto = lineupDto;
    }

    public StockDto getStockDto() {
        return stockDto;
    }

    public void setStockDto(StockDto stockDto) {
        this.stockDto = stockDto;
    }
}
