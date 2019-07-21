package com.fasto.datamanager.dto;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EntryDto implements Serializable {
    private static final long serialVersionUID = -355321872291385524L;

    private long entryId;
    private PlayerDto playerDto = new PlayerDto();
    private TournamentDto tournamentDto = new TournamentDto();
    private Date timestamp;
    private int statusId;
    private int rank;
    private int payout;
    private LineupDto lineupDto = new LineupDto();
    private Date lastTimeStatusChange;

    private double score;
    private Map<Long, List<Integer>> playerIdToRankRangeMap = new HashMap<>();

    public long getEntryId() {
        return entryId;
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public PlayerDto getPlayerDto() {
        return playerDto;
    }

    public void setPlayerDto(PlayerDto playerDto) {
        this.playerDto = playerDto;
    }

    public TournamentDto getTournamentDto() {
        return tournamentDto;
    }

    public void setTournamentDto(TournamentDto tournamentDto) {
        this.tournamentDto = tournamentDto;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getPayout() {
        return payout;
    }

    public void setPayout(int payout) {
        this.payout = payout;
    }

    public LineupDto getLineupDto() {
        return lineupDto;
    }

    public void setLineupDto(LineupDto lineupDto) {
        this.lineupDto = lineupDto;
    }

    public Date getLastTimeStatusChange() {
        return lastTimeStatusChange;
    }

    public void setLastTimeStatusChange(Date lastTimeStatusChange) {
        this.lastTimeStatusChange = lastTimeStatusChange;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
