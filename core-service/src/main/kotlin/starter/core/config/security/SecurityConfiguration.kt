package starter.core.config.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.SecurityWebFiltersOrder
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatcher
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebFluxSecurity
class SecurityConfig {

    companion object {
        val UNSECURED_PATH_MATCHER: ServerWebExchangeMatcher = ServerWebExchangeMatchers.pathMatchers(
            "/actuator/**",
            "/docs/**",
            "/v3/api-docs/**"
        )
    }

    @Bean
    fun securityWebFilterChain(
        authenticationFilter: AuthenticationFilter,
        http: ServerHttpSecurity,
    ): SecurityWebFilterChain {
        return http
            .csrf { csrfSpec -> csrfSpec.disable() }
            .authorizeExchange { exchanges ->
                exchanges
                    .pathMatchers(HttpMethod.OPTIONS)
                    .permitAll()
                    .matchers(UNSECURED_PATH_MATCHER)
                    .permitAll()
                    .anyExchange()
                    .authenticated()
            }
            .addFilterAt(authenticationFilter, SecurityWebFiltersOrder.AUTHENTICATION)
            .build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowCredentials = true
        configuration.allowedOriginPatterns = listOf("*")
        configuration.addAllowedHeader(HttpHeaders.AUTHORIZATION)
        configuration.addAllowedHeader(HttpHeaders.CONTENT_TYPE)
        configuration.addAllowedHeader("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "OPTIONS")

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }
}