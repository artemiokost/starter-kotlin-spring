package starter.common.token

data class AuthToken(
    var sub: String?,
    var issuer: String?,
    var tokenType: String?,
    var username: String?
)
