package starter.core.config

import jakarta.validation.ConstraintViolationException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.*
import org.springframework.http.HttpStatusCode
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import starter.common.error.BasicException
import starter.common.error.ErrorResponse
import starter.common.error.UnauthorizedException
import starter.common.util.Utils.jsonMapper
import java.util.concurrent.TimeoutException

@Component
class RequestWebFilter : WebFilter {

    private val logger = LoggerFactory.getLogger(this::class.simpleName)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val request = exchange.request
        logger.info("${request.method} ${request.uri} ${request.remoteAddress}")
        return chain
            .filter(exchange)
            .onErrorResume { handleException(it, exchange) }
    }

    fun handleException(ex: Throwable, exchange: ServerWebExchange): Mono<Void> {
        val (status, description) = when (ex) {
            is TimeoutException -> REQUEST_TIMEOUT to "Request timed out"
            is UnauthorizedException -> UNAUTHORIZED to ex.description
            is ConstraintViolationException -> TOO_MANY_REQUESTS to ex.message
            is BasicException -> BAD_REQUEST to ex.description
            is ResponseStatusException -> ex.statusCode to ex.reason
            else -> {
                logger.error("Unhandled exception", ex)
                INTERNAL_SERVER_ERROR to ex.message
            }
        }
        return buildErrorResponse(status, description, exchange)
    }

    private fun buildErrorResponse(
        statusCode: HttpStatusCode,
        description: String?,
        exchange: ServerWebExchange,
    ): Mono<Void> {
        exchange.response.statusCode = statusCode
        exchange.response.headers.contentType = APPLICATION_JSON

        val errorResponse = ErrorResponse.build(
            status = statusCode,
            description = description
        )

        val byteArray = jsonMapper.writeValueAsString(errorResponse).toByteArray()
        val body = exchange.response.bufferFactory().wrap(byteArray)
        return exchange.response.writeWith(Mono.just(body))
    }
}