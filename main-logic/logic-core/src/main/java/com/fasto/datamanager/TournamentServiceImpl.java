package com.fasto.datamanager;

import com.fasto.commons.TimeUtil;
import com.fasto.commons.domain.DomainEvent;
import com.fasto.commons.domain.DomainEvents;
import com.fasto.datamanager.dto.EntryDto;
import com.fasto.datamanager.dto.LineupDto;
import com.fasto.datamanager.dto.PayOutDetailsDto;
import com.fasto.datamanager.dto.PayOutDto;
import com.fasto.datamanager.dto.StockDto;
import com.fasto.datamanager.dto.TournamentDto;
import com.fasto.datamanager.dto.TournamentState;
import com.fasto.datamanager.dto.TournamentStatus;
import com.fasto.datamanager.dto.TournamentTemplateDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.model.EntryEntity;
import com.fasto.datamanager.model.LineupEntity;
import com.fasto.datamanager.model.PayOutDetailsEntity;
import com.fasto.datamanager.model.PayOutEntity;
import com.fasto.datamanager.model.PlayerEntity;
import com.fasto.datamanager.model.SlateEntity;
import com.fasto.datamanager.model.StockEntity;
import com.fasto.datamanager.model.StockToLineupMapEntity;
import com.fasto.datamanager.model.TournamentEntity;
import com.fasto.datamanager.model.TournamentTemplateEntity;
import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.datamanager.paging.DefaultFilterFieldToEntityFieldMapper;
import com.fasto.datamanager.paging.PageHelper;
import com.fasto.datamanager.paging.QueryRequest;
import com.fasto.datamanager.paging.QueryResponse;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import javax.ejb.EJB;
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
 * @author kostenko
 */
@Remote(TournamentService.class)
@Stateless(name = TournamentService.TOURNAMENT_SERVICE_NAME)
public class TournamentServiceImpl implements TournamentService {

  final static Log logger = LogFactory.getLog(TournamentServiceImpl.class);

  @PersistenceContext
  EntityManager entityManager;

  @EJB
  StockQuotesService quotesService;

  @EJB
  StockService stockService;

  @EJB
  EntryService entryService;

  @EJB
  LineupService lineupService;

  @Override
  @DomainEvent(DomainEvents.TOURNAMENT_UPDATE)
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public void setTournamentStatus(long tournamentId, TournamentStatus status) {

    TournamentEntity entity = entityManager.find(TournamentEntity.class, tournamentId);
    entity.setStatus(status);
  }


  private void setAdditionalParameters(long playerId, Collection<TournamentDto> tournamentDtoList) {
    Set<Long> tournamentIds = tournamentDtoList.stream().
        map(t -> t.getTournamentId()).
        collect(Collectors.toSet());

    int totalEntries = getTotalEntries(tournamentIds);
    Map<Long, Integer> tournamentIdToMyEntriesMap = getMyEntriesCount(playerId, tournamentIds);
    int rank = getBestRank(playerId);

    tournamentDtoList.stream().forEach(
        c -> {
          long totalPayout = getTotalPayout(c.getTournamentId(), playerId);
          c.setMyTotalPayout(totalPayout);
          c.setEntries(totalEntries);
          c.setRank(rank);
        });
  }

