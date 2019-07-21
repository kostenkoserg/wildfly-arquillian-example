package com.fasto.datamanager.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author kostenko
 */
public class TournamentState implements Serializable {

    private float sumPayIn;
    private float sumRake;
    private float awardPool;

    private TournamentDto tournament;
    private List<StockQuotesDto> initialQuotes = new ArrayList<>();
    private List<StockQuotesDto> lastQuotes = new ArrayList<>();
    private List<EntryDto> entries = new ArrayList<>();
    private List<PayOutDetailsDto> payOutDetails = new ArrayList<>();
    private Map<Long, List<StockDto>> lineupToStocks = new HashMap();
    private List<StockDto> slateStocks = new ArrayList<>();
    private List<LineupDto> lineups = new ArrayList<>();

    public TournamentDto getTournament() {
        return tournament;
    }

    public void setTournament(TournamentDto tournament) {
        this.tournament = tournament;
    }

    public List<StockQuotesDto> getInitialQuotes() {
        return initialQuotes;
    }

    public void setInitialQuotes(List<StockQuotesDto> initialQuotes) {
        this.initialQuotes = initialQuotes;
    }

    public List<StockQuotesDto> getLastQuotes() {
        return lastQuotes;
    }

    public void setLastQuotes(List<StockQuotesDto> lastQuotes) {
        this.lastQuotes = lastQuotes;
    }

    public List<EntryDto> getEntries() {
        return entries;
    }

    public void setEntries(List<EntryDto> entries) {
        this.entries = entries;
    }

    public List<PayOutDetailsDto> getPayOutDetails() {
        return payOutDetails;
    }

    public void setPayOutDetails(List<PayOutDetailsDto> payOutDetails) {
        this.payOutDetails = payOutDetails;
    }

    public float getSumPayIn() {
        return sumPayIn;
    }

    public void setSumPayIn(float sumPayIn) {
        this.sumPayIn = sumPayIn;
    }

    public float getSumRake() {
        return sumRake;
    }

    public void setSumRake(float sumRake) {
        this.sumRake = sumRake;
    }

    public float getAwardPool() {
        return awardPool;
    }

    public void setAwardPool(float awardPool) {
        this.awardPool = awardPool;
    }

    public Map<Long, List<StockDto>> getLineupToStocks() {
        return lineupToStocks;
    }

    public void setLineupToStocks(Map<Long, List<StockDto>> lineupToStocks) {
        this.lineupToStocks = lineupToStocks;
    }

    public List<StockDto> getSlateStocks() {
        return slateStocks;
    }

    public void setSlateStocks(List<StockDto> slateStocks) {
        this.slateStocks = slateStocks;
    }

    public List<LineupDto> getLineups() {
        return lineups;
    }

    public void setLineups(List<LineupDto> lineups) {
        this.lineups = lineups;
    }
}
