package com.vozh.art.gateway.config;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("data-service-mvc", r ->
                        r.path("/api/data/**")
                                .uri("lb://data-service-mvc"))
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
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http){
        http
                .authorizeExchange((authorize) -> authorize
                        .pathMatchers("/eureka/**").permitAll()
                        .pathMatchers("/api/data/dev/**").hasAuthority("registered-user")
                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();
    }

}
