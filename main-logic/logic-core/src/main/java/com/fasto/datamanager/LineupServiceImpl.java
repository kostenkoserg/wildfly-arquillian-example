package com.fasto.datamanager;

import com.fasto.commons.domain.DomainEvent;
import com.fasto.commons.domain.DomainEvents;
import com.fasto.commons.domain.param.PlayerId;
import com.fasto.commons.domain.param.TournamentId;
import com.fasto.datamanager.dto.EntryDto;
import com.fasto.datamanager.dto.LineupDto;
import com.fasto.datamanager.dto.StockDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.model.EntryEntity;
import com.fasto.datamanager.model.LineupEntity;
import com.fasto.datamanager.model.PlayerEntity;
import com.fasto.datamanager.model.SlateEntity;
import com.fasto.datamanager.model.StockEntity;
import com.fasto.datamanager.model.StockToLineupMapEntity;
import com.fasto.datamanager.model.TournamentEntity;
import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.paging.DefaultFilterFieldToEntityFieldMapper;
import com.fasto.datamanager.paging.PageHelper;
import com.fasto.datamanager.paging.QueryRequest;
import com.fasto.datamanager.paging.QueryResponse;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author nkravchenko
 */
@Remote(LineupService.class)
@Stateless(name = LineupService.LINEUP_SERVICE_NAME)
public class LineupServiceImpl implements LineupService {

  final static Log logger = LogFactory.getLog(LineupServiceImpl.class);

  @PersistenceContext
  EntityManager entityManager;

  @Override
  public ResponseWrapper<LineupDto> getLineups(QueryRequest queryRequest) {

    logger.debug("start get Lineup List");

    PageHelper<LineupEntity> pageHelper = PageHelper.builder(entityManager)
        .withEntity(LineupEntity.class)
        .withQueryRequest(queryRequest)
        .withFilterFieldToEntityFieldMapper(new DefaultFilterFieldToEntityFieldMapper())
        .build();

    QueryResponse queryResponse = pageHelper.buildPage();

    List<LineupEntity> lineupEntities = queryResponse.getData();
    lineupEntities = lineupEntities.stream().filter(l -> !l.isHidden())
        .collect(Collectors.toList());
    logger.debug("finish get Lineup List");
    return new ResponseWrapper<>(Convertible.convert(lineupEntities),
        queryResponse.getTotalPageCount());
  }


