import org.jboss.resteasy.plugins.providers.multipart.InputPart;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
public class CharsetFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        requestContext.setProperty(InputPart.DEFAULT_CHARSET_PROPERTY, "UTF-8");
    }

}