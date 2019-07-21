package com.fasto.datamanager.model;

import com.fasto.datamanager.dto.LineupDto;
import com.fasto.datamanager.model.converters.Convertible;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "LINEUP")
public class LineupEntity implements Convertible<LineupDto> {

  @Id
  @Column(name = "LINEUP_ID", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long lineupId;

  @ManyToOne
  @JoinColumn(name = "SLATE_ID")
  private SlateEntity slateEntity = new SlateEntity();

  @ManyToOne
  @JoinColumn(name = "PLAYER_ID")
  private PlayerEntity playerEntity = new PlayerEntity();

  @Column(name = "NAME", nullable = true, length = 255)
  private String name;

  @Temporal(value = TemporalType.TIMESTAMP)
  @Column(name = "CREATED_TIME", nullable = true)
  private Date createdTime;

  @Temporal(value = TemporalType.TIMESTAMP)
  @Column(name = "DELETED_TIME", nullable = true)
  private Date deletedTime;

  @Column(name = "DELETED_REASON", nullable = true, length = 255)
  private String deletedReason;

  @Column(name = "LOCKED", nullable = false)
  private boolean isLocked;

  private boolean hidden;

  public long getLineupId() {
    return lineupId;
  }

  public void setLineupId(long lineupId) {
    this.lineupId = lineupId;
  }

  public SlateEntity getSlateEntity() {
    return slateEntity;
  }

  public void setSlateEntity(SlateEntity slateEntity) {
    this.slateEntity = slateEntity;
  }

  public PlayerEntity getPlayerEntity() {
    return playerEntity;
  }

  public void setPlayerEntity(PlayerEntity playerEntity) {
    this.playerEntity = playerEntity;
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

  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  public boolean isLocked() {
    return isLocked;
  }

  public void setLocked(boolean locked) {
    isLocked = locked;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LineupEntity that = (LineupEntity) o;
    return hidden == that.hidden &&
        Objects.equals(slateEntity, that.slateEntity) &&
        Objects.equals(playerEntity, that.playerEntity) &&
        Objects.equals(name, that.name) &&
        Objects.equals(createdTime, that.createdTime) &&
        Objects.equals(deletedTime, that.deletedTime) &&
        Objects.equals(deletedReason, that.deletedReason);
  }

  @Override
  public int hashCode() {
    return Objects
        .hash(slateEntity, playerEntity, name, createdTime, deletedTime, deletedReason, hidden);
  }

  @Override
  public LineupDto convert() {

    LineupDto dto = new LineupDto();

    dto.setLineupId(this.lineupId);
    dto.setHidden(this.hidden);
    dto.setSlateDto(this.slateEntity.convert());
    dto.setPlayerDto(this.playerEntity.convert());
    dto.setName(this.name);
    dto.setCreatedTime(this.createdTime);
    dto.setDeletedTime(this.deletedTime);
    dto.setDeletedReason(this.deletedReason);
    dto.setLocked(this.isLocked);
    return dto;
  }
}
