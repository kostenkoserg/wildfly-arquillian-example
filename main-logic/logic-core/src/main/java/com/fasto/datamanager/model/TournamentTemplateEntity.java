package com.fasto.datamanager.model;

import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.dto.TournamentTemplateDto;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "TOURNAMENT_TEMPLATE")
public class TournamentTemplateEntity implements Convertible<TournamentTemplateDto> {

  @Id
  @Column(name = "TOURNAMENT_TEMPLATE_ID", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long tournamentTemplateId;

  @ManyToOne
  @JoinColumn(name = "PAYOUT_ID", nullable = true)
  private PayOutEntity payOutEntity;

  @Column(name = "BUYIN", nullable = true)
  private int buyIn;

  @Column(name = "RAKE", nullable = true)
  private int rake;

  @Column(name = "GUARANTEED", nullable = true)
  private boolean guaranteed;

  @Column(name = "NAME", nullable = true, length = 255)
  private String name;

  @Column(name = "MIN_PLAYERS", nullable = true)
  private int minPlayers;

  @Column(name = "MAX_PLAYERS", nullable = true)
  private int maxPlayers;

  @Column(name = "MIN_ENTRIES", nullable = true)
  private int minEntries;

  @Column(name = "MAX_ENTRIES", nullable = true)
  private int maxEntries;

  @Column(name = "MAX_ENTRIES_PER_PLAYER", nullable = true)
  private int maxEntriesPerPlayer;

  @Column(name = "DURATION", nullable = true)
  private int duration;
  
  @ManyToOne
  @JoinColumn(name = "SCORER_ID")
  private ScorerEntity scorerEntity;

  @Column(name = "STARTING_BALANCE", nullable = true)
  private int startingBalance;

  public long getTournamentTemplateId() {
    return tournamentTemplateId;
  }

  public void setTournamentTemplateId(long tournamentTemplateId) {
    this.tournamentTemplateId = tournamentTemplateId;
  }

  public PayOutEntity getPayOutEntity() {
    return payOutEntity;
  }

  public void setPayOutEntity(PayOutEntity payOutEntity) {
    this.payOutEntity = payOutEntity;
  }

  public int getBuyIn() {
    return buyIn;
  }

  public void setBuyIn(int buyin) {
    this.buyIn = buyin;
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

  public ScorerEntity getScorerEntity() {
    return scorerEntity;
  }

  public void setScorerEntity(ScorerEntity scorerEntity) {
    this.scorerEntity = scorerEntity;
  }

  public void setStartingBalance(int startingBalance) {
    this.startingBalance = startingBalance;
  }


  public int getStartingBalance() {
    return startingBalance;
  }

  public void setStartingBalance(Integer startingBalance) {
    this.startingBalance = startingBalance;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TournamentTemplateEntity that = (TournamentTemplateEntity) o;
    return tournamentTemplateId == that.tournamentTemplateId &&
        Objects.equals(payOutEntity, that.payOutEntity) &&
        Objects.equals(buyIn, that.buyIn) &&
        Objects.equals(rake, that.rake) &&
        Objects.equals(guaranteed, that.guaranteed) &&
        Objects.equals(name, that.name) &&
        Objects.equals(minPlayers, that.minPlayers) &&
        Objects.equals(maxPlayers, that.maxPlayers) &&
        Objects.equals(minEntries, that.minEntries) &&
        Objects.equals(maxEntries, that.maxEntries) &&
        Objects.equals(maxEntriesPerPlayer, that.maxEntriesPerPlayer) &&
        Objects.equals(duration, that.duration) &&
        Objects.equals(scorerEntity, that.scorerEntity) &&
        Objects.equals(startingBalance, that.startingBalance);
  }

  @Override
  public int hashCode() {

    return Objects
        .hash(tournamentTemplateId, payOutEntity, buyIn, rake, guaranteed, name, minPlayers, maxPlayers,
            minEntries, maxEntries, maxEntriesPerPlayer, duration, scorerEntity, startingBalance);
  }

  @Override
  public TournamentTemplateDto convert() {
    TournamentTemplateDto dto = new TournamentTemplateDto();
    dto.setTournamentTemplateId(this.tournamentTemplateId);
    dto.setPayOutDto(this.payOutEntity == null ? new PayOutEntity().convert() : this.payOutEntity.convert());
    dto.setBuyIn(this.buyIn);
    dto.setRake(this.rake);
    dto.setGuaranteed(this.guaranteed);
    dto.setName(this.name);
    dto.setMinPlayers(this.minPlayers);
    dto.setMaxPlayers(this.maxPlayers);
    dto.setMinEntries(this.minEntries);
    dto.setMaxEntries(this.maxEntries);
    dto.setMaxEntriesPerPlayer(this.maxEntriesPerPlayer);
    dto.setDuration(this.duration);

    if (scorerEntity != null) {
      dto.setScorerDto(scorerEntity.convert());
      dto.setStartingBalance(this.startingBalance);
    }

    return dto;
  }

}