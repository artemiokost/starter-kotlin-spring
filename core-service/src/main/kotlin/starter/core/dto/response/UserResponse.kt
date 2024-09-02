package starter.core.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UserResponse(
    var id: Long? = null,
    var avatar: String? = null,
    var username: String? = null,
)