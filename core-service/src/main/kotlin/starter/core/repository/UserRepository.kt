package starter.core.repository

import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.stereotype.Repository
import starter.common.type.UserStatusType
import starter.core.entity.UserEntity

@Repository
interface UserRepository : CoroutineCrudRepository<UserEntity, Long> {

    suspend fun findByUsernameAndStatus(
        telegramId: String,
        status: UserStatusType = UserStatusType.ACTIVE,
    ): UserEntity?
}