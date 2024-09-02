package starter.core.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import starter.common.util.V1
import starter.core.dto.request.InitUserRequest
import starter.core.dto.response.UserResponse
import starter.core.entity.UserEntity
import starter.core.service.api.UserApiService

@RestController
@RequestMapping("$V1/user")
@Tag(name = "User", description = "User controller")
class UserController(
    private val userApiService: UserApiService,
) {

    @PutMapping("/init")
    @Operation(summary = "User init")
    suspend fun init(
        @AuthenticationPrincipal user: UserEntity,
        @RequestBody rq: InitUserRequest,
    ): UserResponse {
        return userApiService.init(user, rq)
    }
}