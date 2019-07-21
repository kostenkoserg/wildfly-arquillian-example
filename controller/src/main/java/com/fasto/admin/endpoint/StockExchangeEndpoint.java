package com.fasto.admin.endpoint;

import com.fasto.admin.AdminApplication;
import com.fasto.admin.QueryRequestHelper;
import com.fasto.admin.model.StockExchange;
import com.fasto.admin.security.Secured;
import com.fasto.datamanager.StockExchangeService;
import com.fasto.datamanager.dto.StockExchangeDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.paging.QueryRequest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author kostenko
 */
@Path("/")
@Secured
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StockExchangeEndpoint {

    @EJB(lookup = StockExchangeService.STOCK_EXCHANGE_SERVICE_JNDI)
    StockExchangeService stockExchangeService;

    @GET
    @Path("/stockexchanges")
    public Response getStockExchanges(
            @QueryParam("offset") Integer offset,
            @QueryParam("limit") Integer limit,
            @QueryParam("sort") String sort,
            @QueryParam("filters") String filters
    ) {

        QueryRequest queryRequest = QueryRequestHelper.buildQueryRequest(offset, limit, sort, filters);
        ResponseWrapper<StockExchangeDto> stockExchanges = stockExchangeService.getStockExchanges(queryRequest);

        List<StockExchange> response = new ArrayList<>();
        for (StockExchangeDto stockExchange : stockExchanges.getData()) {
            StockExchange s = new StockExchange();
            s.setId(stockExchange.getStockExchangeId());
            s.setName(stockExchange.getName());
            s.setShortName(stockExchange.getShortName());
            s.setEnabled(stockExchange.getEnabled());
            s.setStartTime(stockExchange.getStartTimeUtc().getTime());
            s.setEndTime(stockExchange.getCloseTimeUtc().getTime());

            response.add(s);
        }

        return Response.ok()
                .header(AdminApplication.Headers.X_PAGER_TOTAL_ENTRIES, stockExchanges.getTotalPageCount())
                .entity(response).build();
    }

    @POST
    @Path("/stockexchange")
    public Response createStockExchange(StockExchange stockExchange) {

        stockExchangeService.createStockExchange(
                stockExchange.getName(), stockExchange.getShortName(), stockExchange.getEnabled(),
                new Date(stockExchange.getStartTime()), new Date(stockExchange.getEndTime()));

        return Response.ok().build();
    }

    @PUT
    @Path("/stockexchange")
    public Response updateStock(StockExchange stockExchange) {
        stockExchangeService.updateStockExchange(stockExchange.getId(),
                stockExchange.getName(), stockExchange.getShortName(), stockExchange.getEnabled(),
                new Date(stockExchange.getStartTime()), new Date(stockExchange.getEndTime()));
        return Response.ok().entity(new ResponseWrapper()).build();
    }


    @DELETE
    @Path("/stockexchange/{id}")
    public Response deleteStock(@PathParam("id") Long stockExchangeId) {
        stockExchangeService.removeStockExchangeById(stockExchangeId);
        return Response.ok().entity(new ResponseWrapper()).build();
    }
}
