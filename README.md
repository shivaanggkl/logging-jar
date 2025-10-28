# Service Logging

A tiny SLF4J-based helper that prefixes every log message with a microservice name. The library is intended to be
packaged as a JAR and consumed by Spring Boot services so that a consistent log format can be achieved with minimal
boilerplate.

## Usage

Add the dependency to your Maven project (after publishing the JAR to your internal repository):

```xml
<dependency>
    <groupId>com.example</groupId>
    <artifactId>service-logging</artifactId>
    <version>0.1.0-SNAPSHOT</version>
</dependency>
```

Initialise the factory during application bootstrap (for example in a Spring `@Configuration` class):

```java
ServiceLoggerFactory.initialize("orders-service");
```

Then obtain loggers as needed:

```java
private static final ServiceLogger LOG = ServiceLoggerFactory.getLogger(MyComponent.class);

LOG.info("Handling request {}", requestId);
```

Every log line emitted through the `ServiceLogger` will automatically include the service name prefix:

```
[orders-service] Handling request 123
```

If you prefer not to use the shared service name you can create a logger with an explicit name for a given component:

```java
ServiceLogger customLogger = ServiceLoggerFactory.getLogger(MyComponent.class, "legacy-service");
```

## Building

Run the unit tests and build the jar:

```bash
mvn clean package
```
