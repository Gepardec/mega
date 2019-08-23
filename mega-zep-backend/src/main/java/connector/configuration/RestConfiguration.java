package connector.configuration;

import connector.filter.CORSResponseFilter;
import connector.rest.api.WorkerApi;
import connector.rest.impl.WorkerImpl;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/rest-api")
@ApplicationScoped
public class RestConfiguration extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<Class<?>>();
        resources.add(CORSResponseFilter.class);
        resources.add(WorkerImpl.class);
        return resources;
    }
}
