package com.vozh.art.gateway.config;

import com.vozh.art.gateway.config.filters.AuthorityLogFilter;
//import com.vozh.art.gateway.config.utils.AuthoritiesConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
//@KeycloakConfiguration
//https://gauthier-cassany.com/posts/spring-boot-keycloak
//https://www.springcloud.io/post/2022-07/spring-boot-keycloak-roles/#gsc.tab=0
@EnableWebFluxSecurity
public class SecurityConfig {
//https://stackoverflow.com/questions/77974795/how-to-resolve-role-based-authentication-using-keycloak-using-spring-webflux-in-apigateway
    private static final Logger log = LoggerFactory.getLogger(SecurityConfig.class);


    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("data-service-mvc", r ->
                        r.path("/api/data/**")
                                .uri("lb://data-service"))
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

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http
    ,Converter<Jwt, Mono<AbstractAuthenticationToken>> authenticationConverter) throws Exception {
        http
                .addFilterBefore(new AuthorityLogFilter(), SecurityWebFiltersOrder.AUTHORIZATION)
                .authorizeExchange((authorize) -> authorize
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/api/data/dev/**").hasAuthority("admin")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(jwtDecoder ->
                        jwtDecoder.jwtAuthenticationConverter(
                                authenticationConverter
                        )));
        http.securityContextRepository(NoOpServerSecurityContextRepository.getInstance());
//        why it is no writen that csrf.disable is deprecated as before but ok
        http.csrf(ServerHttpSecurity.CsrfSpec::disable);
        return http.build();
    }

    @Bean
    ReactiveJwtAuthenticationConverter jwtAuthenticationConverter(Converter<Jwt, Flux<GrantedAuthority>> authoritiesConverter) {
        final var jwtAuthenticationConverter = new ReactiveJwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(authoritiesConverter);
        return jwtAuthenticationConverter;
    }

    static interface ReactiveAuthoritiesConverter extends Converter<Jwt, Flux<GrantedAuthority>> {}

    @SuppressWarnings("unchecked")
    @Bean
    ReactiveAuthoritiesConverter authoritiesConverter() {
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
