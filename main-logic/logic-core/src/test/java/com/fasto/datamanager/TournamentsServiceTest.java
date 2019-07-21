package com.fasto.datamanager;

import com.fasto.datamanager.dto.StockDto;
import com.fasto.datamanager.dto.TournamentDto;
import com.fasto.datamanager.dto.TournamentStatus;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.dto.wrappers.TournamentEndPointRequestWrapper;
import com.fasto.datamanager.model.EntryEntity;
import com.fasto.datamanager.model.PlayerEntity;
import com.fasto.datamanager.model.SlateEntity;
import com.fasto.datamanager.model.StockEntity;
import com.fasto.datamanager.model.StockExchangeEntity;
import com.fasto.datamanager.model.StockToSlateMapEntity;
import com.fasto.datamanager.model.TournamentEntity;
import com.fasto.datamanager.model.converters.Convertible;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
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
public class TournamentsServiceTest {

    TournamentServiceImpl tournamentService = new TournamentServiceImpl();

    @Before
    public void init() {
        tournamentService.entityManager = Persistence.createEntityManagerFactory("fastoDSTest").createEntityManager();
    }

    EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("fastoDSTest");

    @Test
    public void entityCreation() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        Query tournamentQuery = entityManager.createQuery("select t from TournamentEntity t ");
        tournamentQuery.getResultList();
    }

    @Test
    public void aaa() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        StockExchangeEntity see = new StockExchangeEntity();
        see.setCloseTimeUtc(new Date());
        see.setEnabled(true);
        see.setName("ssssssss");
        see.setShortName("d");
        see.setStartTimeUtc(new Date());
        see.setStockExchangeId(1l);
        
        entityManager.merge(see);

        
        SlateEntity slate = new SlateEntity();
        slate.setSlateId(1l);
        slate.setName("asasasasasa");
        slate.setEnabled(true);
        slate.setLineupSize(23);
        slate.setStockExchangeEntity(see);
        entityManager.merge(slate);
        
        

        for (long i = 1; i < 10; i++) {

            StockEntity entity1 = new StockEntity();
            entity1.setName("sdf"+i);
            entity1.setSymbol("A" + i);
            entity1.setStockId(i);
            entity1.setEnabled(true);
            entity1.setUrl("ssss");
            entity1.setStockExchangeEntity(see);

            entityManager.merge(entity1);
            
            StockToSlateMapEntity slm = new StockToSlateMapEntity();
            slm.setSlateEntity(entityManager.find(SlateEntity.class, 1l));
            slm.setStockEntity(entityManager.find(StockEntity.class, i));
            slm.setStockToSlateMapId(i);
            
            entityManager.merge(slm);
        }
        entityManager.getTransaction().commit();
        
        entityManager.getTransaction().begin();
        Query query = entityManager
                .createQuery("SELECT m.stockEntity FROM StockToSlateMapEntity m  WHERE m.slateEntity.slateId = :slateId")
                .setParameter("slateId", 1l);

        List<StockDto> stockDtoList = Convertible.convert(query.getResultList());
        System.out.println(">>>>> " + stockDtoList.size());
        entityManager.getTransaction().commit();
    }

