package com.fasto.datamanager.dto.wrappers;

public class TournamentEntryDto {

  private long entries;
  private long lineupId;
  private long tournamentId;

  public long getEntries() {
    return entries;
  }

  public void setEntries(long entries) {
    this.entries = entries;
  }

  public long getLineupId() {
    return lineupId;
  }

  public void setLineupId(long lineupId) {
    this.lineupId = lineupId;
  }

  public long getTournamentId() {
    return tournamentId;
  }

  public void setTournamentId(long tournamentId) {
    this.tournamentId = tournamentId;
  }

  @Override
  public String toString() {
    return "TournamentEntryDto{" +
        "entries=" + entries +
        ", lineupId=" + lineupId +
        ", tournamentId=" + tournamentId +
        '}';
  }
}
