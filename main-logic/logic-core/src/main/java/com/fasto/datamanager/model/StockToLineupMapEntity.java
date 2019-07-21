package com.fasto.datamanager.model;


import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.LineupDto;
import com.fasto.datamanager.dto.StockDto;
import com.fasto.datamanager.dto.StockToLineupMapDto;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "STOCK_TO_LINEUP_MAP")
public class StockToLineupMapEntity implements Convertible<StockToLineupMapDto> {
    private long id;
    private LineupEntity lineupEntity;
    private StockEntity stockEntity;

    @Id
    @Column(name = "ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ManyToOne
    @JoinColumn(name = "LINEUP_ID", nullable = true)
    public LineupEntity getLineupEntity() {
        return lineupEntity;
    }

    public void setLineupEntity(LineupEntity lineupEntity) {
        this.lineupEntity = lineupEntity;
    }

    @ManyToOne
    @JoinColumn(name = "STOCK_ID", nullable = true)
    public StockEntity getStockEntity() {
        return stockEntity;
    }

    public void setStockEntity(StockEntity stockEntity) {
        this.stockEntity = stockEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StockToLineupMapEntity that = (StockToLineupMapEntity) o;
        return id == that.id &&
                Objects.equals(lineupEntity, that.lineupEntity) &&
                Objects.equals(stockEntity, that.stockEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, lineupEntity, stockEntity);
    }


    @Override
    public StockToLineupMapDto convert() {
        StockToLineupMapDto dto = new StockToLineupMapDto();
        dto.setId(this.id);

        LineupDto lineupDto = this.lineupEntity.convert();
        dto.setLineupDto(lineupDto);

        StockDto stockDto = this.stockEntity.convert();
        dto.setStockDto(stockDto);
        return dto;
    }
}
