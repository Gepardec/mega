package com.gepardec.mega.config;

import io.quarkus.jsonb.JsonbConfigCustomizer;

import javax.inject.Singleton;
import javax.json.bind.JsonbConfig;
import javax.json.bind.config.PropertyVisibilityStrategy;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Configures the Jsonb-Serializer to serialize AutoValue classes.
 * Without this modification, a class which is implemented with Google AutoValue will not be serialized in the
 * response of the REST-API.
 */
@Singleton
public class JsonbConfigurator implements JsonbConfigCustomizer {

    @Override
    public void customize(JsonbConfig jsonbConfig) {
        PropertyVisibilityStrategy accessAutoValueFields = new PropertyVisibilityStrategy() {
            @Override
            public boolean isVisible(Field field) {
                return Modifier.isPrivate(field.getModifiers());
            }

            @Override
            public boolean isVisible(Method method) {
                return Modifier.isPublic(method.getModifiers());
            }
        };
        jsonbConfig.withPropertyVisibilityStrategy(accessAutoValueFields);
    }
}
