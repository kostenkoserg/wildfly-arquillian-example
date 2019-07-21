package com.fasto.datamanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class TournamentDto implements Serializable {

  private static final long serialVersionUID = -1517274021497841194L;

  private long tournamentId;
  private String name;
  private Date setupTimestamp;
  private Date startTimestamp;
  private Date visibleInLobbyTimestamp;
  private TournamentStatus status;
  private String cancellationReason;
  private Date cancellationTimestamp;
  private int currentEntries;
  private int currentPlayers;
  private int rank;
  private long myTotalPayout;
  private String type;
  private String structure;
  private int entries;
  private String payoutName;
  private int duration;
  private List<Long> stockIds = new ArrayList();

  private SlateDto slateDto = new SlateDto();
  private TournamentTemplateDto tournamentTemplateDto = new TournamentTemplateDto();

  public int getDuration() {
    return duration;
  }

  public void setDuration(int duration) {
    this.duration = duration;
  }

  public int getRank() {
    return rank;
  }

  public void setRank(int rank) {
    this.rank = rank;
  }

  public long getTournamentId() {
    return tournamentId;
  }

  public void setTournamentId(long tournamentId) {
    this.tournamentId = tournamentId;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getStructure() {
    return structure;
  }
 
  public void setStructure(String structure) {
    this.structure = structure;
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

  public void setVisibleInLobbyTimestamp(Date visibleInLobbyTimestamp) {
    this.visibleInLobbyTimestamp = visibleInLobbyTimestamp;
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

  public void setEntries(int totalEntries) {
    this.entries = totalEntries;
  }

  public int getEntries() {
    return entries;
  }

  public void setMyTotalPayout(long myTotalPayout) {
    this.myTotalPayout = myTotalPayout;
  }

  public long getMyTotalPayout() {
    return myTotalPayout;
  }

  public String getPayoutName() {
    return payoutName;
  }

  public void setPayoutName(String payoutName) {
    this.payoutName = payoutName;
  }

  public TournamentTemplateDto getTournamentTemplateDto() {
      return tournamentTemplateDto;
  }

  public void setTournamentTemplateDto(TournamentTemplateDto tournamentTemplateDto) {
      this.tournamentTemplateDto = tournamentTemplateDto;
  }

  public SlateDto getSlateDto() {
      return slateDto;
  }

  public void setSlateDto(SlateDto slateDto) {
      this.slateDto = slateDto;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TournamentDto that = (TournamentDto) o;
    return tournamentId == that.tournamentId;
  }

  @Override
  public int hashCode() {
    return Objects.hash(tournamentId);
  }
}
