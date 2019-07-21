package com.fasto.datamanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LineupDto implements Serializable {

  private static final long serialVersionUID = -7941829613695258051L;
  public boolean isHidden;

  private long lineupId;
  private String name;
  private int entries;
  private Date createdTime;
  private Date deletedTime;
  private String deletedReason;
  private List<Long> entryIdsList = new ArrayList<>();
  private List<StockDto> stockList = new ArrayList<>();
  private List<Long> stockIdsList = new ArrayList<>();
  private Map<Long, Integer> tournamentIdToEntriesCount = new HashMap<>();
  private SlateDto slateDto = new SlateDto();
  private PlayerDto playerDto = new PlayerDto();
  private boolean isLocked;
  private List<EntryDto> entriesList = new ArrayList<>();

  public int getEntries() {
    return entries;
  }

  public void setEntries(int entries) {
    this.entries = entries;
  }

  public void setHidden(boolean hidden) {
    isHidden = hidden;
  }

  public long getLineupId() {
    return lineupId;
  }

  public void setLineupId(long lineupId) {
    this.lineupId = lineupId;
  }

  public SlateDto getSlateDto() {
    return slateDto;
  }

  public boolean isLocked() {
    return isLocked;
  }

  public void setLocked(boolean locked) {
    isLocked = locked;
  }

  public void setSlateDto(SlateDto slateDto) {
    this.slateDto = slateDto;
  }

  public PlayerDto getPlayerDto() {
    return playerDto;
  }

  public void setPlayerDto(PlayerDto playerDto) {
    this.playerDto = playerDto;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getCreatedTime() {
    return createdTime;
  }

  public void setCreatedTime(Date createdTime) {
    this.createdTime = createdTime;
  }

  public Date getDeletedTime() {
    return deletedTime;
  }

  public void setDeletedTime(Date deletedTime) {
    this.deletedTime = deletedTime;
  }

  public String getDeletedReason() {
    return deletedReason;
  }

  public void setDeletedReason(String deletedReason) {
    this.deletedReason = deletedReason;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LineupDto lineupDto = (LineupDto) o;
    return lineupId == lineupDto.lineupId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(lineupId);
  }
}
