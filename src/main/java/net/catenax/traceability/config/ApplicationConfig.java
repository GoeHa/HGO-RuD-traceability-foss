package net.catenax.traceability.config;

import net.catenax.traceability.docs.SwaggerPageable;
import org.keycloak.adapters.springboot.KeycloakSpringBootConfigResolver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import springfox.documentation.builders.OAuth2SchemeBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Configuration
@ConfigurationPropertiesScan
@EnableOpenApi
@EnableWebMvc
public class ApplicationConfig {

	@Value("${keycloak.realm:}")
	private String realm;

	@Value("${keycloak.resource:}")
	private String clientId;

	@Value("${keycloak.auth-server-url:}")
	private String authServerUrl;

	@Bean
	public InternalResourceViewResolver defaultViewResolver() {
		return new InternalResourceViewResolver();
	}

	@Bean
	public KeycloakSpringBootConfigResolver keycloakConfigResolver() {
		return new KeycloakSpringBootConfigResolver();
	}

	@Bean
	public Docket swaggerSpringMvcPlugin() {
		return new Docket(DocumentationType.OAS_30)
			.securitySchemes(List.of(authenticationScheme()))
			.securityContexts(List.of(securityContext()))
			.select()
			.apis(RequestHandlerSelectors.basePackage("net.catenax.traceability"))
			.build()
			.directModelSubstitute(Pageable.class, SwaggerPageable.class);
	}

	@Bean
	public SecurityConfiguration security() {
		return SecurityConfigurationBuilder.builder()
			.clientId(clientId)
			.realm(realm)
			.appName(clientId)
			.scopeSeparator(",")
			.additionalQueryStringParams(null)
			.build();
	}

	private SecurityScheme authenticationScheme() {
		return new OAuth2SchemeBuilder("authorizationCode")
			.name("Keycloak")
			.authorizationUrl(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/auth")
			.tokenUrl(authServerUrl + "/realms/" + realm + "/protocol/openid-connect/token")
			.scopes(authorizationScopes())
			.build();
	}

	private List<AuthorizationScope> authorizationScopes() {
		return Arrays.asList(
			new AuthorizationScope("uma_authorization", "UMA authorization")
		);
	}

	private SecurityContext securityContext() {
		return SecurityContext
			.builder()
			.securityReferences(readAccessAuth())
			.operationSelector(operationContext -> HttpMethod.GET.equals(operationContext.httpMethod()))
			.build();
	}

	private List<SecurityReference> readAccessAuth() {
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[] { authorizationScopes().get(0) };
		return Collections.singletonList(
			new SecurityReference("keycloak", authorizationScopes)
		);
	}

}
