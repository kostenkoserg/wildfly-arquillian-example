package com.fasto.admin.endpoint;

import com.fasto.admin.model.Authorization;
import com.fasto.admin.security.Secured;
import com.fasto.admin.security.SecurityFilter;
import java.util.UUID;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ejb.Stateless;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author kostenko
 */

@Path("/")
@Stateless
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserEndpoint {
    
    @GET
    @Path("/user/login")
    public Response login(@QueryParam("username") String username, @QueryParam("password") String password) {

        if ("fasto".equals(username) && "fasto".equals(password)) {
            String token = UUID.randomUUID().toString();
            SecurityFilter.tokens.put(token, username);
            return Response.ok(new Authorization(token)).build();
            
        } else {
            return Response.status(Status.UNAUTHORIZED).entity("Incorrect login and password combination").build();
        }
    }

    @GET
    @Secured
    @Path("/user/logout")
    public Response  logout() {
        return Response.ok().entity("Bye").build();
    }
    
}
