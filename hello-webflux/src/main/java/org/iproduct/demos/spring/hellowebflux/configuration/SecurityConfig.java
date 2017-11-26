package org.iproduct.demos.spring.hellowebflux.configuration;

import org.iproduct.demos.spring.hellowebflux.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;

@Configuration("org.iproduct.demos.spring.hellowebflux")
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http.csrf().disable()
            .authorizeExchange()
                .pathMatchers(HttpMethod.GET, "/api/users").permitAll()
                .pathMatchers(HttpMethod.POST, "/api/users/**").permitAll()
                .pathMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")
                //.pathMatchers("/api/users/{user}/**").access(this::currentUserMatchesPath)
                .anyExchange().permitAll()
            .and()
                .httpBasic()
            .and()
                .build();
    }

//    @Bean
//    public MapReactiveUserDetailsService userDetailsRepository() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("test")
//                .password("test123")
//                .roles("USER")
//                .build();
//        return new MapReactiveUserDetailsService(user);
//    }

    @Bean
    public ReactiveUserDetailsService userDetailsRepository(UserRepository users) {
        return (username) ->
            Flux.fromIterable(users.findByUsername(username)).next().cast(UserDetails.class);
    }

}

//class SecurityConfig {
//
//    @Bean
//    DefaultSecurityFilterChain springWebFilterChain(HttpSecurity http) throws Exception {
//        return http
//                .authorizeRequests()
//                .mvcMatchers(HttpMethod.GET, "/users/**").permitAll()
//                .mvcMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
//                //.pathMatchers("/users/{user}/**").access(this::currentUserMatchesPath)
//                .anyRequest().authenticated()
//                .and()
//                .build();
//    }
//
//    private Mono<AuthorizationDecision> currentUserMatchesPath(Mono<Authentication> authentication, AuthorizationContext context) {
//        return authentication
//                .map( a -> context.getVariables().get("user").equals(a.getName()))
//                .map( granted -> new AuthorizationDecision(granted));
//    }
//
//    @Bean
//    public UserDetailsService userDetailsRepository() {
//        UserDetails rob = User.withUsername("test").password("test123").roles("USER").build();
//        UserDetails admin = User.withUsername("admin").password("admin123").roles("USER","ADMIN").build();
//        return new InMemoryUserDetailsManager(rob, admin);
//    }
