package io.quarkus.vertx.http.security;

import static org.hamcrest.Matchers.containsString;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.security.test.utils.TestIdentityController;
import io.restassured.RestAssured;

public abstract class AbstractAuthenticationAwareTestCase {

    protected static final String APP_PROPS = "" +
            "quarkus.http.auth.proactive=false\n" +
            "quarkus.http.auth.permission.basic.paths=/authentication-aware/*\n" +
            "quarkus.http.auth.permission.basic.policy=authenticated\n" +
            "quarkus.http.auth.basic=true\n" +
            "quarkus.security.users.embedded.enabled=true\n" +
            "quarkus.security.users.embedded.plain-text=true\n" +
            "quarkus.security.users.embedded.users.admin=admin\n" +
            "quarkus.security.users.embedded.roles.admin=password\n";

    @BeforeAll
    public static void setup() {
        TestIdentityController.resetRoles()
                .add("admin", "password", "admin");
    }

    @Test
    public void testAuthAwareFail() {
        RestAssured
                .given()
                .auth()
                .preemptive()
                .basic("admin", "wrong_password")
                .when()
                .get("/authentication-aware/fail")
                .then()
                .assertThat()
                .statusCode(401)
                .header("auth-aware-fail", containsString("failed"))
                .header("auth-aware-before", containsString("before-failed"));

    }

    @Test
    public void testAuthAwareSuccess() {

        RestAssured
                .given()
                .auth()
                .preemptive()
                .basic("admin", "password")
                .when()
                .get("/authentication-aware/success")
                .then()
                .assertThat()
                .statusCode(200)
                .header("auth-aware-before", containsString("before-after"))
                .body(containsString("admin:/authentication-aware/success"));

    }
}
