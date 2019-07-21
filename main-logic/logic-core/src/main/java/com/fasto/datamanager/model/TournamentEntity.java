package com.fasto.datamanager.model;

import com.fasto.datamanager.dto.TournamentDto;
import com.fasto.datamanager.dto.TournamentStatus;
import com.fasto.datamanager.model.converters.Convertible;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "TOURNAMENT")
@NamedEntityGraph(name = "graph.tournament.tournamentTemplate", attributeNodes = @NamedAttributeNode("tournamentTemplateEntity"))
public class TournamentEntity implements Convertible<TournamentDto> {

  @Id
  @Column(name = "TOURNAMENT_ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long tournamentId;

  @ManyToOne
  @JoinColumn(name = "SLATE_ID", nullable = true)
  private SlateEntity slateEntity;

  @ManyToOne
  @JoinColumn(name = "TOURNAMENT_TEMPLATE_ID", nullable = true)
  private TournamentTemplateEntity tournamentTemplateEntity;

  @Column(name = "NAME", nullable = true, length = 255)
  private String name;

  @Temporal(value = TemporalType.TIMESTAMP)
  @Column(name = "SETUP_TIMESTAMP", nullable = true)
  private Date setupTimestamp;

  @Temporal(value = TemporalType.TIMESTAMP)
  @Column(name = "START_TIMESTAMP", nullable = true)
  private Date startTimestamp;

  @Temporal(value = TemporalType.TIMESTAMP)
  @Column(name = "VISIBLE_IN_LOBBY_TIMESTAMP", nullable = true)
  private Date visibleInLobbyTimestamp;

  @Column(name = "STATUS", nullable = true)
  @Enumerated(EnumType.STRING)
  private TournamentStatus status;

  @Column(name = "CANCELLATION_REASON", nullable = true, length = 255)
  private String cancellationReason;

  @Column(name = "TOURNAMENT_TYPE", nullable = true)
  private String tournamentType;

  @Column(name = "TOURNAMENT_STRUCTURE", nullable = true)
  private String tournamentStructure;

  @Temporal(value = TemporalType.TIMESTAMP)
  @Column(name = "CANCELLATION_TIMESTAMP", nullable = true)
  private Date cancellationTimestamp;

  @Column(name = "CURRENT_ENTRIES", nullable = true)
  private int currentEntries;

  @Column(name = "CURRENT_PLAYERS", nullable = true)
  private int currentPlayers;

  public long getTournamentId() {
    return tournamentId;
  }

  public void setTournamentId(long tournamentId) {
    this.tournamentId = tournamentId;
  }

  public SlateEntity getSlateEntity() {
    return slateEntity;
  }

  public void setSlateEntity(SlateEntity slateEntity) {
    this.slateEntity = slateEntity;
  }

  public TournamentTemplateEntity getTournamentTemplateEntity() {
    return tournamentTemplateEntity;
  }

  public void setTournamentTemplateEntity(TournamentTemplateEntity tournamentTemplateEntity) {
    this.tournamentTemplateEntity = tournamentTemplateEntity;
  }

  public String getTournamentType() {
    return tournamentType;
  }

  public void setTournamentType(String tournamentType) {
    this.tournamentType = tournamentType;
  }

  public String getTournamentStructure() {
    return tournamentStructure;
  }

  public void setTournamentStructure(String tournamentStructure) {
    this.tournamentStructure = tournamentStructure;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getSetupTimestamp() {
    return setupTimestamp;
  }

  public void setSetupTimestamp(Date setupTimestamp) {
    this.setupTimestamp = setupTimestamp;
  }

  public Date getStartTimestamp() {
    return startTimestamp;
  }

  public void setStartTimestamp(Date startTimestamp) {
    this.startTimestamp = startTimestamp;
  }

  public Date getVisibleInLobbyTimestamp() {
    return visibleInLobbyTimestamp;
  }

  public void setVisibleInLobbyTimestamp(Date visableInLobbyTimestamp) {
    this.visibleInLobbyTimestamp = visableInLobbyTimestamp;
  }

  public TournamentStatus getStatus() {
    return status;
  }

  public void setStatus(TournamentStatus status) {
    this.status = status;
  }

  public String getCancellationReason() {
    return cancellationReason;
  }

  public void setCancellationReason(String cancellationReason) {
    this.cancellationReason = cancellationReason;
  }

  public Date getCancellationTimestamp() {
    return cancellationTimestamp;
  }

  public void setCancellationTimestamp(Date cancellationTimestamp) {
    this.cancellationTimestamp = cancellationTimestamp;
  }

  public int getCurrentEntries() {
    return currentEntries;
  }

  public void setCurrentEntries(int currentEntries) {
    this.currentEntries = currentEntries;
  }

  public int getCurrentPlayers() {
    return currentPlayers;
  }

  public void setCurrentPlayers(int currentPlayers) {
    this.currentPlayers = currentPlayers;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TournamentEntity that = (TournamentEntity) o;
    return tournamentId == that.tournamentId &&
        Objects.equals(slateEntity, that.slateEntity) &&
        Objects.equals(tournamentTemplateEntity, that.tournamentTemplateEntity) &&
        Objects.equals(name, that.name) &&
        Objects.equals(setupTimestamp, that.setupTimestamp) &&
        Objects.equals(startTimestamp, that.startTimestamp) &&
        Objects.equals(visibleInLobbyTimestamp, that.visibleInLobbyTimestamp) &&
        Objects.equals(status, that.status) &&
        Objects.equals(cancellationReason, that.cancellationReason) &&
        Objects.equals(cancellationTimestamp, that.cancellationTimestamp) &&
        Objects.equals(currentEntries, that.currentEntries) &&
        Objects.equals(currentPlayers, that.currentPlayers);
  }

  @Override
  public int hashCode() {

    return Objects.hash(tournamentId, slateEntity, tournamentTemplateEntity, name, setupTimestamp,
        startTimestamp, visibleInLobbyTimestamp, status, cancellationReason, cancellationTimestamp,
        currentEntries, currentPlayers);
  }

  @Override
  public TournamentDto convert() {
    TournamentDto dto = new TournamentDto();
    dto.setTournamentId(this.tournamentId);
    dto.setName(this.name);
    TournamentTemplateEntity tournamentTemplateEntity = this.tournamentTemplateEntity;
    if (tournamentTemplateEntity != null) {
      dto.setTournamentTemplateDto(tournamentTemplateEntity.convert());
      dto.setDuration(tournamentTemplateEntity.getDuration());
      PayOutEntity payOutEntity = tournamentTemplateEntity.getPayOutEntity();
      if (payOutEntity != null) {
        dto.setPayoutName(payOutEntity.getName());
      }
    }
    dto.setSetupTimestamp(this.setupTimestamp);
    dto.setStartTimestamp(this.startTimestamp);
    dto.setVisibleInLobbyTimestamp(this.visibleInLobbyTimestamp);
    dto.setStatus(this.status);
    dto.setCancellationReason(this.cancellationReason);
    dto.setCancellationTimestamp(this.cancellationTimestamp);
    dto.setCurrentEntries(this.currentEntries);
    dto.setCurrentPlayers(this.currentPlayers);
    dto.setStructure(this.tournamentStructure);
    dto.setType(this.tournamentType);
    dto.setSlateDto(this.slateEntity.convert());
    return dto;
  }

}
