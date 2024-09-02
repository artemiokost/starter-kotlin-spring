package starter.core.config.security

import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import starter.core.config.RequestWebFilter
import starter.core.config.security.SecurityConfig.Companion.UNSECURED_PATH_MATCHER

@Component
class AuthenticationFilter(
    private val authenticationProvider: AuthenticationProvider,
) : RequestWebFilter() {

    override fun filter(
        exchange: ServerWebExchange,
        chain: WebFilterChain,
    ): Mono<Void> {
        return UNSECURED_PATH_MATCHER.matches(exchange).flatMap { matchResult ->
            if (matchResult.isMatch) {
                chain.filter(exchange)
            } else {
                authenticationProvider.authenticate(exchange.request)
                    .flatMap {
                        val context = ReactiveSecurityContextHolder.withAuthentication(it)
                        chain.filter(exchange).contextWrite(context)
                    }
                    .onErrorResume { handleException(it, exchange) }
            }
        }
    }
}