package io.quarkus.vertx.http.runtime.security;

import io.vertx.ext.web.RoutingContext;

/**
 * This class is responsible for catching every HTTP based authentication attempt for all type of HttpAuthenticationMechanism
 */
public interface AuthenticationAware {

    int DEFAULT_PRIORITY = 1000;

    void before(RoutingContext routingContext);

    void fail(RoutingContext routingContext, Throwable throwable);

    void after(RoutingContext routingContext);

    /**
     * Returns a priority which determines in which order AuthenticationAware handle the authentication actions
     *
     * Multiple listeners are sorted in descending order, so the highest priority gets the first chance to intercept action. The
     * default priority is equal to 1000.
     *
     * @return priority
     */
    default int getPriority() {
        return DEFAULT_PRIORITY;
    }
}
