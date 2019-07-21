package com.fasto.zootester.api;

import com.fasto.datamanager.DataFeedProviderService;
import com.fasto.datamanager.PlayerService;
import com.fasto.datamanager.SlateService;
import com.fasto.datamanager.StockExchangeService;
import com.fasto.datamanager.StockService;
import com.fasto.datamanager.TournamentService;
import com.fasto.datamanager.dto.EntryDto;
import com.fasto.datamanager.dto.PlayerDto;
import com.fasto.datamanager.dto.SlateDto;
import com.fasto.datamanager.dto.TournamentDto;
import com.fasto.datamanager.dto.TournamentTemplateDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;

import javax.ejb.Singleton;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.lang.management.ManagementFactory;
import java.util.Calendar;
import java.util.List;

/**
 * Provide simple API to emulate FASTO scenarios. Uses from integration tests
 *
 * @author kostenko
 */
@Singleton
public class TestAPI {

    @PersistenceContext
    EntityManager entityManager;

    public static final String ADMIN_ENDPOINT = "http://localhost:8080/admin-endpoint-0.1/";
    public static final String PLAYER_ENDPOINT = "http://localhost:8080/player-endpoint-0.1/";

    /**
     * Create tournament over admin-endpoint.
     *
     * @param tournament
     */
    public void createTournament(TournamentDto tournament) {

        Response response = ClientBuilder.newClient().target(ADMIN_ENDPOINT + "tournament/create")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(tournament));

        System.out.println(response.readEntity(String.class));
    }

    /**
     * Create tournament template over admin-endpoint.
     *
     * @param tournamentTemplate
     */
    public void createTournamentTemplate(TournamentTemplateDto tournamentTemplate) {

        Response response = ClientBuilder.newClient().target(ADMIN_ENDPOINT + "tournament/template/create")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(tournamentTemplate));

        System.out.println(response.readEntity(String.class));
    }

    /**
     * Create slate template over admin-endpoint.
     *
     * @param tournamentTemplate
     */
    public void createSlate(SlateDto slate) {

        Response response = ClientBuilder.newClient().target(ADMIN_ENDPOINT + "tournament/slate/create")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(slate));

        System.out.println(response.readEntity(String.class));
    }

    /**
     * @param sql
     * @return
     */
    public int selectCount(String sql) {
        return ((Number) entityManager.createNativeQuery("SELECT COUNT(*) " + sql).getSingleResult()).intValue();
    }

    public void createPlayer(PlayerDto playerDto) {
        Response response = ClientBuilder.newClient().target(PLAYER_ENDPOINT + "player/create")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(playerDto));

        System.out.println(response.readEntity(String.class));
    }

    public void createEntry(EntryDto entryDto) {
        Response response = ClientBuilder.newClient().
                target(PLAYER_ENDPOINT + "entry/create")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(entryDto));

        System.out.println(response.readEntity(String.class));
    }

    public ResponseWrapper getUpcomingTournaments(Long playerID) {
        Response response = ClientBuilder.newClient().
                target(PLAYER_ENDPOINT + "tournament/upcoming/list?perPage=100&page=1&playerID=" + playerID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        return response.readEntity(new GenericType<ResponseWrapper<TournamentDto>>() {
        });

    }

    public List<TournamentDto> getLivingTournaments(Long playerID) {
        Response response = ClientBuilder.newClient().target(PLAYER_ENDPOINT + "tournament/living/list?perPage=100&page=1&playerID=" + playerID)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();

        return response.readEntity(new GenericType<List<TournamentDto>>() {
        });

    }

    public void waitForTournamentRunnerShouldRun() {
        try {
            while (Calendar.getInstance().get(Calendar.SECOND) != 0) {
                System.out.println("Waiting for TournamentRunner ...");
                Thread.sleep(500);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void waitForFinishBatches() throws Exception {
        final MBeanServer mbServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("jboss.as:subsystem=batch-jberet,thread-pool=batch");
        Integer activeCount = (Integer) mbServer.getAttribute(objectName, "activeCount");
        while (activeCount > 0 ) { 
            Thread.sleep(1000);
            activeCount = (Integer) mbServer.getAttribute(objectName, "activeCount");
            System.out.println("Waiting for finish batches. Active threads " + activeCount);
        }
        System.out.println("No active batch threads");
    }
    
    public void waitForJmsProcessing(String module, String mdbName) throws Exception {
        final MBeanServer mbServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName objectName = new ObjectName("jboss.as:deployment=" + module + ".ear,subdeployment=" + module + ".jar,subsystem=ejb3,message-driven-bean=" + mdbName);
        Object poolMaxSize = mbServer.getAttribute(objectName, "pool-max-size");
        Object poolAvailableCount = mbServer.getAttribute(objectName, "pool-available-count");
        int count = 0;
        while (!poolMaxSize.equals(poolAvailableCount) && count < 30) { // waits for 30 seconds
            System.out.println("Waiting for finish jms processing...");
            Thread.sleep(1000);
            poolMaxSize = mbServer.getAttribute(objectName, "pool-max-size");
            poolAvailableCount = mbServer.getAttribute(objectName, "pool-available-count");
            count++;
        }
    }
    

    public PlayerService getPlayerService() throws NamingException {
        return (PlayerService) new InitialContext().lookup(PlayerService.PLAYER_SERVICE_JNDI);
    }
    
    public TournamentService getTournamentService() throws NamingException {
        return (TournamentService) new InitialContext().lookup(TournamentService.TOURNAMENT_SERVICE_JNDI);
    }

    public SlateService getSlateService() throws NamingException {
        return (SlateService) new InitialContext().lookup(SlateService.SLATE_SERVICE_JNDI_NAME);
    }

    public StockService getStockService() throws NamingException {
        return (StockService) new InitialContext().lookup(StockService.STOCK_SERVICE_JNDI);
    }

    public StockExchangeService getStockExchangeService() throws NamingException {
        return (StockExchangeService) new InitialContext().lookup(StockExchangeService.STOCK_EXCHANGE_SERVICE_JNDI);
    }

    public DataFeedProviderService getDataFeedProviderService() throws NamingException {
        return (DataFeedProviderService) new InitialContext().lookup(DataFeedProviderService.DATA_FEED_PROVIDER_SERVICE_JNDI);
    }

}
