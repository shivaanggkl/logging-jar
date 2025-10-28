package com.example.logging;

import org.slf4j.Logger;

/**
 * A lightweight wrapper around SLF4J's {@link Logger} that prefixes each log message with a service name.
 * <p>
 * The wrapper keeps the semantics of SLF4J's parameterized logging, simply injecting the service name before
 * the original message.
 */
public final class ServiceLogger {

    private final Logger delegate;
    private final String serviceName;

    ServiceLogger(Logger delegate, String serviceName) {
        this.delegate = delegate;
        this.serviceName = serviceName;
    }

    public void trace(String message, Object... args) {
        if (delegate.isTraceEnabled()) {
            delegate.trace(prefix(message), args);
        }
    }

    public void debug(String message, Object... args) {
        if (delegate.isDebugEnabled()) {
            delegate.debug(prefix(message), args);
        }
    }

    public void info(String message, Object... args) {
        if (delegate.isInfoEnabled()) {
            delegate.info(prefix(message), args);
        }
    }

    public void warn(String message, Object... args) {
        if (delegate.isWarnEnabled()) {
            delegate.warn(prefix(message), args);
        }
    }

    public void error(String message, Object... args) {
        if (delegate.isErrorEnabled()) {
            delegate.error(prefix(message), args);
        }
    }

    public void error(String message, Throwable throwable) {
        if (delegate.isErrorEnabled()) {
            delegate.error(prefix(message), throwable);
        }
    }

    private String prefix(String message) {
        if (message == null) {
            return String.format("[%s]", serviceName);
        }
        return String.format("[%s] %s", serviceName, message);
    }
}
