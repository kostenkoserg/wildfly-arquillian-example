package com.fasto.datamanager.model;

import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.SlateDto;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SLATE")
public class SlateEntity implements Convertible<SlateDto> {

  @Id
  @Column(name = "SLATE_ID", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long slateId;

  @Column(name = "NAME", nullable = false, length = 255)
  private String name;

  @Column(name = "LINEUP_SIZE")
  private int lineupSize = 6;

  @Column(name = "ENABLED", nullable = false)
  private boolean enabled;
  
  @ManyToOne
  @JoinColumn(name = "STOCK_EXCHANGE_ID", nullable = false)
  private StockExchangeEntity stockExchangeEntity;

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

  public StockExchangeEntity getStockExchangeEntity() {
    return stockExchangeEntity;
  }

  public void setStockExchangeEntity(StockExchangeEntity stockExchangeEntity) {
    this.stockExchangeEntity = stockExchangeEntity;
  }

  public boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }


  public Integer getLineupSize() {
    return lineupSize;
  }

  public void setLineupSize(Integer lineupSize) {
    this.lineupSize = lineupSize;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SlateEntity that = (SlateEntity) o;
    return slateId == that.slateId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(slateId);
  }

  @Override
  public SlateDto convert() {
    SlateDto dto = new SlateDto();
    dto.setName(this.name);
    dto.setSlateId(this.slateId);
    dto.setEnabled(this.enabled);
    dto.setLineupSize(this.lineupSize);

    Optional<StockExchangeEntity> stockExchangeEntityOptional = Optional.ofNullable(this.stockExchangeEntity);
    StockExchangeEntity stockExchangeEntity = stockExchangeEntityOptional.isPresent() ? stockExchangeEntityOptional.get()  : new StockExchangeEntity();
    dto.setStockExchangeDto(stockExchangeEntity.convert());

    return dto;
  }
}