  @Override
  @DomainEvent(DomainEvents.LINEUP_ATTR_UPDATE)
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public void lockLineup(long lineupId, @PlayerId long playerId, @TournamentId long tournamentId) {
    LineupEntity lineupEntity = entityManager.find(LineupEntity.class, lineupId);
    if (lineupEntity == null) {
      String errMessage = String.format("lineup with id = %d not found", lineupId);
      logger.error(errMessage);
      throw new RuntimeException(errMessage);
    }
    lineupEntity.setHidden(true);
    entityManager.merge(lineupEntity);
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public Map<Long, Integer> getLineupIdToEntriesCountMap(long playerId, long tournamentId) {
    Query query = entityManager
        .createNativeQuery("select lineup_id, count(distinct entry_id)\n"
            + "from entry\n"
            + "where player_id=:playerId and tournament_id=:tournamentId\n"
            + "group by player_id,  lineup_id;").
            setParameter("playerId", playerId).
            setParameter("tournamentId", tournamentId);
    List<Object[]> resultList = query.getResultList();
    Map<Long, Integer> lineupIdToLineupIdToEntriesCountMap = new HashMap<>();

    for (Object[] obj : resultList) {
      Long lineupId =
          obj[0] instanceof BigInteger ? ((BigInteger) obj[0]).intValue() : (Long) obj[0];
      Integer count =
          obj[1] instanceof BigInteger ? ((BigInteger) obj[1]).intValue() : (Integer) obj[1];
      lineupIdToLineupIdToEntriesCountMap.put(lineupId, count);

    }
    return lineupIdToLineupIdToEntriesCountMap;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public Map<Long, List<EntryDto>> getLineupIdToEntriesListMap(long playerId, long tournamentId) {
    Query query = entityManager
        .createQuery("select e\n"
            + "from EntryEntity e\n"
            + "where e.playerEntity.playerId=:playerId and e.tournamentEntity.tournamentId=:tournamentId")
        .
            setParameter("playerId", playerId).
            setParameter("tournamentId", tournamentId);
    List<EntryEntity> resultList = query.getResultList();
    Map<Long, List<EntryDto>> lineupIdToLineupIdToEntriesListMap = new HashMap<>();

    for (EntryEntity entryEntity : resultList) {
      List<EntryDto> entryEntities = lineupIdToLineupIdToEntriesListMap
          .get(entryEntity.getLineupEntity().getLineupId());
      if (entryEntities == null) {
        entryEntities = new ArrayList();
      }
      entryEntities.add(entryEntity.convert());
      lineupIdToLineupIdToEntriesListMap
          .put(entryEntity.getLineupEntity().getLineupId(), entryEntities);
    }
    return lineupIdToLineupIdToEntriesListMap;
  }

  @Override
  public ResponseWrapper<LineupDto> getLineupsByTournamentIdAndPlayerId(QueryRequest queryRequest,
      long playerId) {
    long tournamentId = ((Number) (queryRequest.getFilterUnits().get(0)).getValue()).longValue();
    TournamentEntity tournamentEntity = entityManager.find(TournamentEntity.class, tournamentId);
    PlayerEntity playerEntity = entityManager.find(PlayerEntity.class, playerId);

    queryRequest.setFilterUnits(Arrays.asList(
        new QueryRequest.FilterUnit("slateEntity", QueryRequest.Operator.EQ,
            tournamentEntity.getSlateEntity()),
        new QueryRequest.FilterUnit("playerEntity", QueryRequest.Operator.EQ, playerEntity)
    ));

    return getLineups(queryRequest);

  }


  @Override
  @DomainEvent(DomainEvents.LINEUP_UPDATE)
  public void updateLineup(List<Long> stockIdsList, @TournamentId long tournamentId,
      @PlayerId long playerId, long lineupId, String name) {

    LineupEntity lineupEntity = entityManager.find(LineupEntity.class, lineupId);
    checkLinupSize(stockIdsList, lineupEntity);

    List<EntryEntity> entryEntities = entityManager
        .createQuery(
            "FROM EntryEntity e WHERE e.lineupEntity.lineupId = :lineupId AND e.tournamentEntity.startTimestamp < :currentDate")
        .setParameter("lineupId", lineupId)
        .setParameter("currentDate", new Date())
        .getResultList();

    if (entryEntities.size() != 0) {
      throw new RuntimeException("Lineup is locked");
    }
    lineupEntity.setName(name);
    updateLineupStockMapping(stockIdsList, lineupEntity);
  }

  @Override
  @DomainEvent(DomainEvents.LINEUP_CREATE)
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public void createLineup(
      @TournamentId long tournamentId,
      @PlayerId long playerId,
      List<Long> stockIdsList, String name) {

    LineupEntity lineupEntity = new LineupEntity();
    lineupEntity.setCreatedTime(new Date());
    lineupEntity.setName(name);

    TournamentEntity tournamentEntity = entityManager.find(TournamentEntity.class, tournamentId);
    if (tournamentEntity == null) {
      String errMessage = String.format("tournament with id = %d not found", tournamentId);
      logger.error(errMessage);
      throw new RuntimeException(errMessage);
    }

    SlateEntity slateEntity = tournamentEntity.getSlateEntity();
    lineupEntity.setSlateEntity(slateEntity);
    checkLinupSize(stockIdsList, lineupEntity);

    PlayerEntity playerEntity = entityManager.find(PlayerEntity.class, playerId);
    if (playerEntity == null) {
      String errMessage = String.format("player with id = %d not found", playerId);
      logger.error(errMessage);
      throw new RuntimeException(errMessage);
    }
    lineupEntity.setPlayerEntity(playerEntity);

    entityManager.persist(lineupEntity);
    entityManager.flush();
    createLineupStockMapping(stockIdsList, lineupEntity);
  }

  private void checkLinupSize(List<Long> stockIdsList, LineupEntity lineupEntity) {
    SlateEntity slateEntity = lineupEntity.getSlateEntity();
    int lineupSize = slateEntity.getLineupSize();
    int stockSize = stockIdsList.size();
    if (lineupSize != stockSize) {
      throw new RuntimeException(String
          .format("lineup stock size is incorrect: instead %s should be %s", stockSize,
              lineupSize));
    }
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  private void createLineupStockMapping(List<Long> stockIds, LineupEntity lineupEntity) {
    for (Long stockId : stockIds) {
      StockEntity stockEntity = entityManager.find(StockEntity.class, stockId);
      StockToLineupMapEntity stockToLineupMapEntity = new StockToLineupMapEntity();
      stockToLineupMapEntity.setLineupEntity(lineupEntity);
      stockToLineupMapEntity.setStockEntity(stockEntity);
      entityManager.persist(stockToLineupMapEntity);
    }
  }

  private void removeLineupStockMappingForLineup(LineupEntity lineupEntity) {
    Query nativeQuery = entityManager
        .createNativeQuery("delete from STOCK_TO_LINEUP_MAP where LINEUP_ID = ?");
    nativeQuery.setParameter(1, lineupEntity.getLineupId());
    nativeQuery.executeUpdate();

  }

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  private void updateLineupStockMapping(List<Long> stockIds, LineupEntity lineupEntity) {
    removeLineupStockMappingForLineup(lineupEntity);
    for (Long stockId : stockIds) {
      StockEntity stockEntity = entityManager.find(StockEntity.class, stockId);
      StockToLineupMapEntity stockToLineupMapEntity = new StockToLineupMapEntity();
      stockToLineupMapEntity.setLineupEntity(lineupEntity);
      stockToLineupMapEntity.setStockEntity(stockEntity);
      entityManager.merge(stockToLineupMapEntity);
    }
  }

  @Override
  public Map<Long, List<StockDto>> getLineupIdStocksMap(List<Long> lineupIds) {

    Map<Long, List<StockDto>> result = new HashMap<>();

    if (lineupIds == null || lineupIds.isEmpty()) {
        return result;
    }
    
    List<Object[]> list = entityManager
        .createQuery(
            "SELECT slm.lineupEntity.lineupId, slm.stockEntity FROM StockToLineupMapEntity slm WHERE slm.lineupEntity.lineupId in (:lineupIds)")
        .setParameter("lineupIds", lineupIds)
        .getResultList();

    for (Object[] obj : list) {
      Long lineupId = ((Number) obj[0]).longValue();
      StockEntity stock = (StockEntity) obj[1];

      if (result.containsKey(lineupId)) {

        result.get(lineupId).add(stock.convert());
      } else {

        List<StockDto> stockSymbols = new ArrayList<>();
        stockSymbols.add(stock.convert());
        result.put(lineupId, stockSymbols);
      }
    }
    return result;
  }


  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public void lockLineups(List<LineupDto> lineups) {
    entityManager.createQuery("update LineupEntity set isLocked=true where lineupId in (:lineupIds)").
        setParameter("lineupIds", lineups.stream().map(l->l.getLineupId()).collect(Collectors.toList())).
        executeUpdate();
  }
}
