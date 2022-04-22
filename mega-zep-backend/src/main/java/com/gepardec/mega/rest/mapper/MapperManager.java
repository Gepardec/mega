package com.gepardec.mega.rest.mapper;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.DefaultMapperFactory;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class MapperManager {

    private final MapperFactory mapperFactory;

    public MapperManager() {
        this.mapperFactory = new DefaultMapperFactory.Builder().build();
    }

    public <T, V> V map(T obj, Class<V> type) {
        this.mapperFactory.classMap(obj.getClass(), type)
                .mapNulls(true)
                .byDefault()
                .register();
        return mapperFactory.getMapperFacade().map(obj, type);
    }

    public <T, V, W> List<W> mapAsList(List<T> objects, Class<V> sourceType, Class<W> destinationType) {
        this.mapperFactory.classMap(sourceType, destinationType)
                .mapNulls(true)
                .byDefault()
                .register();
        return mapperFactory.getMapperFacade().mapAsList(objects, destinationType);
    }
}
