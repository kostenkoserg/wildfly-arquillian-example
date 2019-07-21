package com.fasto.datamanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;

public class PlayerDto implements Serializable {
    private static final long serialVersionUID = 1488362101336914710L;

    private long playerId;
    private String alias;
    private int operatorId;//? check is it necessary
    private String playerForeignId;//why we need that column?
    private Date timeCreated;
    private int rating;
    private int balance;
    private String password;
    private String email;
    
    private List<Long> entryIDs = new ArrayList<>();
    private List<Long> lineupIDs = new ArrayList<>();

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(int operatorId) {
        this.operatorId = operatorId;
    }

    public String getPlayerForeignId() {
        return playerForeignId;
    }

    public void setPlayerForeignId(String playerForeignId) {
        this.playerForeignId = playerForeignId;
    }

    public Date getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(Date timeCreated) {
        this.timeCreated = timeCreated;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public List<Long> getEntryIDs() {
        return entryIDs;
    }

    public void setEntryIDs(List<Long> entryIDs) {
        this.entryIDs = entryIDs;
    }

    public List<Long> getLineupIDs() {
        return lineupIDs;
    }

    public void setLineupIDs(List<Long> lineupIDs) {
        this.lineupIDs = lineupIDs;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
