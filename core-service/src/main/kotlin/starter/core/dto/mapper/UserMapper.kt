package starter.core.dto.mapper

import org.springframework.stereotype.Component
import starter.core.dto.response.UserResponse
import starter.core.entity.UserEntity

@Component
class UserMapper {

    fun toResponse(user: UserEntity): UserResponse {
        return UserResponse(
            id = user.id,
            avatar = user.avatar,
            username = user.username
        )
    }
}