//    @Test
//    public void getTournamentList() {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        List<TournamentEntity> tournamentEntities = getTournamentEntitiesWithSpecifiedStatus(entityManager, TournamentStatus.NEW);
//        TournamentEndPointRequestWrapper tournamentEndPointRequestWrapper = new TournamentEndPointRequestWrapper(20, 1);
//        ResponseWrapper<TournamentDto> responseWrapper = tournamentService.getTournamentList(tournamentEndPointRequestWrapper);
//        List<TournamentDto> tournamentDtoList = responseWrapper.getData();
//        checkTournamentListsOnEquals(tournamentEntities, tournamentDtoList);
//    }
//    @Test(expected = RuntimeException.class)
//    public void getTournamentListByNotExistingPlayerId() {
//        TournamentEndPointRequestWrapper tournamentEndPointRequestWrapper = new TournamentEndPointRequestWrapper(0l);
//        tournamentService.getTournamentListByPlayerIdAndStatusWithPagination(tournamentEndPointRequestWrapper);
//    }
//    @Test
//    public void getTournamentListByPlayerId() {
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//
//        entityManager.getTransaction().begin();
//        //create playerEntity to persist
//        PlayerEntity playerEntity = getPlayerEntity(entityManager);
//        long playerId = playerEntity.getPlayerId();
//        Assert.assertTrue(playerId > 0);
//
//        //create tournamentEntity
//        List<TournamentEntity> tournamentEntityList = getTournamentEntitiesWithSpecifiedStatus(entityManager, TournamentStatus.NEW);
//
//        //create entry
//        createEntry(entityManager, playerEntity, tournamentEntityList);
//
//        //collect all ids for original tournaments
//        TournamentEndPointRequestWrapper tournamentEndPointRequestWrapper = new TournamentEndPointRequestWrapper(playerId);
//         ResponseWrapper<TournamentDto> tournamentDtosGettingByService = tournamentService.getTournamentListByPlayerIdAndStatusWithPagination(tournamentEndPointRequestWrapper);
//
//        checkTournamentListsOnEquals(tournamentEntityList, tournamentDtosGettingByService.getData());
//    }
    private void checkTournamentListsOnEquals(Collection<TournamentEntity> testTournamentEntityList, List<TournamentDto> tournamentDtosGettingByService) {
        Set<Long> tournamentDtoIds = tournamentDtosGettingByService.stream().map(c -> c.getTournamentId()).collect(Collectors.toSet());
        testTournamentEntityList.stream().forEach(t -> Assert.assertTrue(tournamentDtoIds.contains(t.getTournamentId())));
    }

    private PlayerEntity getPlayerEntity(EntityManager entityManager) {
        PlayerEntity playerEntity = new PlayerEntity();
        playerEntity.setAlias("playerAlias");
        playerEntity.setBalance(100000);
        playerEntity.setRating(2);
        playerEntity.setTimeCreated(new Date());
        playerEntity = entityManager.merge(playerEntity);
        entityManager.getTransaction().commit();
        return playerEntity;
    }

    private List<TournamentEntity> getTournamentEntitiesWithSpecifiedStatus(EntityManager entityManager, TournamentStatus tournamentStatus) {
        List<TournamentEntity> tournamentEntityList = new ArrayList();
        for (int i = 0; i < 10; i++) {
            entityManager.getTransaction().begin();
            TournamentEntity tournamentEntity = new TournamentEntity();
            tournamentEntity.setStatus(tournamentStatus);
            tournamentEntity.setCurrentEntries(i);
            tournamentEntity.setCurrentPlayers(i);
            tournamentEntity.setName("tournamentName " + i);
            tournamentEntity.setStartTimestamp(new Date());
            tournamentEntity = entityManager.merge(tournamentEntity);
            entityManager.getTransaction().commit();
            long tournamentId = tournamentEntity.getTournamentId();
            Assert.assertTrue(tournamentId > 0);
            tournamentEntityList.add(tournamentEntity);
        }
        return tournamentEntityList;
    }

    private void createEntry(EntityManager entityManager, PlayerEntity playerEntity, List<TournamentEntity> tournamentEntityList) {
        for (int i = 0; i < tournamentEntityList.size(); i++) {
            entityManager.getTransaction().begin();
            EntryEntity entryEntity = new EntryEntity();
            entryEntity.setLastTimeStatusChange(new Date());
            entryEntity.setPayout(100);
            entryEntity.setTournamentEntity(tournamentEntityList.get(i));
            entryEntity.setPlayerEntity(playerEntity);
            entryEntity = entityManager.merge(entryEntity);
            entityManager.getTransaction().commit();
            Assert.assertTrue(entryEntity.getEntryId() > 0);
        }
    }
}
