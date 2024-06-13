package springframework.spring6restmvc.controller;

import com.atlassian.oai.validator.OpenApiInteractionValidator;
import com.atlassian.oai.validator.restassured.OpenApiValidationFilter;
import com.atlassian.oai.validator.whitelist.ValidationErrorsWhitelist;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.ActiveProfiles;

import static com.atlassian.oai.validator.whitelist.rule.WhitelistRules.messageHasKey;
import static io.restassured.RestAssured.given;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)//assigned based on available ports
@Import(BeerControllerRestAssuredTest.TestConfig.class)
@ComponentScan(basePackages = "springframework.spring6restmvc")
public class BeerControllerRestAssuredTest {

    OpenApiValidationFilter validationFilter = new OpenApiValidationFilter(OpenApiInteractionValidator
            .createForSpecificationUrl("oa3.yml")
            .withWhitelist(ValidationErrorsWhitelist.create()
                    .withRule("I don't care about date formating!",
                            messageHasKey("validation.response.body.schema.additionalProperties")))
            .build());

    @Configuration
    public static class TestConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    @LocalServerPort
    Integer localPort;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = localPort;
    }

    @Test
    void beerList() {
        given().contentType(ContentType.JSON)
                .when()
                .filter(validationFilter)//to use swagger request validator
                .get("api/v1/beer")
                .then()
                .assertThat().statusCode(200);
    }
}
