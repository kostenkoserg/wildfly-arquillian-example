package com.fasto.datamanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TournamentTemplateDto implements Serializable {
    private static final long serialVersionUID = -3552362390230041608L;

    private long tournamentTemplateId;
    private PayOutDto payOutDto = new PayOutDto();
    private int buyIn;
    private int rake;
    private boolean guaranteed;
    private String name;
    private int minPlayers;
    private int maxPlayers;
    private int minEntries;
    private int maxEntries;
    private int maxEntriesPerPlayer;
    private int duration;
    private int scorerId;
    private int startingBalance;
    private ScorerDto scorerDto = new ScorerDto();
    private List<Long> tournamentIDsList = new ArrayList<>();

    public long getTournamentTemplateId() {
        return tournamentTemplateId;
    }

    public void setTournamentTemplateId(long tournamentTemplateId) {
        this.tournamentTemplateId = tournamentTemplateId;
    }

    public PayOutDto getPayOutDto() {
        return payOutDto;
    }

    public void setPayOutDto(PayOutDto payOutDto) {
        this.payOutDto = payOutDto;
    }

    public int getBuyIn() {
        return buyIn;
    }

    public void setBuyIn(int buyIn) {
        this.buyIn = buyIn;
    }

    public int getRake() {
        return rake;
    }

    public void setRake(int rake) {
        this.rake = rake;
    }

    public boolean getGuaranteed() {
        return guaranteed;
    }

    public void setGuaranteed(boolean guaranteed) {
        this.guaranteed = guaranteed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(int minPlayers) {
        this.minPlayers = minPlayers;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public int getMinEntries() {
        return minEntries;
    }

    public void setMinEntries(int minEntries) {
        this.minEntries = minEntries;
    }

    public int getMaxEntries() {
        return maxEntries;
    }

    public void setMaxEntries(int maxEntries) {
        this.maxEntries = maxEntries;
    }

    public int getMaxEntriesPerPlayer() {
        return maxEntriesPerPlayer;
    }

    public void setMaxEntriesPerPlayer(int maxEntriesPerPlayer) {
        this.maxEntriesPerPlayer = maxEntriesPerPlayer;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getScorerId() {
        return scorerId;
    }

    public void setScorerId(int scorerId) {
        this.scorerId = scorerId;
    }

    public int getStartingBalance() {
        return startingBalance;
    }

    public void setStartingBalance(int startingBalance) {
        this.startingBalance = startingBalance;
    }

    public List<Long> getTournamentIDsList() {
        return tournamentIDsList;
    }

    public void setTournamentIDsList(List<Long> tournamentIDsList) {
        this.tournamentIDsList = tournamentIDsList;
    }

    public void setScorerDto(ScorerDto scorerDto) {
        this.scorerDto = scorerDto;
    }

    public ScorerDto getScorerDto() {
        return scorerDto;
    }
}
