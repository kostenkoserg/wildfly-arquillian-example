package com.fasto.datamanager;

import com.fasto.datamanager.dto.LineupDto;
import com.fasto.datamanager.dto.StockDto;
import com.fasto.datamanager.dto.StockQuotesDto;
import com.fasto.datamanager.model.LineupEntity;
import com.fasto.datamanager.model.PlayerEntity;
import com.fasto.datamanager.model.SlateEntity;
import com.fasto.datamanager.model.StockEntity;
import com.fasto.datamanager.model.StockExchangeEntity;
import com.fasto.datamanager.model.StockQuotesEntity;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class EntityCreationTest {


  EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("fastoDSTest");
  EntityManager entityManager = entityManagerFactory.createEntityManager();
  PlayerServiceImpl playerService = new PlayerServiceImpl();
  TournamentServiceImpl tournamentService = new TournamentServiceImpl();
  EntryServiceImpl entryService = new EntryServiceImpl();
  LineupServiceImpl lineupService = new LineupServiceImpl();

  @Before
  public void init() {
    playerService.entityManager = entityManager;
    tournamentService.entityManager = entityManager;
    entryService.entityManager = entityManager;
    lineupService.entityManager = entityManager;
  }

  @Test//todo
  public void entityCreation() {
    Query lineupQuery = entityManager.createQuery("select le from LineupEntity le ");
    lineupQuery.getResultList();
  }


  @Test
  public void entityStockQuotesCreation() {
    entityManager.getTransaction().begin();
    //create stockEntity to persist
    StockEntity stockEntity = new StockEntity();
    stockEntity.setStockId(1);
    stockEntity.setName("stock name");
    stockEntity.setSymbol("symbol");
    stockEntity = entityManager.merge(stockEntity);
    entityManager.getTransaction().commit();
    long originalStockEntityId = stockEntity.getStockId();

    entityManager.getTransaction().begin();
    //create stockQuotesDto
    StockQuotesDto stockQuotesDto = new StockQuotesDto();
    stockQuotesDto.setValue(new BigDecimal(12));
    stockQuotesDto.setTradeDate(new Date());
    stockQuotesDto.setEventDate(new Date());

    //create stockDto => not use detach stockEntity above
    StockDto stockDto = new StockDto();
    stockDto.setStockId(originalStockEntityId);
    stockQuotesDto.setStockDto(stockDto);

    StockQuotesEntity stockQuotesEntity = StockQuotesEntity.convertToEntity(stockQuotesDto);
    StockEntity stockEntityFromDto = StockEntity.convertToEntity(stockDto);
    stockQuotesEntity.setStockEntity(stockEntityFromDto);

    //persist stockQuotesEntity
    stockQuotesEntity = entityManager.merge(stockQuotesEntity);
    entityManager.getTransaction().commit();
    Assert.assertNotNull(stockQuotesEntity.getStockEntity());
    Assert.assertEquals(stockQuotesEntity.getStockEntity().getStockId(), originalStockEntityId);
  }


  @Test
  public void createLineup_checkLock() {
    entityManager.getTransaction().begin();
    //create player
    PlayerEntity playerEntity = new PlayerEntity();
    playerEntity.setBalance(100);
    playerEntity.setRating(10);
    entityManager.persist(playerEntity);

    SlateEntity slateEntity = new SlateEntity();
    slateEntity.setEnabled(true);
    slateEntity.setName("slate1");

    StockExchangeEntity stockExchangeEntity = new StockExchangeEntity();
    stockExchangeEntity.setCloseTimeUtc(new Date());
    stockExchangeEntity.setEnabled(true);
    stockExchangeEntity.setShortName("se1");
    stockExchangeEntity.setName("se1");
    stockExchangeEntity.setStartTimeUtc(new Date());
    entityManager.persist(stockExchangeEntity);
    slateEntity.setStockExchangeEntity(stockExchangeEntity);
    entityManager.persist(slateEntity);

    entityManager.getTransaction().commit();

    //create tournamentDto
    entityManager.getTransaction().begin();
    LineupEntity lineupEntity = new LineupEntity();
    lineupEntity.setName("lineup1");
    lineupEntity.setHidden(false);
    lineupEntity.setLocked(false);
    lineupEntity.setPlayerEntity(playerEntity);
    lineupEntity.setSlateEntity(slateEntity);
    entityManager.persist(lineupEntity);
    entityManager.getTransaction().commit();

    entityManager.getTransaction().begin();
    List<LineupDto> lineups = Arrays.asList(lineupEntity.convert());
    lineupService.lockLineups(lineups);
    entityManager.flush();
    entityManager.getTransaction().commit();
    entityManager.refresh(lineupEntity);
    entityManager.getTransaction().begin();
    LineupEntity changedEntity = entityManager.find(LineupEntity.class, 1l);
    Assert.assertTrue(changedEntity.isLocked());
    entityManager.getTransaction().commit();

  }


}
