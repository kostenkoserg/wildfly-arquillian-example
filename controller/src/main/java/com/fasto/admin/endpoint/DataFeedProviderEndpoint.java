package com.fasto.admin.endpoint;

import com.fasto.admin.AdminApplication;
import com.fasto.admin.QueryRequestHelper;
import com.fasto.admin.model.DataFeedProvider;
import com.fasto.admin.security.Secured;
import com.fasto.datamanager.DataFeedProviderService;
import com.fasto.datamanager.dto.DataFeedProviderDto;
import com.fasto.datamanager.dto.wrappers.ResponseWrapper;
import com.fasto.datamanager.paging.QueryRequest;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kostenko
 */
@Path("/")
@Secured
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class DataFeedProviderEndpoint {

    @EJB(lookup = DataFeedProviderService.DATA_FEED_PROVIDER_SERVICE_JNDI)
    DataFeedProviderService dataFeedProviderService;

    @GET
    @Path("/datafeedproviders")
    public Response getStocks(
            @QueryParam("offset") Integer offset,
            @QueryParam("limit") Integer limit,
            @QueryParam("sort") String sort,
            @QueryParam("filters") String filters
    ) {

        QueryRequest queryRequest = QueryRequestHelper.buildQueryRequest(offset, limit, sort, filters);
        ResponseWrapper<DataFeedProviderDto> dataFeedProviders = dataFeedProviderService.getDataFeedProviders(queryRequest);

        List<DataFeedProvider> response = new ArrayList<>();
        for (DataFeedProviderDto dto : dataFeedProviders.getData()) {
            DataFeedProvider d = new DataFeedProvider();
            d.setId(dto.getDataFeedProviderId());
            d.setName(dto.getName());
            d.setSiteUrl(dto.getSiteUrl());
            d.setDataUrl(dto.getDataUrl());
            d.setAdditionalInfo(dto.getAdditionalInfoJSON());

            response.add(d);
        }

        return Response.ok()
                .header(AdminApplication.Headers.X_PAGER_TOTAL_ENTRIES, dataFeedProviders.getTotalPageCount())
                .entity(response).build();
    }

    @POST
    @Path("/datafeedprovider")
    public Response createStock(DataFeedProvider dataFeedProvider) {

        dataFeedProviderService.createDataFeedProvider(
                dataFeedProvider.getName(),
                dataFeedProvider.getSiteUrl(),
                dataFeedProvider.getDataUrl(),
                dataFeedProvider.getCredentials(),
                dataFeedProvider.getAdditionalInfo()
        );

        return Response.ok().build();
    }

    @PUT
    @Path("/datafeedprovider")
    public Response updateStock(DataFeedProvider dataFeedProvider) {
        dataFeedProviderService.updateDataFeedProvider(
                dataFeedProvider.getName(),
                dataFeedProvider.getSiteUrl(),
                dataFeedProvider.getDataUrl(),
                dataFeedProvider.getCredentials(),
                dataFeedProvider.getAdditionalInfo(),
                dataFeedProvider.getId());
        return Response.ok().entity(new ResponseWrapper()).build();
    }


    @DELETE
    @Path("/datafeedprovider/{id}")
    public Response deleteStock(@PathParam("id") Integer dataFeedProviderId) {
        dataFeedProviderService.removeDataFeedProviderById(dataFeedProviderId);
        return Response.ok().entity(new ResponseWrapper()).build();
    }

}
