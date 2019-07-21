package com.fasto.admin.endpoint;

import com.fasto.admin.AdminApplication.Headers;
import com.fasto.admin.QueryRequestHelper;
import com.fasto.admin.model.Stock;
import com.fasto.admin.security.Secured;
import com.fasto.datamanager.StockService;
import com.fasto.datamanager.dto.StockDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.paging.QueryRequest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/")
@Secured
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class StockEndpoint {


    @EJB(lookup = StockService.STOCK_SERVICE_JNDI)
    StockService stockService;

    @GET
    @Path("/stocks")
    public Response getStocks(
            @QueryParam("offset") Integer offset,
            @QueryParam("limit") Integer limit,
            @QueryParam("sort") String sort,
            @QueryParam("filters") String filters
    ) {

        QueryRequest queryRequest = QueryRequestHelper.buildQueryRequest(offset, limit, sort, filters);
        ResponseWrapper<StockDto> stocks = stockService.getStocks(queryRequest);

        List<Stock> response = new ArrayList<>();
        for (StockDto stock : stocks.getData()) {
            Stock s = new Stock();
            s.setId(stock.getStockId());
            s.setName(stock.getName());
            s.setSymbol(stock.getSymbol());
            s.setUrl(stock.getUrl());
            s.setEnabled(stock.getEnabled());
            response.add(s);
        }

        return Response.ok()
                .header(Headers.X_PAGER_TOTAL_ENTRIES, stocks.getTotalPageCount())
                .entity(response).build();
    }

    @GET
    @Path("/stock")
    public Response getStock(@QueryParam("id") Long stockId) {
        StockDto stockDto = stockService.getStocksByID(stockId);
        return Response.ok().entity(stockDto).build();
    }

    @POST
    @Path("/stock")
    public Response createStock(Stock stock) {
        stockService.createStock(stock.getName(), stock.getSymbol(), stock.getUrl(), stock.getEnabled(), stock.getStockExchangeId());
        return Response.ok().entity(new ResponseWrapper()).build();
    }

    @PUT
    @Path("/stock")
    public Response updateStock(Stock stock) {
        stockService.updateStock(stock.getId(), stock.getName(), stock.getSymbol(), stock.getUrl(), stock.getEnabled(), stock.getStockExchangeId());
        return Response.ok().entity(new ResponseWrapper()).build();
    }


    @DELETE
    @Path("/stock/{id}")
    public Response deleteStock(@PathParam("id") Integer stockId) {
        stockService.removeStockById(stockId);
        return Response.ok().entity(new ResponseWrapper()).build();
    }
}
