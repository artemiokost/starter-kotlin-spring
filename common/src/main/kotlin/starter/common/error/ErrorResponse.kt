package starter.common.error

import org.springframework.http.HttpStatusCode

data class ErrorResponse(
    val status: HttpStatusCode,
    val description: String? = null,
) {

    companion object {
        fun build(
            status: HttpStatusCode,
            description: String? = null,
        ) = ErrorResponse(
            status = status,
            description = description
        )
    }
}