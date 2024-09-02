package starter.common.error

import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode

open class BasicException(
    val status: HttpStatusCode,
    open val description: String?
) : RuntimeException(description)

class SomethingWentWrongException(description: String? = null) : BasicException(
    HttpStatus.INTERNAL_SERVER_ERROR,
    description
)

class UnauthorizedException(
    override val description: String? = null
) : BasicException(
    HttpStatus.UNAUTHORIZED,
    description ?: "Unauthorized"
)

class UserNotFoundException : BasicException(
    HttpStatus.NOT_FOUND,
    "User not found"
)