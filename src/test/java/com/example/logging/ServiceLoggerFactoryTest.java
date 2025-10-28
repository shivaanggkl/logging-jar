package com.example.logging;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

class ServiceLoggerFactoryTest {

    @AfterEach
    void tearDown() {
        ServiceLoggerFactory.clear();
    }

    @Test
    void logsIncludeServiceName() {
        ServiceLoggerFactory.initialize("order-service");
        ServiceLogger logger = ServiceLoggerFactory.getLogger(ServiceLoggerFactoryTest.class);

        Logger logbackLogger = (Logger) LoggerFactory.getLogger(ServiceLoggerFactoryTest.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logbackLogger.addAppender(appender);

        logger.info("Processing order {}", 42);

        List<ILoggingEvent> events = appender.list;
        assertEquals(1, events.size());
        assertTrue(events.get(0).getFormattedMessage().contains("[order-service] Processing order 42"));

        logbackLogger.detachAppender(appender);
    }

    @Test
    void explicitServiceNameOverridesGlobal() {
        ServiceLoggerFactory.initialize("order-service");
        ServiceLogger logger = ServiceLoggerFactory.getLogger(ServiceLoggerFactoryTest.class, "billing-service");

        Logger logbackLogger = (Logger) LoggerFactory.getLogger(ServiceLoggerFactoryTest.class);
        ListAppender<ILoggingEvent> appender = new ListAppender<>();
        appender.start();
        logbackLogger.addAppender(appender);

        logger.warn("Payment {} failed", 1001);

        List<ILoggingEvent> events = appender.list;
        assertEquals(1, events.size());
        assertTrue(events.get(0).getFormattedMessage().contains("[billing-service] Payment 1001 failed"));

        logbackLogger.detachAppender(appender);
    }

    @Test
    void initializeWithBlankNameFails() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> ServiceLoggerFactory.initialize(" "));
        assertEquals("serviceName must not be null or blank", exception.getMessage());
    }

    @Test
    void getLoggerWithoutInitializationFails() {
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> ServiceLoggerFactory.getLogger(ServiceLoggerFactoryTest.class));
        assertTrue(exception.getMessage().contains("initialize(serviceName)"));
    }
}
