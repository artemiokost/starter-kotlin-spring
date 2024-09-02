package starter.core.config.security

import com.nimbusds.jose.JWSVerifier
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jwt.SignedJWT
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.reactor.mono
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.http.HttpHeaders
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import starter.common.error.UnauthorizedException
import starter.common.token.AuthToken
import starter.common.util.Utils.utcNowMilli
import starter.core.service.UserService
import java.sql.Timestamp

@Component
class AuthenticationProvider(
    private val userService: UserService,
    @Value("\${app.jwt.public-key}") private val publicKeyPath: String,
    @Value("\${app.jwt.issuer}") private val issuer: String,
) {

    companion object {
        const val AUTH_HEADER_PREFIX = "Bearer "
    }

    private lateinit var verifier: JWSVerifier

    @PostConstruct
    fun init() {
        val publicKeyResource = ClassPathResource(publicKeyPath)
        val publicKeyContent = publicKeyResource.inputStream.bufferedReader().use { it.readText() }
        val publicKey = RSAKey.parse(publicKeyContent)
        verifier = RSASSAVerifier(publicKey)
    }

    private fun toAuthToken(signedJWT: SignedJWT): AuthToken {
        val claims = signedJWT.jwtClaimsSet
        return AuthToken(
            sub = claims.getStringClaim("sub"),
            username = claims.getStringClaim("username"),
            tokenType = claims.getStringClaim("type"),
            issuer = claims.issuer
        )
    }

    fun resolveToken(request: ServerHttpRequest): String? {
        val authHeader = request.headers.getFirst(HttpHeaders.AUTHORIZATION) ?: return null
        if (!authHeader.startsWith(AUTH_HEADER_PREFIX)) return null
        return authHeader.removePrefix(AUTH_HEADER_PREFIX).trim()
    }

    fun authenticate(request: ServerHttpRequest): Mono<UserAuthenticationToken> {
        return mono {
            val token = resolveToken(request)
            if (token.isNullOrBlank()) throw UnauthorizedException("Token is not valid")

            val tokenObject: SignedJWT = SignedJWT.parse(token)
            if (tokenObject.jwtClaimsSet.expirationTime.before(Timestamp(utcNowMilli()))) {
                throw UnauthorizedException("Token has been expired")
            }
            if (!tokenObject.verify(verifier) && tokenObject.jwtClaimsSet.issuer != issuer) {
                throw UnauthorizedException("Unknown token issuer")
            }

            val telegramId = toAuthToken(tokenObject).sub ?: throw UnauthorizedException()

            val user = userService.getOrCreate(telegramId)
            UserAuthenticationToken(principal = user)
        }
    }
}