  @Override
  public TournamentState generateTournamentState(long tournamentId) {

    List<EntryDto> entries = entryService.getEntriesByTournamentId(tournamentId);

    TournamentEntity tournamentEntity = entityManager.find(TournamentEntity.class, tournamentId);
    TournamentTemplateDto template =
        tournamentEntity.getTournamentTemplateEntity() == null ? new TournamentTemplateEntity()
            .convert() : tournamentEntity.getTournamentTemplateEntity().convert();
    long slateId = tournamentEntity.getSlateEntity().getSlateId();

    TournamentDto tournament = tournamentEntity.convert();
    Map<Long, Set<StockDto>> stocksBySlateIdMap = stockService
        .getStocksBySlateIds(Arrays.asList(slateId));

    List<StockDto> slateStocks = new ArrayList<>(stocksBySlateIdMap.get(slateId));
    List<PayOutDetailsDto> details = getPayOutDetailsByPayOutId(
        tournament.getTournamentTemplateDto().getPayOutDto().getId());
    Map<Long, List<StockDto>> lnpToStocks = lineupService.getLineupIdStocksMap(
        entries.stream().map(e -> e.getLineupDto().getLineupId()).collect(Collectors.toList()));
    List<LineupDto> lineups = entries.stream().map(e -> e.getLineupDto()).distinct()
        .collect(Collectors.toList());

    float summPayIn = template.getBuyIn() * entries.size();
    float sumRake = summPayIn * template.getRake() / 100;
    float awardPool = summPayIn - sumRake;

    logger.info(String
        .format("Sum pay ins = %s, sum rake = %s, award pool = %s", summPayIn, sumRake, awardPool));

    TournamentState tournamentState = new TournamentState();
    tournamentState.setSumPayIn(summPayIn);
    tournamentState.setSumRake(sumRake);
    tournamentState.setAwardPool(awardPool);

    tournamentState.setTournament(tournament);
    tournamentState.setEntries(entries);
    tournamentState.setSlateStocks(slateStocks);
    tournamentState.setPayOutDetails(details);
    tournamentState.setLineupToStocks(lnpToStocks);
    tournamentState.setLineups(lineups);

    tournamentState.setEntries(entries);
    
    Date startDate = tournament.getStartTimestamp();
    Date endDate = TimeUtil.plusSeconds(startDate, tournament.getDuration() * 60);

    tournamentState.setInitialQuotes(quotesService.getQuotesBetween(startDate, TimeUtil.plusSeconds(startDate, 5), slateId));
    tournamentState.setLastQuotes(quotesService.getQuotesBetween(endDate, TimeUtil.plusSeconds(endDate, 5), slateId));
    
    return tournamentState;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  private long getTotalPayout(long tournamentId, long playerId) {
    Query query = entityManager.createQuery(
        "SELECT sum(e.payout) "
            + "FROM EntryEntity e "
            + "WHERE e.tournamentEntity.tournamentId = :tournamentId"
            + " AND e.playerEntity.playerId = :playerId").
        setParameter("tournamentId", tournamentId).
        setParameter("playerId", playerId);
    Object singleResult = query.getSingleResult();

    List<Long> resultList = query.getResultList();
    return resultList.get(0) == null ? 0 : resultList.get(0);
  }

  @Override
  public int getCurrentPlayers(long tournamentId) {
    Query query = entityManager.createQuery(
        "SELECT count(distinct e.playerEntity.id) "
            + "FROM EntryEntity e "
            + "WHERE e.tournamentEntity.tournamentId = :tournamentId").
        setParameter("tournamentId", tournamentId);
    Object singleResult = query.getSingleResult();
    return ((Long) (Optional.ofNullable(singleResult).isPresent() ? singleResult : 0))
        .intValue();
  }

  private int getBestRank(long playerId) {
    Query query = entityManager.createQuery(
        "SELECT min(e.rank) "
            + "FROM EntryEntity e "
            + "WHERE e.playerEntity.id = :playerId").
        setParameter("playerId", playerId);
    Object singleResult = query.getSingleResult();
    return ((Integer) (Optional.ofNullable(singleResult).isPresent() ? singleResult : 0))
        .intValue();
  }

  private int getTotalEntries(Set<Long> tournamentIds) {
    if (tournamentIds == null || tournamentIds.size() == 0) {
      return 0;
    }
    Query query = entityManager.createNativeQuery(
        "SELECT count(*) " +
            "FROM tournament t " +
            "JOIN slate s on t.slate_id =s.slate_id " +
            "JOIN lineup l on l.slate_id=s.slate_id " +
            "WHERE t.tournament_id in (:tournamentIds)").
        setParameter("tournamentIds", tournamentIds);
    Object singleResult = query.getSingleResult();
    return ((BigInteger) (Optional.ofNullable(singleResult).isPresent() ? singleResult : 0))
        .intValue();
  }

  @Override
  public int getMyEntriesCount(long playerId, Long tournamentId) {
    Map<Long, Integer> myEntriesCount = getMyEntriesCount(playerId,
        new HashSet(Arrays.asList(tournamentId)));
    return myEntriesCount.get(tournamentId);
  }

  private Map<Long, Integer> getMyEntriesCount(long playerId,
      Set<Long> tournamentIds) {
    Map<Long, Integer> tournamentIdToMyEntriesSize = new HashMap<>();
    for (Long tournamentId : tournamentIds) {
      Query query = entityManager.createQuery(
          "SELECT count(e.tournamentEntity) "
              + "FROM EntryEntity e "
              + "WHERE e.playerEntity.id = :playerId AND e.tournamentEntity.tournamentId = :tournamentId")
          .
              setParameter("playerId", playerId).
              setParameter("tournamentId", tournamentId);
      Object singleResult = query.getSingleResult();
      int myEntriesSize = ((Long) (Optional.ofNullable(singleResult).isPresent() ? singleResult
          : 0))
          .intValue();
      tournamentIdToMyEntriesSize.put(tournamentId, myEntriesSize);
    }
    return tournamentIdToMyEntriesSize;
  }

  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public Long getTotalCount() {
    Query query = entityManager.createQuery("select count(t) " +
        "from TournamentEntity t");
    Object singleResult = query.getSingleResult();
    return ((Long) (Optional.ofNullable(singleResult).isPresent() ? singleResult : 0)).longValue();
  }

  @Override
  public TournamentDto getTournamentDetails(Long tournamentId) {
    if (tournamentId == null) {
      String errMessage = "tournamentId should be specified";
      logger.error(errMessage);
      throw new RuntimeException(errMessage);
    }
    TournamentEntity tournamentEntity
        = entityManager.find(TournamentEntity.class, tournamentId);

    return tournamentEntity.convert();

  }

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public long updateTournament(String name, Long tournamentId, Long templateId, Long slateId,
      Long payOutId, String type, String structure, Integer duration, Date startTime,
      Date visibleInLobbyTime) {

    TournamentEntity entity = entityManager.find(TournamentEntity.class, tournamentId);
    entity.setName(name);
    entity.setStatus(TournamentStatus.NEW);
    TournamentTemplateEntity tournamentTemplateEntity = entityManager
        .find(TournamentTemplateEntity.class, templateId);
    if (tournamentTemplateEntity != null) {
      tournamentTemplateEntity.setDuration(duration);
      tournamentTemplateEntity.setPayOutEntity(entityManager.find(PayOutEntity.class, payOutId));
      entity.setTournamentTemplateEntity(tournamentTemplateEntity);
    }
    entity.setSlateEntity(entityManager.find(SlateEntity.class, slateId));

    entity.setTournamentType(type);
    entity.setTournamentStructure(structure);
    entity.getTournamentTemplateEntity().setDuration(duration);
    entity.setStartTimestamp(startTime);
    entity.setVisibleInLobbyTimestamp(visibleInLobbyTime);

    entityManager.merge(entity);

    return entity.getTournamentId();
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public void removeTournamentById(Long tournamentId) {
    TournamentEntity entity = entityManager.find(TournamentEntity.class, tournamentId);
    entityManager.remove(entity);
  }

  @Override
  public int getCurrentEntries(long tournamentId) {
    Query query = entityManager.createQuery(
        "SELECT count(e.tournamentEntity.tournamentId) "
            + "FROM EntryEntity e "
            + "WHERE e.tournamentEntity.tournamentId = :tournamentId").
        setParameter("tournamentId", tournamentId);
    Object singleResult = query.getSingleResult();
    return ((Long) (Optional.ofNullable(singleResult).isPresent() ? singleResult : 0))
        .intValue();
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public long createTournament(String name, long templateId, long slateId, long payOutId,
      String type, String structure, int duration, Date startTimestamp,
      Date visibleInLobbyTimestamp) {

    TournamentEntity entity = new TournamentEntity();
    entity.setName(name);
    entity.setStatus(TournamentStatus.NEW);
    TournamentTemplateEntity tournamentTemplateEntity = entityManager
        .find(TournamentTemplateEntity.class, templateId);

    if (tournamentTemplateEntity != null) {
      tournamentTemplateEntity.setDuration(duration);
      tournamentTemplateEntity.setPayOutEntity(entityManager.find(PayOutEntity.class, payOutId));
      entity.setTournamentTemplateEntity(tournamentTemplateEntity);
    }

    entity.setSlateEntity(entityManager.find(SlateEntity.class, slateId));
    entity.setTournamentType(type);
    entity.setTournamentStructure(structure);
    entity.setStartTimestamp(startTimestamp);
    entity.setVisibleInLobbyTimestamp(visibleInLobbyTimestamp);

    entityManager.persist(entity);

    return entity.getTournamentId();
  }

  @Override
  @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
  public long createTournamentTemplate(String name, int buyIn, int rake, boolean guaranteed,
      int minPlayers, int maxPlayers, int minEntries, int maxEntries, int maxEntriesPerPlayer) {

    TournamentTemplateEntity entity = new TournamentTemplateEntity();
    entity.setName(name);
    entity.setBuyIn(buyIn);
    entity.setRake(rake);
    entity.setGuaranteed(guaranteed);
    entity.setMinPlayers(minPlayers);
    entity.setMaxPlayers(maxPlayers);
    entity.setMinEntries(minEntries);
    entity.setMaxEntries(maxEntries);
    entity.setMaxEntriesPerPlayer(maxEntriesPerPlayer);

    entityManager.persist(entity);

    return entity.getTournamentTemplateId();
  }

  @Override
  public long createPayOut(String name) {
    PayOutEntity entity = new PayOutEntity();
    entity.setName(name);

    entityManager.persist(entity);
    return entity.getId();
  }

  @Override
  public long createPayOutDetails(long payOutId, int startPlace, int endPlace, float percentage) {
    PayOutDetailsEntity entity = new PayOutDetailsEntity();
    entity.setPayOutEntity(entityManager.find(PayOutEntity.class, payOutId));
    entity.setStartPlace(startPlace);
    entity.setEndPlace(endPlace);
    entity.setPercentage(percentage);

    entityManager.persist(entity);
    return entity.getId();
  }

  @Override
  public long createLineUp(String name, long playerId, long slateId) {
    LineupEntity entity = new LineupEntity();
    entity.setName(name);
    entity.setPlayerEntity(entityManager.find(PlayerEntity.class, playerId));
    entity.setSlateEntity(entityManager.find(SlateEntity.class, slateId));

    entityManager.persist(entity);
    return entity.getLineupId();
  }

  @Override
  public long createEntry(long playerId, long tournamentId, long lineUpId) {
    EntryEntity entity = new EntryEntity();
    entity.setPlayerEntity(entityManager.find(PlayerEntity.class, playerId));
    entity.setTournamentEntity(entityManager.find(TournamentEntity.class, tournamentId));
    entity.setLineupEntity(entityManager.find(LineupEntity.class, lineUpId));

    entityManager.persist(entity);
    return entity.getEntryId();
  }

  @Override
  public long createStockToLineUpMap(long stockId, long lineUpId) {
    StockToLineupMapEntity entity = new StockToLineupMapEntity();
    entity.setStockEntity(entityManager.find(StockEntity.class, stockId));
    entity.setLineupEntity(entityManager.find(LineupEntity.class, lineUpId));

    entityManager.persist(entity);
    return entity.getId();
  }

  @Override
  public QueryResponse<TournamentDto> getTournaments(QueryRequest queryRequest, long playerId) {
    PageHelper pageHelper = PageHelper.builder(entityManager)
        .withEntity(TournamentEntity.class)
        .withQueryRequest(queryRequest)
        .withFilterFieldToEntityFieldMapper(new DefaultFilterFieldToEntityFieldMapper())
        .build();

    QueryResponse queryResponse = pageHelper.buildPage();
    List<TournamentDto> data = Convertible.convert(queryResponse.getData());
    setAdditionalParameters(playerId, data);
    queryResponse.setData(data);
    return queryResponse;
  }


  @Override
  public QueryResponse<TournamentDto> getTournaments(QueryRequest queryRequest) {

    PageHelper pageHelper = PageHelper.builder(entityManager, "graph.tournament.tournamentTemplate")
        .withEntity(TournamentEntity.class)
        .withQueryRequest(queryRequest)
        .withFilterFieldToEntityFieldMapper(new DefaultFilterFieldToEntityFieldMapper())
        .build();

    QueryResponse queryResponse = pageHelper.buildPage();
    List<TournamentDto> data = Convertible.convert(queryResponse.getData());
    queryResponse.setData(data);
    return queryResponse;
  }

  @Override
  public ResponseWrapper<TournamentTemplateDto> getTournamentTemplates(QueryRequest queryRequest) {

    PageHelper pageHelper = PageHelper.builder(entityManager)
        .withEntity(TournamentTemplateEntity.class)
        .withQueryRequest(queryRequest)
        .withFilterFieldToEntityFieldMapper(new DefaultFilterFieldToEntityFieldMapper())
        .build();

    QueryResponse queryResponse = pageHelper.buildPage();

    return new ResponseWrapper<>(Convertible.convert(queryResponse.getData()),
        queryResponse.getTotalPageCount());
  }

  @Override
  public ResponseWrapper<PayOutDto> getPayouts(QueryRequest queryRequest) {
    PageHelper pageHelper = PageHelper.builder(entityManager)
        .withEntity(PayOutEntity.class)
        .withQueryRequest(queryRequest)
        .withFilterFieldToEntityFieldMapper(new DefaultFilterFieldToEntityFieldMapper())
        .build();

    QueryResponse queryResponse = pageHelper.buildPage();

    return new ResponseWrapper<>(Convertible.convert(queryResponse.getData()),
        queryResponse.getTotalPageCount());
  }

  @Override
  public List<PayOutDetailsDto> getPayOutDetailsByPayOutId(long payOutId) {

    List<PayOutDetailsEntity> details =
        entityManager.createQuery("FROM PayOutDetailsEntity t WHERE t.payOutEntity.id = :id")
            .setParameter("id", payOutId)
            .getResultList();

    return Convertible.convert(details);
  }

  @Override
  public List<Long> getTournamentIdsByPlayerAndStatus(long playerId, TournamentStatus status) {
      
      String sql = "SELECT DISTINCT e.tournamentEntity.tournamentId FROM EntryEntity e WHERE e.playerEntity.playerId = :playerId AND e.tournamentEntity.status = :status";
      
      List<Long> ids = entityManager.createQuery(sql)
            .setParameter("playerId", playerId)
            .setParameter("status", status)
            .getResultList();
      
      return ids;
  }

    @Override
    public Map<Long, List<Integer>> getEntriesGroupedDataByPlayerAndTournaments(long playerId, List<Long> tournamentIds) {
        
        Map<Long, List<Integer>> result = new HashMap<>();
        
        if (tournamentIds == null || tournamentIds.isEmpty()) {
            return result;
        }
        
        String sql = "SELECT e.tournamentEntity.tournamentId, COUNT(e.entryId), SUM(e.payout) FROM EntryEntity e "
                + "WHERE e.playerEntity.playerId = :playerId AND e.tournamentEntity.tournamentId IN (:tournamentIds) "
                + "GROUP BY e.tournamentEntity.tournamentId";

        List<Object[]> queryResultList = entityManager.createQuery(sql)
            .setParameter("playerId", playerId)
            .setParameter("tournamentIds", tournamentIds)
            .getResultList();
        
        for (Object[] queryResult : queryResultList) {
            
            Number tournamentId = (Number) queryResult[0];
            Number entriesCount = (Number) queryResult[1];
            Number totalPayout  = (Number) queryResult[2];
            
            // @TODO: Think about ussage separate object instead of List
            result.put(tournamentId.longValue(), Arrays.asList(entriesCount.intValue(), totalPayout.intValue()));
        }
        return result;
    }
  
}
