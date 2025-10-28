package com.example.logging;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory methods for creating {@link ServiceLogger} instances.
 *
 * <p>The factory can be initialised once with a service name via {@link #initialize(String)}. Subsequent calls to
 * {@link #getLogger(Class)} will use that shared name. Alternatively, callers can request a logger for a specific
 * service name with {@link #getLogger(Class, String)}.</p>
 */
public final class ServiceLoggerFactory {

    private static final AtomicReference<String> SERVICE_NAME = new AtomicReference<>();

    private ServiceLoggerFactory() {
    }

    /**
     * Initializes the factory with the service name that should be injected into all log messages.
     *
     * @param serviceName the service (microservice) name
     * @throws IllegalArgumentException if the supplied name is {@code null} or blank
     */
    public static void initialize(String serviceName) {
        SERVICE_NAME.set(validate(serviceName));
    }

    /**
     * Clears any previously configured service name. Intended primarily for tests.
     */
    public static void clear() {
        SERVICE_NAME.set(null);
    }

    /**
     * Returns a logger for the supplied class using the globally configured service name.
     *
     * @param clazz the class the logger is associated with
     * @return a {@link ServiceLogger}
     * @throws IllegalStateException if {@link #initialize(String)} has not been called
     */
    public static ServiceLogger getLogger(Class<?> clazz) {
        String name = SERVICE_NAME.get();
        if (name == null) {
            throw new IllegalStateException(
                    "ServiceLoggerFactory has not been initialised. Call initialize(serviceName) before requesting loggers.");
        }
        return getLogger(clazz, name);
    }

    /**
     * Returns a logger for the supplied class using the provided service name.
     *
     * @param clazz       the class the logger is associated with
     * @param serviceName the service name that should prefix log entries
     * @return a {@link ServiceLogger}
     */
    public static ServiceLogger getLogger(Class<?> clazz, String serviceName) {
        Objects.requireNonNull(clazz, "clazz");
        String validated = validate(serviceName);
        Logger logger = LoggerFactory.getLogger(clazz);
        return new ServiceLogger(logger, validated);
    }

    private static String validate(String serviceName) {
        if (serviceName == null || serviceName.isBlank()) {
            throw new IllegalArgumentException("serviceName must not be null or blank");
        }
        return serviceName;
    }
}
