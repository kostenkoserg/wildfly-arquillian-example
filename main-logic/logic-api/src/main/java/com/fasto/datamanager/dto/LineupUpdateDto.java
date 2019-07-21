package com.fasto.datamanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class LineupUpdateDto implements Serializable {
    private static final long serialVersionUID = -7941829613695258353L;
    private List<Long> stocks = new ArrayList<>();
    private Long id;
    private String name;
    private Long tournamentId;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public List<Long> getStocks() {
        return stocks;
    }

    public void setStocks(List<Long> stocks) {
        this.stocks = stocks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getTournamentId() {
        return tournamentId;
    }

    public void setTournamentId(Long tournamentId) {
        this.tournamentId = tournamentId;
    }
}
