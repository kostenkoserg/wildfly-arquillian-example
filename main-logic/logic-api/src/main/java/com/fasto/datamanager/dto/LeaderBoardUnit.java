package com.fasto.datamanager.dto;

import java.io.Serializable;

/**
 *
 * @author kostenko
 */
public class LeaderBoardUnit implements Serializable {

    private Double gain;
    private Long totalTournamentsPlayed;
    private Long totalWin;
    private Long totalCashIns;
    private Long userId;
    private String alias;
    private Integer position;

    public LeaderBoardUnit() {
    }

    public LeaderBoardUnit(Double gain, Long totalTournamentsPlayed, Long totalWin, Long totalCashIns, Long userId, String alias) {
        this.gain = gain;
        this.totalTournamentsPlayed = totalTournamentsPlayed;
        this.totalWin = totalWin;
        this.totalCashIns = totalCashIns;
        this.userId = userId;
        this.alias = alias;
    }

    public Double getGain() {
        return gain;
    }

    public void setGain(Double gain) {
        this.gain = gain;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public Long getTotalTournamentsPlayed() {
        return totalTournamentsPlayed;
    }

    public void setTotalTournamentsPlayed(Long totalTournamentsPlayed) {
        this.totalTournamentsPlayed = totalTournamentsPlayed;
    }

    public Long getTotalWin() {
        return totalWin;
    }

    public void setTotalWin(Long totalWin) {
        this.totalWin = totalWin;
    }

    public Long getTotalCashIns() {
        return totalCashIns;
    }

    public void setTotalCashIns(Long totalCashIns) {
        this.totalCashIns = totalCashIns;
    }
}
