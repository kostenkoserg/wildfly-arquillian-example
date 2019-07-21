package com.fasto.datamanager;

import com.fasto.commons.domain.DomainEvent;
import com.fasto.commons.domain.DomainEvents;
import com.fasto.commons.domain.param.PlayerId;
import com.fasto.commons.domain.param.TournamentId;
import com.fasto.datamanager.dto.EntryDto;
import com.fasto.datamanager.dto.LeaderBoardState;
import com.fasto.datamanager.dto.LeaderBoardUnit;
import com.fasto.datamanager.model.EntryEntity;
import com.fasto.datamanager.model.LineupEntity;
import com.fasto.datamanager.model.PlayerEntity;
import com.fasto.datamanager.model.TournamentEntity;
import com.fasto.datamanager.model.converters.Convertible;
import com.fasto.wallet.WalletService;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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
 * @author nkravchenko
 */
@Remote(EntryService.class)
@Stateless(name = EntryService.ENTRY_SERVICE_NAME)
public class EntryServiceImpl implements EntryService {

    final static Log logger = LogFactory.getLog(EntryServiceImpl.class);

    @PersistenceContext
    EntityManager entityManager;

    @EJB
    TournamentService tournamentService;

    @EJB(lookup = WalletService.WALLET_SERVICE_JNDI)
    WalletService walletService;

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public List<EntryDto> getEntriesByTournamentId(Long tournamentId) {
        logger.debug("start getEntriesByTournamentId");
        Query q = entityManager
                .createQuery("FROM EntryEntity e WHERE e.tournamentEntity.tournamentId = :tournamentId");
        q.setParameter("tournamentId", tournamentId);

        List<EntryEntity> entries = q.getResultList();
        logger.debug("finish getEntries by tournamentId");
        return Convertible.convert(entries);
    }

    @Override
    public List<EntryDto> getEntriesByTournamentIdAndPlayerId(long playerId, long tournamentId) {
        logger.debug("start getEntriesByTournamentId");
        Query q = entityManager.createQuery(
                "FROM EntryEntity e WHERE e.tournamentEntity.tournamentId = :tournamentId AND e.playerEntity.playerId = :playerId");
        q.setParameter("tournamentId", tournamentId);
        q.setParameter("playerId", playerId);

        List<EntryEntity> entries = q.getResultList();
        logger.debug("finish getEntries by tournamentId");
        return Convertible.convert(entries);
    }

    @Override
    @DomainEvent(DomainEvents.ENTRY_CREATE)
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void updateEntries(Long entries, long lineupId, @TournamentId long tournamentId,
            @PlayerId long playerId) throws Exception {

        LineupEntity lineupEntity = entityManager.find(LineupEntity.class, lineupId);
        TournamentEntity tournamentEntity = entityManager.find(TournamentEntity.class, tournamentId);
        PlayerEntity playerEntity = entityManager.find(PlayerEntity.class, playerId);

        List<EntryEntity> entryEntityList = entityManager
                .createQuery(
                        "FROM EntryEntity e WHERE e.lineupEntity.lineupId = :lineupId AND e.tournamentEntity.tournamentId = :tournamentId")
                .setParameter("lineupId", lineupId)
                .setParameter("tournamentId", tournamentId)
                .getResultList();

        validateEntry(entries, playerId, tournamentEntity);

        int size = 0;

        int buyIn = tournamentEntity.getTournamentTemplateEntity().getBuyIn();

        if (entries < 0) {
            size = removeEntries(entries, entryEntityList);
            walletService.moneyTransaction(playerId, entries.intValue() * buyIn, WalletService.TransactionType.CREDIT);
        } else {
            size = createEntries(entries, entryEntityList, lineupEntity, tournamentEntity, playerEntity);
            walletService.moneyTransaction(playerId, entries.intValue() * buyIn, WalletService.TransactionType.DEBIT);
        }
        logger.info(">>> actual added " + size);
        logger.info(">>> currentEntries added " + tournamentEntity.getCurrentEntries());
        tournamentEntity.setCurrentEntries(tournamentService.getCurrentEntries(tournamentId));
        tournamentEntity.setCurrentPlayers(tournamentService.getCurrentPlayers(tournamentId));
        entityManager.merge(tournamentEntity);
    }

    private int removeEntries(long entries, List<EntryEntity> entryEntityList) {
        int size = entryEntityList.size();
        int removedEntriesSize = 0;
        for (int i = 0; i < Math.abs(entries); i++, size--, removedEntriesSize++) {
            if (size == 0) {
                break;
            }
            entityManager.remove(entryEntityList.get(i));
        }
        return removedEntriesSize;
    }

    private int createEntries(long entries,
            List<EntryEntity> entryEntityList,
            LineupEntity lineupEntity,
            TournamentEntity tournamentEntity, PlayerEntity playerEntity) {

        int size = entryEntityList.size();
        for (int i = 0; i < entries; i++, size++) {
            logger.debug("start create Entry");
            EntryEntity entryEntity = new EntryEntity();
            entryEntity.setPlayerEntity(playerEntity);
            entryEntity.setTournamentEntity(tournamentEntity);
            entryEntity.setLineupEntity(lineupEntity);

            entityManager.persist(entryEntity);

            logger.debug("finish create Entry");
        }
        return size;
    }

