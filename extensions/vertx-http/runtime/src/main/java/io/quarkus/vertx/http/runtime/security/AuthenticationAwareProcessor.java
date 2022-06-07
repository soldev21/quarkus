package io.quarkus.vertx.http.runtime.security;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.enterprise.inject.Instance;
import javax.inject.Singleton;

import io.vertx.ext.web.RoutingContext;

/**
 * Class that is responsible for collecting user defined AuthenticationAware implementations and perform according action
 * through all instances
 */
@Singleton
public class AuthenticationAwareProcessor {

    final AuthenticationAware[] authenticationAwares;

    public AuthenticationAwareProcessor(Instance<AuthenticationAware> authenticationAwares) {
        List<AuthenticationAware> authenticationAwareList = new ArrayList<>();
        for (AuthenticationAware authenticationAware : authenticationAwares) {
            authenticationAwareList.add(authenticationAware);
        }
        authenticationAwareList.sort(new Comparator<AuthenticationAware>() {
            @Override
            public int compare(AuthenticationAware aware1, AuthenticationAware aware2) {
                //descending order
                return Integer.compare(aware1.getPriority(), aware2.getPriority());
            }
        });
        this.authenticationAwares = authenticationAwareList.toArray(new AuthenticationAware[authenticationAwareList.size()]);
    }

    public void processFail(RoutingContext routingContext, Throwable throwable) {
        for (AuthenticationAware authenticationAware : authenticationAwares) {
            authenticationAware.fail(routingContext, throwable);
        }
    }

    public void processBefore(RoutingContext routingContext) {
        for (AuthenticationAware authenticationAware : authenticationAwares) {
            authenticationAware.before(routingContext);
        }
    }

    public void processAfter(RoutingContext routingContext) {
        for (AuthenticationAware authenticationAware : authenticationAwares) {
            authenticationAware.after(routingContext);
        }
    }
}
