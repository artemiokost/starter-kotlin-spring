package starter.core.service

import org.springframework.stereotype.Service
import starter.common.error.UserNotFoundException
import starter.core.entity.UserEntity
import starter.core.repository.UserRepository

@Service
class UserService(
    private val userRepository: UserRepository,
) {

    suspend fun create(
        username: String,
        avatar: String? = null,
    ): UserEntity {
        val user = UserEntity().apply {
            this.avatar = avatar
            this.username = username
        }
        return save(user)
    }

    suspend fun getOrCreate(username: String): UserEntity {
        return getByUsernameOrNull(username) ?: create(username)
    }

    suspend fun getByIdOrNull(id: Long) = userRepository.findById(id)

    suspend fun getByIdOrThrow(id: Long): UserEntity {
        return getByIdOrNull(id) ?: throw UserNotFoundException()
    }

    suspend fun getByUsernameOrNull(username: String): UserEntity? {
        return userRepository.findByUsernameAndStatus(username)
    }

    suspend fun getByUsernameOrThrow(telegramId: String): UserEntity {
        return getByUsernameOrNull(telegramId) ?: throw UserNotFoundException()
    }

    suspend fun save(user: UserEntity): UserEntity {
        return userRepository.save(user)
    }
}