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

class ContestNotFoundException : BasicException(
    HttpStatus.NOT_FOUND,
    "Contest not found"
)

class ContestResultNotFoundException : BasicException(
    HttpStatus.NOT_FOUND,
    "Contest result not found"
)

class OrderNotException : BasicException(
    HttpStatus.NOT_FOUND,
    "Order not found"
)

class PostbackValidationException : BasicException(
    HttpStatus.BAD_REQUEST,
    "Postback validation error"
)

class RoomValidationException(info: String) : BasicException(
    HttpStatus.NOT_FOUND,
    "Room validation: $info"
)

class RoomNotFoundException : BasicException(
    HttpStatus.NOT_FOUND,
    "Room not found"
)

class SessionValidationException : BasicException(
    HttpStatus.BAD_REQUEST,
    "Session validation error"
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