package com.fasto.datamanager.dto;

import java.io.Serializable;
import java.util.List;

public class LineupCreateDto implements Serializable {
    private static final long serialVersionUID = -7941829613695258353L;

    private long playerId;
    private long tournamentId;
    private List<Long> stocks;
    private String name;

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(long tournamentId) {
        this.tournamentId = tournamentId;
    }

    public List<Long> getStocks() {
        return stocks;
    }

    public void setStocks(List<Long> stocks) {
        this.stocks = stocks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
