package com.fasto.admin.endpoint;

import com.fasto.admin.AdminApplication;
import com.fasto.admin.QueryRequestHelper;
import com.fasto.admin.model.PayOut;
import com.fasto.admin.model.PayOutDetail;
import com.fasto.admin.model.Slate;
import com.fasto.admin.model.Tournament;
import com.fasto.admin.model.TournamentTemplate;
import com.fasto.admin.security.Secured;
import com.fasto.datamanager.SlateService;
import com.fasto.datamanager.StockService;
import com.fasto.datamanager.dto.PayOutDetailsDto;
import com.fasto.datamanager.dto.PayOutDto;
import com.fasto.datamanager.dto.SlateDto;
import com.fasto.datamanager.dto.TournamentDto;
import com.fasto.datamanager.dto.TournamentTemplateDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.paging.QueryRequest;
import com.fasto.datamanager.paging.QueryResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.kostenko.examples.wildflyarquillian.TestService;

/**
 * @author kostenko
 */
@Path("/")
@Secured
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TestEndpoint {

    @EJB(lookup = TestService.TOURNAMENT_SERVICE_JNDI)
    TestService tournamentService;

    @EJB(lookup = SlateService.SLATE_SERVICE_JNDI_NAME)
    SlateService slateService;

    @EJB(lookup = StockService.STOCK_SERVICE_JNDI)
    StockService stockService;


    @GET
    @Path("/tournaments")
    public Response getTournaments(
            @QueryParam("offset") Integer offset,
            @QueryParam("limit") Integer limit,
            @QueryParam("sort") String sort,
            @QueryParam("filters") String filters
    ) {

        QueryRequest queryRequest = QueryRequestHelper.buildQueryRequest(offset, limit, sort, filters);
        QueryResponse<TournamentDto> tournaments = tournamentService.getTournaments(queryRequest);

        List<Tournament> response = new ArrayList<>();
        for (TournamentDto dto : tournaments.getData()) {
            Tournament t = new Tournament();
            t.setId(dto.getTournamentId());
            t.setDuration(dto.getTournamentTemplateDto().getDuration());
            t.setName(dto.getName());
            t.setSlateId(dto.getSlateDto().getSlateId());
            t.setStartTime(dto.getStartTimestamp().getTime());
            t.setStructure(dto.getStructure());
            t.setTemplateId(dto.getTournamentTemplateDto().getTournamentTemplateId());
            t.setType(dto.getType());
            t.setVisibleInLobbyTime(dto.getVisibleInLobbyTimestamp().getTime());

            response.add(t);
        }

        return Response.ok()
                .header(AdminApplication.Headers.X_PAGER_TOTAL_ENTRIES, tournaments.getTotalPageCount())
                .entity(response).build();
    }

    @POST
    @Path("/tournament")
    public Response createTournament(final Tournament tournament) {

        tournamentService.createTournament(
                tournament.getName(),
                tournament.getTemplateId(),
                tournament.getSlateId(),
                tournament.getPayOutId(),
                tournament.getType(),
                tournament.getStructure(),
                tournament.getDuration(),
                new Date(tournament.getStartTime()),
                new Date(tournament.getVisibleInLobbyTime()));

        return Response.ok().build();
    }


    @PUT
    @Path("/tournament")
    public Response updateTournament(final Tournament tournament) {

        tournamentService.updateTournament(
                tournament.getName(),
                tournament.getId(),
                tournament.getTemplateId(),
                tournament.getSlateId(),
                tournament.getPayOutId(),
                tournament.getType(),
                tournament.getStructure(),
                tournament.getDuration(),
                new Date(tournament.getStartTime()),
                new Date(tournament.getVisibleInLobbyTime()));

        return Response.ok().build();
    }

    @DELETE
    @Path("/tournament/{id}")
    public Response deleteStock(@PathParam("id") Long tournamentId) {
        tournamentService.removeTournamentById(tournamentId);
        return Response.ok().entity(new ResponseWrapper()).build();
    }

    @GET
    @Path("/tournament/templates")
    public Response getTournamentTemplates(
            @QueryParam("offset") Integer offset,
            @QueryParam("limit") Integer limit,
            @QueryParam("sort") String sort,
            @QueryParam("filters") String filters
    ) {

        QueryRequest queryRequest = QueryRequestHelper.buildQueryRequest(offset, limit, sort, filters);
        ResponseWrapper<TournamentTemplateDto> tournamentTemplates = tournamentService.getTournamentTemplates(queryRequest);

        List<TournamentTemplate> response = new ArrayList<>();
        for (TournamentTemplateDto dto : tournamentTemplates.getData()) {
            TournamentTemplate tt = new TournamentTemplate();
            tt.setBuyIn(dto.getBuyIn());
            tt.setGuaranteed(dto.getGuaranteed());
            tt.setId(dto.getTournamentTemplateId());
            tt.setMaxEntries(dto.getMaxEntries());
            tt.setMaxEntriesPerPlayer(dto.getMaxEntriesPerPlayer());
            tt.setMaxPlayers(dto.getMaxPlayers());
            tt.setMinEntries(dto.getMinEntries());
            tt.setMinPlayers(dto.getMinPlayers());
            tt.setName(dto.getName());
            tt.setRake(dto.getRake());

            response.add(tt);
        }

        return Response.ok()
                .header(AdminApplication.Headers.X_PAGER_TOTAL_ENTRIES, tournamentTemplates.getTotalPageCount())
                .entity(response).build();
    }

    @POST
    @Path("/tournament/template")
    public Response createTournamentTemplate(final TournamentTemplate tournamentTemplate) {

        tournamentService.createTournamentTemplate(
                tournamentTemplate.getName(),
                tournamentTemplate.getBuyIn(),
                tournamentTemplate.getRake(),
                tournamentTemplate.getGuaranteed(),
                tournamentTemplate.getMinPlayers(),
                tournamentTemplate.getMaxPlayers(),
                tournamentTemplate.getMinEntries(),
                tournamentTemplate.getMaxEntries(),
                tournamentTemplate.getMaxEntriesPerPlayer());

        return Response.ok().build();
    }

    @GET
    @Path("/tournament/payouts")
    public Response getPayouts(
            @QueryParam("offset") Integer offset,
            @QueryParam("limit") Integer limit,
            @QueryParam("sort") String sort,
            @QueryParam("filters") String filters
    ) {

        QueryRequest queryRequest = QueryRequestHelper.buildQueryRequest(offset, limit, sort, filters);
        ResponseWrapper<PayOutDto> payouts = tournamentService.getPayouts(queryRequest);

        List<PayOut> response = new ArrayList<>();
        for (PayOutDto dto : payouts.getData()) {
            PayOut p = new PayOut();
            p.setId(dto.getId());
            p.setName(dto.getName());

            List<PayOutDetailsDto> detailsDtos = new ArrayList<>();//to.getPayOutDetails();
            for (PayOutDetailsDto ddto : detailsDtos) {
                PayOutDetail pd = new PayOutDetail();
                pd.setId(ddto.getId());
                pd.setStartPlace(ddto.getStartPlace());
                pd.setEndPlace(ddto.getEndPlace());
                pd.setPercentage(ddto.getPercentage());

                p.getPayoutdetails().add(pd);
            }

            response.add(p);
        }

        return Response.ok()
                .header(AdminApplication.Headers.X_PAGER_TOTAL_ENTRIES, payouts.getTotalPageCount())
                .entity(response).build();
    }

    @POST
    @Path("/tournament/payout")
    public Response createPayout(final PayOut payOut) {

        long payoutId = tournamentService.createPayOut(payOut.getName());
        for (PayOutDetail pd : payOut.getPayoutdetails()) {
            tournamentService.createPayOutDetails(payoutId, pd.getStartPlace(), pd.getEndPlace(), pd.getPercentage());
        }
        return Response.ok().build();
    }

    @GET
    @Path("/tournament/slates")
    public Response getSlates(
            @QueryParam("offset") Integer offset,
            @QueryParam("limit") Integer limit,
            @QueryParam("sort") String sort,
            @QueryParam("filters") String filters
    ) {

        QueryRequest queryRequest = QueryRequestHelper.buildQueryRequest(offset, limit, sort, filters);
        ResponseWrapper<SlateDto> slates = slateService.getSlates(queryRequest);

        List<Slate> response = new ArrayList<>();
        for (SlateDto dto : slates.getData()) {
            Slate s = new Slate();
            s.setId(dto.getSlateId());
            s.setEnabled(dto.getEnabled());
            s.setName(dto.getName());
            s.setStockExchangeId(dto.getStockExchangeDto().getStockExchangeId());
            //s.getStockIds().addAll(dto.getStockIDs());
            response.add(s);
        }

        return Response.ok()
                .header(AdminApplication.Headers.X_PAGER_TOTAL_ENTRIES, slates.getTotalPageCount())
                .entity(response).build();
    }

    @POST
    @Path("/tournament/slate")
    public Response createSlate(final Slate slate) {

        long slateId = slateService.createSlate(
                slate.getName(),
                slate.getStockExchangeId(),
                slate.getEnabled());

        for (long stockId : slate.getStockIds()) {
            stockService.createStockToSlateMap(stockId, slateId);
        }

        return Response.ok().build();
    }

    @GET
    @Path("/tournament/details")
    public Response getTournament(
            @QueryParam("id") Long tournamentId
    ) {
        TournamentDto tournamentDto = tournamentService.getTournamentDetails(tournamentId);
        return Response.ok()
                .entity(tournamentDto).build();
    }


}
