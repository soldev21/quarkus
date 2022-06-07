package io.quarkus.vertx.http.security;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.vertx.http.runtime.security.AuthenticationAware;
import io.vertx.ext.web.RoutingContext;

@ApplicationScoped
public class DummyAuthenticationAware implements AuthenticationAware {
    @Override
    public void before(RoutingContext routingContext) {
        routingContext.request().headers().add("auth-aware-before", "before");
    }

    @Override
    public void fail(RoutingContext routingContext, Throwable throwable) {
        routingContext.response().putHeader("auth-aware-fail", "failed");
        appendBeforeToTheResponse(routingContext, "failed");
    }

    @Override
    public void after(RoutingContext routingContext) {
        routingContext.response().putHeader("auth-aware-after", "after");
        appendBeforeToTheResponse(routingContext, "after");
    }

    private void appendBeforeToTheResponse(RoutingContext routingContext, String action) {
        routingContext.response().putHeader("auth-aware-before",
                routingContext.request().getHeader("auth-aware-before").concat("-" + action));
    }
}
