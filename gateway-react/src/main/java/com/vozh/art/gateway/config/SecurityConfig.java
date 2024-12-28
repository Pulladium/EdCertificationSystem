package com.vozh.art.gateway.config;

import com.vozh.art.gateway.config.filters.AuthorityLogFilter;
//import com.vozh.art.gateway.config.utils.AuthoritiesConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.security.web.server.util.matcher.NegatedServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.PathPatternParserServerWebExchangeMatcher;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Configuration
//@KeycloakConfiguration
//https://gauthier-cassany.com/posts/spring-boot-keycloak
//https://www.springcloud.io/post/2022-07/spring-boot-keycloak-roles/#gsc.tab=0
@EnableWebFluxSecurity
public class SecurityConfig {
//https://stackoverflow.com/questions/77974795/how-to-resolve-role-based-authentication-using-keycloak-using-spring-webflux-in-apigateway
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);


    /**
     * Flag to enable development mode with relaxed security settings.
     */
    @Value("${app.security.dev-mode:false}")
    private boolean devMode;


    /**
     * Configures custom routes for the application.
     * Defines paths and predicates for different services.
     */
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("data-service-mvc", r ->
                        r.path("/api/data/**")
                                .uri("lb://data-service"))
                .route("no-csrf-data-service", r -> r
                                .path("/no-csrf/api/data/**")
                                .and().predicate(exchange -> devMode)
                                .filters(f -> f.rewritePath("^/no-csrf(?<segment>/.*)$", "${segment}"))
                                .uri("lb://data-service")
                                )
//                .route("no-csrf-data-service", r ->
//                        r.path("/no-csrf/api/data/**")
//                                .filters(f -> f.stripPrefix(1))
//                                .uri("lb://data-service"))


                .route("processing-service", r ->
                        r.path("/api/processing/**")
                                .uri("lb://processing-service"))
                .route("eureka-server", r -> r.path("/eureka/web/**")
                        .filters(f -> f.setPath("/"))
                        .uri("http://localhost:8761"))
                .route("eureka-server-static-resources", r -> r.path("/eureka/**")
                        .uri("http://localhost:8761"))
                .build();
    }
    //todo change to pathMathcers.permit and others authinticated

    /**
     * Configures the security filter chain for the application.
     * Defines authorization rules, CSRF protection, and authentication methods.
     */
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http
    ,Converter<Jwt, Mono<AbstractAuthenticationToken>> authenticationConverter) throws Exception {
        http

                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .addFilterBefore(new AuthorityLogFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
                .authorizeExchange((authorize) -> authorize
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/api/data/dev/**").hasRole("admin")
                        .pathMatchers("/api/data/certificates/**").hasRole("registered-user")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(jwtDecoder ->
                        jwtDecoder.jwtAuthenticationConverter(
                                authenticationConverter
                        ))

                );
        if (devMode) {
            ServerWebExchangeMatcher csrfMatcher = new NegatedServerWebExchangeMatcher(
                    new PathPatternParserServerWebExchangeMatcher("/no-csrf/**")
            );
            http.csrf(csrf -> csrf.requireCsrfProtectionMatcher(csrfMatcher));



        }

        http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
//        why it is no writen that csrf.disable is deprecated as before but ok
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
    /**
     * Creates a CorsWebFilter bean to apply CORS policies.
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        corsConfig.setMaxAge(8000L);
        corsConfig.addAllowedMethod("*");
        corsConfig.addAllowedHeader("*");

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
    @Bean
    ReactiveJwtAuthenticationConverter jwtAuthenticationConverter(Converter<Jwt, Flux<GrantedAuthority>> authoritiesConverter) {
        final var jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return jwtAuthenticationConverter;
    }

    static interface KeycloakReactiveAuthoritiesConverter extends Converter<Jwt, Flux<GrantedAuthority>> {}

    @SuppressWarnings("unchecked")
    @Bean
    KeycloakReactiveAuthoritiesConverter authoritiesConverter() {
        return jwt -> {
            final List<String> allRoles = new ArrayList<>();
            final var realmAccess = (Map<String, Object>) jwt.getClaims().getOrDefault("realm_access", Map.of());
            allRoles.addAll((List<String>) realmAccess.getOrDefault("roles", List.of()));

            final var resourceAccess = (Map<String, Object>) jwt.getClaims().getOrDefault("resource_access", Map.of());
            for(final var clientId : resourceAccess.keySet()) {
                final var clientAccess = (Map<String, Object>) resourceAccess.getOrDefault(clientId, Map.of());
                allRoles.addAll((List<String>) clientAccess.getOrDefault("roles", List.of()));
            }

            allRoles.addAll((List<String>) jwt.getClaims().getOrDefault("roles", List.of()));

            return Flux.fromStream(allRoles.stream().map(r -> "ROLE_%s".formatted(r)).map(SimpleGrantedAuthority::new));
        };
    }
}
