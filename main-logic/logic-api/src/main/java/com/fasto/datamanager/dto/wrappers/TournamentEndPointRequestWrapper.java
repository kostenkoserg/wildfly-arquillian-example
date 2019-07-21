package com.fasto.datamanager.dto.wrappers;

import com.fasto.datamanager.dto.TournamentStatus;

import java.io.Serializable;

public class TournamentEndPointRequestWrapper implements Serializable {
    Long tournamentId;
    Long playerId;
    Integer perPage;
    Integer page;
    TournamentStatus tournamentStatus;
    Long lineupId;

    public TournamentEndPointRequestWrapper() {
    }

    public TournamentEndPointRequestWrapper(Long playerId) {
        this.playerId = playerId;
    }

    public TournamentEndPointRequestWrapper(Integer perPage, Integer page) {
        this.perPage = perPage;
        this.page = page;
    }

    public TournamentEndPointRequestWrapper(Long playerId, Integer perPage, Integer page, TournamentStatus tournamentStatus) {
        this.playerId = playerId;
        this.perPage = perPage;
        this.page = page;
        this.tournamentStatus = tournamentStatus;
    }


    public Long getLineupId() {
        return lineupId;
    }

    public void setLineupId(Long lineupId) {
        this.lineupId = lineupId;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public Integer getPerPage() {
        return perPage == null ? 0 : perPage;
    }

    public void setPerPage(Integer perPage) {
        this.perPage = perPage;
    }

    public Integer getPage() {
        return page == null ? 0 : page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public TournamentStatus getTournamentStatus() {
        return tournamentStatus;
    }

    public void setTournamentStatus(TournamentStatus tournamentStatus) {
        this.tournamentStatus = tournamentStatus;
    }

    @Override
    public String toString() {
        return "TournamentEndPointRequestWrapper{" +
                "tournamentId=" + tournamentId +
                ", playerId=" + playerId +
                ", perPage=" + perPage +
                ", page=" + page +
                ", tournamentStatus=" + tournamentStatus +
                '}';
    }
}
