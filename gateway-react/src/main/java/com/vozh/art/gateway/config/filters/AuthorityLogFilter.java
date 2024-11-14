package com.vozh.art.gateway.config.filters;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNullApi;

import java.util.Collection;

public class AuthorityLogFilter implements WebFilter {
    private static final Logger log = LoggerFactory.getLogger(AuthorityLogFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .doOnNext(authentication -> {
                    if (authentication != null && authentication.isAuthenticated()) {
                        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
                        String path = exchange.getRequest().getPath().value();
                        log.info("User '{}' accessing path: {} with authorities: {}",
                                authentication.getName(),
                                path,
                                authorities);
                    }
                })
                .then(chain.filter(exchange));
    }
}
