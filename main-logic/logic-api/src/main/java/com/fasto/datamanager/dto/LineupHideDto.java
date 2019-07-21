package com.fasto.datamanager.dto;

import java.io.Serializable;

public class LineupHideDto implements Serializable {

  private static final long serialVersionUID = -7941829613695258051L;

  private long lineupId;
  private long tournamentId;

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
    return "LineupHideDto{" +
        "lineupId=" + lineupId +
        ", tournamentId=" + tournamentId +
        '}';
  }
}