    private void validateEntry(long entries, long playerId, TournamentEntity tournamentEntity)
            throws Exception {

        int myEntries = ((Number) entityManager
                .createQuery(
                        "SELECT count(e.entryId) FROM EntryEntity e WHERE e.playerEntity.playerId = :playerId AND e.tournamentEntity.tournamentId = :tournamentId")
                .setParameter("playerId", playerId)
                .setParameter("tournamentId", tournamentEntity.getTournamentId())
                .getSingleResult()).intValue();

        int currentPlayers = tournamentEntity.getCurrentPlayers();
        int currentEntries = tournamentEntity.getCurrentEntries();

        int maxEntries = tournamentEntity.getTournamentTemplateEntity().getMaxEntries();
        int maxEntriesPerPlayer = tournamentEntity.getTournamentTemplateEntity()
                .getMaxEntriesPerPlayer();
        int maxPlayers = tournamentEntity.getTournamentTemplateEntity().getMaxPlayers();

        long updatedEntriesNumber = currentEntries + entries;
        if (updatedEntriesNumber > maxEntries
                || myEntries + entries > maxEntriesPerPlayer
                || (currentPlayers >= maxPlayers && myEntries == 0)) {
            throw new Exception(String.format("%d out of %d entries were created", 0, Math.abs(entries)));
        }
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
    public void updateEntry(long entryId, int payOut, int rank, double score) {

        EntryEntity ee = entityManager.find(EntryEntity.class, entryId);
        ee.setPayout(payOut);
        ee.setRank(rank);
        ee.setScore(score);

    }

    @Override
    public LeaderBoardState getCurrentLeaderBoardState() {

        String boardUnit = "new com.fasto.datamanager.dto.LeaderBoardUnit("
                + "SUM(e.score), "
                + "COUNT(DISTINCT e.tournamentEntity.tournamentId), "
                + "SUM(e.payout), "
                + "SUM(CASE WHEN e.payout > 0  THEN 1 ELSE 0 END), "
                + "e.playerEntity.playerId, "
                + "e.playerEntity.alias)";

        String allTimeWinnersQueryString = "SELECT " + boardUnit + " FROM EntryEntity e WHERE e.tournamentEntity.startTimestamp BETWEEN :from AND :to  GROUP BY e.playerEntity ORDER BY SUM(e.payout) DESC";
        String playersQueryString = "SELECT " + boardUnit + " FROM EntryEntity e WHERE e.tournamentEntity.startTimestamp BETWEEN :from AND :to AND e.tournamentEntity.tournamentTemplateEntity.duration in :duration GROUP BY e.playerEntity ORDER BY SUM(e.score) DESC";

        Date fromDate;
        Date toDate = new Date();

        LeaderBoardState lbs = new LeaderBoardState();

        // all times
        fromDate = new Date(0);
        lbs.getAll().add(new LeaderBoardState.AllTimeWinnersUnit(this.buildLeaderBoardPiece(allTimeWinnersQueryString, fromDate, toDate, null)));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(-1, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(5, 10, 15, 30, 60, 1440))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(5, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(5))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(10, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(10))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(15, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(15))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(30, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(30))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(60, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(60))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(1440, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(1440))));

        // from start of the day
        Long time = new Date().getTime();
        fromDate = new Date(time - time % (24 * 60 * 60 * 1000));
        lbs.getAll().add(new LeaderBoardState.AllTimeWinnersUnit(this.buildLeaderBoardPiece(allTimeWinnersQueryString, fromDate, toDate, null)));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(-1, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(5, 10, 15, 30, 60, 1440))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(5, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(5))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(10, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(10))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(15, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(15))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(30, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(30))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(60, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(60))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(1440, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(1440))));

        // week
        Calendar wc = Calendar.getInstance();
        wc.add(Calendar.WEEK_OF_YEAR, -1);
        fromDate = wc.getTime();
        lbs.getAll().add(new LeaderBoardState.AllTimeWinnersUnit(this.buildLeaderBoardPiece(allTimeWinnersQueryString, fromDate, toDate, null)));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(-1, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(5, 10, 15, 30, 60, 1440))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(5, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(5))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(10, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(10))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(15, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(15))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(30, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(30))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(60, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(60))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(1440, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(1440))));

        // month
        Calendar mc = Calendar.getInstance();
        mc.add(Calendar.MONTH, -1);
        fromDate = mc.getTime();
        lbs.getAll().add(new LeaderBoardState.AllTimeWinnersUnit(this.buildLeaderBoardPiece(allTimeWinnersQueryString, fromDate, toDate, null)));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(-1, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(5, 10, 15, 30, 60, 1440))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(5, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(5))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(10, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(10))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(15, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(15))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(30, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(30))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(60, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(60))));
        lbs.getAll().add(new LeaderBoardState.PlayersUnit(1440, this.buildLeaderBoardPiece(playersQueryString, fromDate, toDate, Arrays.asList(1440))));

        return lbs;
    }

    private List buildLeaderBoardPiece(String jpql, Date fromDate, Date toDate, List duration) {
        
        int limit = 10;
        Query query = entityManager.createQuery(jpql).setParameter("from", fromDate).setParameter("to", toDate);
        if (duration != null && !duration.isEmpty()) {
            query.setParameter("duration", duration);
        }
        List<LeaderBoardUnit> resultList = query.setMaxResults(limit).getResultList();
        for (int i = 0; i < resultList.size(); i++) {
            resultList.get(i).setPosition(i+1);
        }
        return resultList;
    }
}
