package com.fasto.admin.model;

/**
 * TournamentTemplate
 */
public class TournamentTemplate {

    private Long id;
    private String name;
    private Integer buyIn;
    private Integer rake;
    private Boolean guaranteed;
    private Integer minPlayers;
    private Integer maxPlayers;
    private Integer minEntries;
    private Integer maxEntries;
    private Integer maxEntriesPerPlayer;

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

    public Integer getBuyIn() {
        return buyIn;
    }

    public void setBuyIn(Integer buyIn) {
        this.buyIn = buyIn;
    }

    public Integer getRake() {
        return rake;
    }

    public void setRake(Integer rake) {
        this.rake = rake;
    }

    public Boolean getGuaranteed() {
        return guaranteed;
    }

    public void setGuaranteed(Boolean guaranteed) {
        this.guaranteed = guaranteed;
    }

    public Integer getMinPlayers() {
        return minPlayers;
    }

    public void setMinPlayers(Integer minPlayers) {
        this.minPlayers = minPlayers;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(Integer maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public Integer getMinEntries() {
        return minEntries;
    }

    public void setMinEntries(Integer minEntries) {
        this.minEntries = minEntries;
    }

    public Integer getMaxEntries() {
        return maxEntries;
    }

    public void setMaxEntries(Integer maxEntries) {
        this.maxEntries = maxEntries;
    }

    public Integer getMaxEntriesPerPlayer() {
        return maxEntriesPerPlayer;
    }

    public void setMaxEntriesPerPlayer(Integer maxEntriesPerPlayer) {
        this.maxEntriesPerPlayer = maxEntriesPerPlayer;
    }

}
