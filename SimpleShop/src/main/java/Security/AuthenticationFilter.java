package Security;

import Models.TokenSession;
import Models.User;
import enums.UserRights;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Principal;

@Secured
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    @Context
    ResourceInfo resourceInfo;

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException{

        MultivaluedMap<String,String> pathparam = requestContext.getUriInfo().getPathParameters();
        String parameter = "0";
        parameter = pathparam.getFirst("id");

        String authorizatonHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if(authorizatonHeader == null){
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }

        String token = authorizatonHeader.substring("Bearer".length()).trim();

        try{
            final TokenSession tokenSession = TokenSession.findByToken(token);

            if(tokenSession == null){
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                return ;
            }

            Method resourceMethod = resourceInfo.getResourceMethod();
            Secured methodAnnot = resourceMethod.getAnnotation(Secured.class);
            UserRights[] allowedRights = methodAnnot.value();
            boolean userHasRights = User.hasRequiredRights(tokenSession.getUserId(), allowedRights,parameter);

            if(!userHasRights){
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                return;
            }

            requestContext.setSecurityContext(new SecurityContext() {

                @Override
                public Principal getUserPrincipal() {

                    return new Principal() {

                        @Override
                        public String getName() {
                            return tokenSession.getUserId();
                        }
                    };
                }

                @Override
                public boolean isUserInRole(String role) {
                    return true;
                }

                @Override
                public boolean isSecure() {
                    return false;
                }

                @Override
                public String getAuthenticationScheme() {
                    return BASIC_AUTH;
                }

            });

        } catch (Exception e) {
            e.printStackTrace();
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
        }
    }

}



