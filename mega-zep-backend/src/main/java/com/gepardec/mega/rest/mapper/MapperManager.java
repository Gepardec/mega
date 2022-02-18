package at.liwest.ems.fiveg.application.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MapperManager {

    private static final String EXCLUDED_FIELD_TRANSACTIONID = "TransactionId";

    private final MapperFactory mapperFactory;

    public MapperManager() {
        this.mapperFactory = new DefaultMapperFactory.Builder().build();
    }

    public <T, V> V map(T obj, Class<V> type) {
        this.mapperFactory.classMap(obj.getClass(), type)
                .exclude(EXCLUDED_FIELD_TRANSACTIONID)
                .mapNulls(true)
                .byDefault()
                .register();
        return mapperFactory.getMapperFacade().map(obj, type);
    }
}
