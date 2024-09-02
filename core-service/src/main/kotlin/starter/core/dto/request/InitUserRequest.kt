package starter.core.dto.request

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class InitUserRequest(
    val avatar: String? = null,
    val username: String? = null,
)