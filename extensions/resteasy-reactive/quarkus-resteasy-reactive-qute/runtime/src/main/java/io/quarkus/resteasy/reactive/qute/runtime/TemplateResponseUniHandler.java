package io.quarkus.resteasy.reactive.qute.runtime;

import static io.quarkus.resteasy.reactive.qute.runtime.Util.*;
import static io.quarkus.resteasy.reactive.qute.runtime.Util.toUni;

import org.jboss.resteasy.reactive.server.core.ResteasyReactiveRequestContext;
import org.jboss.resteasy.reactive.server.spi.ServerRestHandler;

import io.quarkus.arc.Arc;
import io.quarkus.qute.Engine;
import io.quarkus.qute.TemplateInstance;
import io.smallrye.mutiny.Uni;

public class TemplateResponseUniHandler implements ServerRestHandler {

    private volatile Engine engine;

    @Override
    public void handle(ResteasyReactiveRequestContext requestContext) {
        Object result = requestContext.getResult();
        if (!(result instanceof TemplateInstance)) {
            return;
        }

        if (engine == null) {
            synchronized (this) {
                if (engine == null) {
                    engine = Arc.container().instance(Engine.class).get();
                }
            }
        }
        requestContext.setResult(createUni(requestContext, (TemplateInstance) result, engine));
    }

    private Uni<String> createUni(ResteasyReactiveRequestContext requestContext, TemplateInstance result, Engine engine) {
        setSelectedVariant(result, requestContext.getRequest(),
                requestContext.getHttpHeaders().getAcceptableLanguages());
        return toUni(result, engine);
    }

}
