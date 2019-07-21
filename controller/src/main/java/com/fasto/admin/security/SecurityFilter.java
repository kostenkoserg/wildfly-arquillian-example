package com.fasto.admin.security;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.ws.rs.Path;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

/**
 *
 * @author kostenko
 */
@Provider
public class SecurityFilter implements ContainerRequestFilter {

    //@TODO: Temporary solution:
    public final static Map<String, String>  tokens = new ConcurrentHashMap<>();
    
    @Override
    public void filter(ContainerRequestContext context) throws IOException {

//        if (isRequestedResourceSecured(context)) {
//            String token = context.getHeaderString(HttpHeaders.AUTHORIZATION);
//            if (!this.isTokenValid(token)) {
//             //   context.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("Access denied").build());
//            }
//        }
    }

    private boolean isTokenValid(String token) {
        
        if ("THIS_IS_FASTO_TOKEN_001".equals(token)) {
            return true;
        }
        
        return token != null && tokens.containsKey(token);
    }

    private boolean isRequestedResourceSecured(ContainerRequestContext context) {

        UriInfo uriInfo = context.getUriInfo();
        List matchedResources = uriInfo.getMatchedResources();
        if (!matchedResources.isEmpty()) {
            Class matchedClass = matchedResources.get(0).getClass();

            String pathOnClass = ((Path) matchedClass.getAnnotation(Path.class)).value();
            Boolean securedOnClass = ((Secured) matchedClass.getAnnotation(Secured.class)) != null;

            Method[] methods = matchedClass.getDeclaredMethods();
            for (Method method : methods) {
                Path pathOnMethod = ((Path) method.getAnnotation(Path.class));
                if (pathOnMethod != null) {
                    Boolean securedOnMethod = ((Secured) method.getAnnotation(Secured.class)) != null;
                    if (uriInfo.getPath().equals((pathOnClass + pathOnMethod.value()).replaceAll("//", "/"))) {
                        if (securedOnClass || securedOnMethod) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
}
