package starter.core.service.api

import org.springframework.stereotype.Service
import starter.core.dto.mapper.UserMapper
import starter.core.dto.request.InitUserRequest
import starter.core.dto.response.UserResponse
import starter.core.entity.UserEntity
import starter.core.service.UserService

@Service
class UserApiService(
    private val userService: UserService,
    private val userMapper: UserMapper,
) {

    suspend fun init(user: UserEntity, rq: InitUserRequest): UserResponse {
        rq.avatar?.let { user.avatar = it }
        rq.username?.let { user.username = it }
        userService.save(user)
        return userMapper.toResponse(user)
    }
}