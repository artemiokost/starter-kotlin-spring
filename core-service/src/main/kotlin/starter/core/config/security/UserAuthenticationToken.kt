package starter.core.config.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import starter.core.entity.UserEntity

class UserAuthenticationToken(
    private val principal: UserEntity,
    permissions: List<SimpleGrantedAuthority> = listOf(),
) : AbstractAuthenticationToken(permissions) {
    override fun getCredentials() = null
    override fun getPrincipal() = principal
    override fun isAuthenticated() = true
}