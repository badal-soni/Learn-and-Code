package com.intimetec.newsaggregation.client.context;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ApplicationContext {

    private final Map<Class<?>, Object> beans = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <T> T getBean(Class<T> requiredType) {
        return (T) beans.computeIfAbsent(requiredType, this::createBean);
    }

    private <T> T createBean(Class<T> type) {
        try {
            var constructors = type.getDeclaredConstructors();
            var constructor = constructors[0];
            for (var c : constructors) {
                if (c.getParameterCount() > constructor.getParameterCount()) {
                    constructor = c;
                }
            }

            Class<?>[] parameterTypes = constructor.getParameterTypes();
            Object[] dependencies = new Object[parameterTypes.length];
            for (int i = 0; i < parameterTypes.length; i++) {
                dependencies[i] = getBean(parameterTypes[i]);
            }

            constructor.setAccessible(true);
            return type.cast(constructor.newInstance(dependencies));
        } catch (Exception e) {
            throw new RuntimeException("Failed to create bean of type: " + type.getName(), e);
        }
    }
}
